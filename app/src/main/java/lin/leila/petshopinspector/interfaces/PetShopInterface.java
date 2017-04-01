package lin.leila.petshopinspector.interfaces;

import java.util.List;

import lin.leila.petshopinspector.models.City;
import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.models.PetShopQueryCondition;

/**
 * Created by javiosyc on 2017/3/18.
 */

public interface PetShopInterface {
    public List<PetShop> getPetShop(PetShopQueryCondition condition);

    public List<String> getType();

    public List<City> getCity();

    public List<PetShop> getPetShopOrderByDistance(double latitude, double longitude,int maxCount);
}
