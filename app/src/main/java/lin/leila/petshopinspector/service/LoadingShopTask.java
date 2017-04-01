package lin.leila.petshopinspector.service;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import lin.leila.petshopinspector.MapUtils;
import lin.leila.petshopinspector.models.PetShop;

/**
 * Created by javiosyc on 2017/3/26.
 */

public class LoadingShopTask extends AsyncTask<Object, PetShop, Void> {

    private GoogleMap map;
    private Context contexts;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Object... params) {

        contexts = (Context) params[0];

        List<PetShop> petShops = (List<PetShop>) params[1];

        map = (GoogleMap) params[2];

        for (PetShop petShop : petShops) {
            publishProgress(petShop);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(PetShop... values) {

        PetShop petShop = values[0];

        LatLng shopLocation = new LatLng(petShop.getLatitude(), petShop.getLongitude());

        MapUtils.addMarker(map,
                shopLocation,
                petShop.getShopName(),
                "",
                MapUtils.createBubble(contexts, 6, ""));

    }
}
