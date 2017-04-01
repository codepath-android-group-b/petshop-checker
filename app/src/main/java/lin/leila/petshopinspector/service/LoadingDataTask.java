package lin.leila.petshopinspector.service;

import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.utils.AnimalDataUtils;
import lin.leila.petshopinspector.utils.PetShopUtils;

/**
 * Created by javiosyc on 2017/3/26.
 */

public class LoadingDataTask extends AsyncTask<Object, Integer, List<PetShop>> {

    private long version;
    private long totalCount;

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
        PetShopUtils.isUpdatingPetShopData(false, sdf.format(new Date()) , version);
    }

    @Override
    protected List<PetShop> doInBackground(Object... params) {

        DataSnapshot dataSnapshot = (DataSnapshot) params[0];

        String fileName = (String) params[1];

        version = (long) params[2];

        totalCount = (long) params[3];

        List<PetShop> petShops = new ArrayList<>();

        if (totalCount == 0)
            return petShops;

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            PetShop petShop = child.getValue(PetShop.class);

            petShops.add(petShop);
        }

        JSONArray petShopsJsonArray = new JSONArray();

        for (PetShop petShop : petShops) {
            JSONObject object = PetShopUtils.convertObjectToJson(petShop);
            petShopsJsonArray.put(object);
        }

        AnimalDataUtils.writeJsonToFile(petShopsJsonArray, fileName);

        return petShops;
    }
}
