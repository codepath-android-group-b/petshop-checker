package lin.leila.petshopinspector.utils;

import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lin.leila.petshopinspector.R;
import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.models.PetShopSetting;
import lin.leila.petshopinspector.service.LoadingDataTask;


/**
 * Created by javiosyc on 2017/3/16.
 */

public class AnimalDataUtils {

    private static final String TAG = "DEBUG";
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static final String DATA_FILE_NAME = "animal-coa-cert.json";

    private static InputStream getAnimalData(AssetManager assetManager) throws IOException {

        InputStream input = null;
        try {
            input = assetManager.open(DATA_FILE_NAME);
        } catch (IOException e) {
            Log.d("ERROR", "animal data is not found in asset.", e);
            throw e;
        }
        return input;
    }

    public static boolean isDataLoaded(PetShopSetting setting) {
        return !setting.getUpdateDate().equals("");
    }


    private static void loadAnimalDataFromJson(InputStream inputStream, String destFile) {
        byte[] buffer = new byte[4 * 1024]; // or other buffer size
        int read;

        OutputStream output = null;
        try {
            output = new FileOutputStream(destFile);
            while ((read = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyAnimalDataFromAssets(ContextWrapper contextWrapper) {
        InputStream inputStream = null;
        String path = contextWrapper.getFilesDir().getPath();
        try {
            inputStream = AnimalDataUtils.getAnimalData(contextWrapper.getAssets());

            AnimalDataUtils.loadAnimalDataFromJson(inputStream, path + "/" + DATA_FILE_NAME);

            PetShopSetting petShopSetting = new PetShopSetting();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");

            petShopSetting.setUpdating(false);
            petShopSetting.setUpdateDate(sdf.format(new Date()));
            petShopSetting.setFileName(DATA_FILE_NAME);
            petShopSetting.setVersion(1L);

            PetShopUtils.saveSetting(petShopSetting);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<PetShop> getPetShopFromFile(String path) {
        File jsonFile = new File(path + "/" + DATA_FILE_NAME);

        Log.d("DEBUG", "load data from " + jsonFile + "/" + DATA_FILE_NAME);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("Loading data have error!");
        }

        List<PetShop> petShops = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(text.toString());

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                petShops.add(PetShopUtils.parseJson(object));
            }

        } catch (JSONException e) {
            throw new RuntimeException("parse data error");
        }

        return petShops;
    }

    public static void syncData(final ContextWrapper contextWrapper) {

        DatabaseReference myRef = database.getReference(contextWrapper.getString(R.string.data_version_url));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Long serverDataVersion = dataSnapshot.getValue(Long.class);
                Log.d(TAG, "Server version is: " + serverDataVersion.toString());

                PetShopSetting setting = PetShopUtils.getPetShopSetting();

                Long localDataVersion = setting.getVersion();

                Log.d(TAG, "Version is: " + localDataVersion);

                if (serverDataVersion > localDataVersion) {
                    loadingData(contextWrapper.getFilesDir().getPath(), contextWrapper.getString(R.string.data_url), serverDataVersion);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private static void loadingData(final String filesDir, String url, final long version) {

        DatabaseReference petShopRef = database.getReference(url);
        petShopRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long count = dataSnapshot.getChildrenCount();

                Log.d(TAG, "read petshop data count : " + count);

                LoadingDataTask task = new LoadingDataTask();

                task.execute(dataSnapshot, filesDir + "/" + DATA_FILE_NAME, version, count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static void writeJsonToFile(JSONArray array, String fileName) {
        FileWriter file = null;
        try {
            file = new FileWriter(fileName);
            file.write(array.toString());
        } catch (Exception e) {
            throw new RuntimeException("update pet shop data error!", e);
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                throw new RuntimeException("update pet shop data error!", e);
            }
        }
    }
}
