package lin.leila.petshopinspector.utils;

import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import lin.leila.petshopinspector.models.PetShop;

/**
 * Created by javiosyc on 2017/3/16.
 */

public class AnimalDataUtils {

    private static final String FILE_NAME = "animal-coa-cert.json";

    private static InputStream getAnimalData(AssetManager assetManager) throws IOException {

        InputStream input = null;
        try {
            input = assetManager.open(FILE_NAME);
        } catch (IOException e) {
            Log.d("ERROR", "animal data is not found in asset.", e);
            throw e;
        }
        return input;
    }

    public static boolean isDataLoaded(File filesDir) {
        File file = new File(filesDir, FILE_NAME);
        return file.isFile();
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

    public static void copyAnimalDataFromAssets(AssetManager assets ,String path) {
        InputStream inputStream = null;
        try {
            inputStream = AnimalDataUtils.getAnimalData(assets);

            AnimalDataUtils.loadAnimalDataFromJson(inputStream, path + "/" + FILE_NAME);

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
        File jsonFile = new File(path + "/" + FILE_NAME);

        Log.d("DEBUG", "load data from " + jsonFile + "/" + FILE_NAME);

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
                petShops.add(PetShop.parseJson(object));
            }

        } catch (JSONException e) {
            throw new RuntimeException("parse data error");
        }

        return petShops;
    }
}
