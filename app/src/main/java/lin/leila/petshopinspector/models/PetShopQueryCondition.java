package lin.leila.petshopinspector.models;

/**
 * Created by javiosyc on 2017/3/18.
 */

public class PetShopQueryCondition {
    private City city;
    private District district;
    private String service;

    public PetShopQueryCondition() {
    }

    public PetShopQueryCondition(City city, District district, String service) {
        this.city = city;
        this.district = district;
        this.service = service;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
