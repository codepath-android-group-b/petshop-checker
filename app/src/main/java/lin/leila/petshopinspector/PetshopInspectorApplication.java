package lin.leila.petshopinspector;

import android.app.Application;
import android.util.Log;

import lin.leila.petshopinspector.database.PetShopDB;
import lin.leila.petshopinspector.interfaces.PetShopInterface;
import lin.leila.petshopinspector.models.PetShopSetting;
import lin.leila.petshopinspector.utils.AnimalDataUtils;
import lin.leila.petshopinspector.utils.PetShopUtils;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = RestApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class PetshopInspectorApplication extends Application {

    private static PetShopInterface db;

    @Override
    public void onCreate() {
        super.onCreate();

        PetShopUtils.setContextWrapper(this);
        PetShopSetting setting = PetShopUtils.getPetShopSetting();

        if (!AnimalDataUtils.isDataLoaded(setting)) {
            Log.d("DEBUG", "data is not loaded");
            AnimalDataUtils.copyAnimalDataFromAssets(this);

        }
        db = PetShopDB.getInstance(getFilesDir().getPath());

        AnimalDataUtils.syncData(this);
    }

    public static PetShopInterface getPetShopDB() {
        return db;
    }
}