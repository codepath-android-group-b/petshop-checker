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
    private static final int ALL_CITIY_OPTION_ID = 0;
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
        return condition.getCity().getId() == 0 && condition.getDistrict().getZip() == 0 && TextUtils.isEmpty(condition.getService());
    }

    private boolean isSelected(PetShop petShop, PetShopQueryCondition condition) {
        boolean isSelected = false;

        String service = condition.getService();

        boolean isSameService = isSameService(petShop, service);

        if (condition.getCity().getId() == ALL_CITIY_OPTION_ID) {
            if (isSameService)
                return true;
        }

        String cityName = condition.getCity().getName();

        boolean isSameCity = petShop.getCity().equals(cityName);

        if (isSameCity && condition.getDistrict().getZip() == 0) {
            if (isSameService)
                return true;
        }

        String districtName = condition.getDistrict().getName();

        boolean isSameDist = petShop.getAddress().indexOf(districtName) >= 0;

        if (isSameCity && isSameDist) {
            if (isSameService) {
                isSelected = true;
            }
        }

        return isSelected;
    }

    private boolean isSameService(PetShop petShop, String service) {
        return service.equals("項目") || petShop.getServices().indexOf(service) >= 0;
    }

    private boolean isConditionEquals(String value, int condition) {
        return value.equals(condition);
    }
}
