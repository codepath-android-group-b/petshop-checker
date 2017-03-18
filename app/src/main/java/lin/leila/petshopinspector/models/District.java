package lin.leila.petshopinspector.models;

/**
 * Created by javiosyc on 2017/3/18.
 */
public class District {
    private String name;
    private String zip;

    public District(String name, String zip) {
        this.name = name;
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
