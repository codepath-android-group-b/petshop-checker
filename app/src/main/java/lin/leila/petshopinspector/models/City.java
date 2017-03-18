package lin.leila.petshopinspector.models;

import java.util.List;

/**
 * Created by javiosyc on 2017/3/18.
 */

public class City {
    private int id;
    private String name;
    private List<District> districtList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList = districtList;
    }
}
