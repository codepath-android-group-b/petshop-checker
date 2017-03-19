package lin.leila.petshopinspector.models;

/**
 * Created by javiosyc on 2017/3/18.
 */
public class District {
    private String name;
    private int zip;

    public District(String name, int zip) {
        this.name = name;
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

}
