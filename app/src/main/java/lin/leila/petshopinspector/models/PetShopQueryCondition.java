package lin.leila.petshopinspector.models;

/**
 * Created by javiosyc on 2017/3/18.
 */

public class PetShopQueryCondition {
    private String city;
    private String district;
    private String service;

    public PetShopQueryCondition() {
    }

    public PetShopQueryCondition(String city, String district, String service) {
        this.city = city;
        this.district = district;
        this.service = service;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
