package lin.leila.petshopinspector.services;

import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.utils.PetShopUtils;

/**
 * Created by javiosyc on 2017/3/26.
 */

public class LoadingDataTask extends AsyncTask<Object, Integer, List<PetShop>> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
        PetShopUtils.isUpdatingPetShopData(true, sdf.format(new Date()));
    }

    @Override
    protected void onPostExecute(List<PetShop> petShops) {
        super.onPostExecute(petShops);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
        PetShopUtils.isUpdatingPetShopData(false, sdf.format(new Date()));
    }

    @Override
    protected List<PetShop> doInBackground(Object... params) {

        DataSnapshot dataSnapshot = (DataSnapshot) params[0];

        String fileName = (String) params[1];

        long totalCount = (long) params[2];

        List<PetShop> petShops = new ArrayList<>();

        if (totalCount == 0)
            return petShops;

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            PetShop petShop = child.getValue(PetShop.class);

            petShops.add(petShop);
        }

        JSONArray petShopsJsonArray = new JSONArray();

        for (PetShop petShop : petShops) {
            JSONObject object = convertObjectToJson(petShop);
            petShopsJsonArray.put(object);
        }

        writeJsonToFile(petShopsJsonArray, fileName);

        return petShops;
    }


    public JSONObject convertObjectToJson(PetShop petShop) {
        JSONObject object = new JSONObject();
        try {
            object.put("certNo", petShop.getCertNo());
            object.put("certDate", petShop.getCertDate());
            object.put("certGrade", petShop.getCertGrade());
            object.put("assistant", petShop.getAssistant());
            object.put("shopName", petShop.getShopName());
            object.put("city", petShop.getCity());
            object.put("district", petShop.getDistrict());
            object.put("address", petShop.getAddress());
            object.put("services", parseServieObjectFrom(petShop.getServices()));
            object.put("latitude", petShop.getLatitude());
            object.put("longitude", petShop.getLongitude());
            object.put("manager", petShop.getManager());

        } catch (JSONException e) {
            throw new RuntimeException("convert error");
        }
        return object;
    }

    private JSONArray parseServieObjectFrom(List<String> services) {
        JSONArray array = new JSONArray();
        for (String service : services) {
            array.put(service);
        }
        return array;
    }

    public void writeJsonToFile(JSONArray array, String fileName) {
        FileWriter file = null;
        try {
            file = new FileWriter(fileName);
            file.write(array.toString());
        } catch (Exception e) {
            throw new RuntimeException("update pet shop data error!");
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                throw new RuntimeException("update pet shop data error!");
            }

        }
    }
}
