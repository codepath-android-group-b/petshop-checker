package lin.leila.petshopinspector.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import lin.leila.petshopinspector.MapUtils;
import lin.leila.petshopinspector.models.PetShop;

/**
 * Created by javiosyc on 2017/3/26.
 */

public class AddPetShopMarkerTask extends AsyncTask<List<PetShop>, PetShop, Void> {

    private final Context contexts;

    private final GoogleMap map;

    private final boolean appended;

    private final int DEFAULT_DELAY_MILLIS_SEC = 1;

    public AddPetShopMarkerTask(Context contexts, GoogleMap map) {
        this.contexts = contexts;
        this.map = map;
        this.appended = false;
    }

    public AddPetShopMarkerTask(Context contexts, GoogleMap map, boolean appended) {
        this.contexts = contexts;
        this.map = map;
        this.appended = appended;
    }

    @Override
    protected void onPreExecute() {
        Log.d("DEBUG", "CLEAR Method");
        if (map != null && !appended) {
            map.clear();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(List<PetShop>... params) {
        List<PetShop> petShops = params[0];

        for (PetShop petShop : petShops) {
            publishProgress(petShop);
            try {
                Thread.sleep(DEFAULT_DELAY_MILLIS_SEC);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(PetShop... values) {

        if (contexts == null || map == null) {
            return;
        }

        PetShop petShop = values[0];

        LatLng shopLocation = new LatLng(petShop.getLatitude(), petShop.getLongitude());

        Log.d("DEBUG", petShop.getShopName() + " at [ " + petShop.getLatitude() + "," + petShop.getLongitude() + "]");

        MapUtils.addMarker(map, shopLocation, petShop.getShopName(), petShop);
    }
}
