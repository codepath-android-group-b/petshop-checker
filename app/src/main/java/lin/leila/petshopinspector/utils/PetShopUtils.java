package lin.leila.petshopinspector.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.models.PetShopSetting;

/**
 * Created by javiosyc on 2017/3/25.
 */

public class PetShopUtils {

    private static final String UPDATING_DATA = "updatingData";
    private static ContextWrapper contextWrapper;

    private static final String SETTINGS_FILE = "Settings";
    private static final String FILE_NAME = "fileName";
    private static final String UPDATED_DATE = "updatedDate";
    private static final String VERSION = "version";


    public static void setContextWrapper(ContextWrapper contextWrapper) {
        PetShopUtils.contextWrapper = contextWrapper;
    }

    public static PetShopSetting getPetShopSetting() {
        SharedPreferences sharedPreferences = contextWrapper.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
        PetShopSetting setting = getSettingFromPreference(sharedPreferences);
        return setting;
    }

    public static void isUpdatingPetShopData(Boolean status, String date) {
        SharedPreferences sharedPreferences = contextWrapper.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(UPDATING_DATA, status);
        editor.putString(UPDATED_DATE, date);
        editor.apply();
    }


    public static PetShopSetting saveSetting(ContextWrapper contextWrapper, PetShopSetting petShopSetting) {
        SharedPreferences.Editor editor = contextWrapper.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE).edit();

        editor.putString(FILE_NAME, petShopSetting.getFileName());
        editor.putString(UPDATED_DATE, petShopSetting.getUpdateDate());
        editor.putLong(VERSION, petShopSetting.getVersion());

        editor.apply();

        return petShopSetting;
    }


    private static PetShopSetting getSettingFromPreference(SharedPreferences sharedPreferences) {
        PetShopSetting setting = new PetShopSetting();

        setting.setVersion(sharedPreferences.getLong(VERSION, 0));
        setting.setUpdateDate(sharedPreferences.getString(UPDATED_DATE, ""));
        setting.setFileName(sharedPreferences.getString(FILE_NAME, ""));
        setting.setUpdating(sharedPreferences.getBoolean(UPDATING_DATA, false));

        return setting;
    }


    public static PetShop parseJson(JSONObject object) {

        PetShop petShop = new PetShop();

        try {
            petShop.setCertGrade(object.getString("certGrade"));
            petShop.setCertNo(object.getString("certNo"));
            petShop.setAddress(object.getString("address"));
            petShop.setManager(object.getString("manager"));

            petShop.setCity(object.getString("city"));
            petShop.setCertDate(object.getString("certDate"));
            petShop.setAssistant(object.getString("assistant"));

            petShop.setDistrict(object.getString("district"));
            petShop.setLatitude(object.getDouble("latitude"));


            JSONArray services = object.getJSONArray("services");

            List<String> serviceList = new ArrayList<>();
            for (int i = 0; i < services.length(); i++) {

                serviceList.add(services.getString(i));
            }

            petShop.setServices(serviceList);
            petShop.setShopName(object.getString("shopName"));
            petShop.setLongitude(object.getDouble("longitude"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return petShop;
    }
}
