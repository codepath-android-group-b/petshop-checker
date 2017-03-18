package lin.leila.petshopinspector.utils;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by javiosyc on 2017/3/16.
 */

public class AnimalDataUtils {

    private final String FILE_NAME = "animal-coa-cert.json";

    public static InputStream getAnimalData(AssetManager assetManager) throws IOException {

        InputStream input = null;
        try {
            input = assetManager.open("animal-coa-cert.json");
        } catch (IOException e) {
            Log.d("ERROR", "animal data is not found.", e);
            throw e;
        }
        return input;
    }




    public static boolean isDataLoaded() {

    }
}
