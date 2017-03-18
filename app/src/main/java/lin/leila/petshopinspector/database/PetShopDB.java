package lin.leila.petshopinspector.database;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import lin.leila.petshopinspector.interfaces.PetShopInterface;
import lin.leila.petshopinspector.models.City;
import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.models.PetShopQueryCondition;
import lin.leila.petshopinspector.utils.AnimalDataUtils;
import lin.leila.petshopinspector.utils.LocationUtils;

/**
 * Created by javiosyc on 2017/3/18.
 */

public class PetShopDB implements PetShopInterface {
    private static List<PetShop> petShopList;

    private static PetShopDB instance;

    private static String basePath;

    private PetShopDB() {
    }

    public static PetShopDB getInstance(String filePath) {
        basePath = filePath;

        if (instance == null) {
            instance = new PetShopDB();
            petShopList = AnimalDataUtils.getPetShopFromFile(basePath);
        }
        return instance;
    }

    @Override
    public List<PetShop> getPetShop(PetShopQueryCondition condition) {
        List<PetShop> results = new ArrayList<>();
        if (petShopList == null)
            return results;

        if (isSelectedAll(condition)) {
            return petShopList;
        }

        for (PetShop petShop : petShopList) {
            if (isSelected(petShop, condition)) {
                results.add(petShop);
            }
        }
        return results;
    }

    @Override
    public List<String> getType() {

        return null;
    }

    @Override
    public List<City> getCity() {
        return LocationUtils.getInstance().getCities();
    }

    private boolean isSelectedAll(PetShopQueryCondition condition) {

        return TextUtils.isEmpty(condition.getCity()) && TextUtils.isEmpty(condition.getDistrict()) && TextUtils.isEmpty(condition.getService());
    }

    private boolean isSelected(PetShop petShop, PetShopQueryCondition condition) {
        boolean isSelected = false;

        String city = condition.getCity();
        String district = condition.getDistrict();
        String service = condition.getService();

        if (isConditionEquals(petShop.getCity(), city)) {
            if (isConditionEquals(petShop.getDistrict(), district)) {
                if (TextUtils.isEmpty(service) || petShop.getServices().indexOf(service) > 0) {
                    isSelected = true;
                }
            }
        }

        return isSelected;
    }

    private boolean isConditionEquals(String value, String condition) {
        return TextUtils.isEmpty(condition) || value.equals(condition);
    }
}
