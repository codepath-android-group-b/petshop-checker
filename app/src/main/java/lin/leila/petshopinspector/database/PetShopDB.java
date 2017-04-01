package lin.leila.petshopinspector.database;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private static final int ALL_OPTION_ID = 0;
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
        Log.d("DEBUG", "getPetShop condition" + condition);

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

        Log.d("DEBUG", "count:" + results.size());
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

    @Override
    public List<PetShop> getPetShopOrderByDistance(final double latitude, final double longitude) {
        if (petShopList == null) {
            return new ArrayList<>();
        }

        List<PetShop> result = new ArrayList<>();

        result.addAll(petShopList);

        Collections.sort(result, new Comparator<PetShop>() {
            @Override
            public int compare(PetShop o1, PetShop o2) {
                float distance1 = LocationUtils.getDistanceBetween(o1, latitude, longitude);

                float distance2 = LocationUtils.getDistanceBetween(o2, latitude, longitude);

                return Float.compare(distance1, distance2);
            }
        });

        return result;
    }

    private boolean isSelectedAll(PetShopQueryCondition condition) {
        return condition.getCity().getId() == 0 && condition.getDistrict().getZip() == 0 && TextUtils.isEmpty(condition.getService());
    }

    private boolean isSelected(PetShop petShop, PetShopQueryCondition condition) {
        return checkCity(petShop, condition) && checkDistrict(petShop, condition) && checkService(petShop, condition);
    }

    private boolean checkService(PetShop petShop, PetShopQueryCondition condition) {
        return condition.getService().equals("項目") || petShop.getServices().indexOf(condition.getService()) >= 0;
    }

    private boolean checkDistrict(PetShop petShop, PetShopQueryCondition condition) {
        if (ALL_OPTION_ID == condition.getDistrict().getZip()) {
            return true;
        }
        return petShop.getDistrict().equals(condition.getDistrict().getName());
    }

    private boolean checkCity(PetShop petShop, PetShopQueryCondition condition) {
        if (ALL_OPTION_ID == condition.getCity().getId()) {
            return true;
        }

        return petShop.getCity().equals(condition.getCity().getName());
    }
}
