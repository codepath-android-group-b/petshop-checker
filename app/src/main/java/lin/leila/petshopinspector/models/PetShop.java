package lin.leila.petshopinspector.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by javiosyc on 2017/3/18.
 */

public class PetShop implements Parcelable {
    private String city;
    private String district;
    private String type;
    private String assistant;
    private String shopName;
    private String manager;
    private String address;
    private String services;
    private String certGrade;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAssistant() {
        return assistant;
    }

    public void setAssistant(String assistant) {
        this.assistant = assistant;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getCertGrade() {
        return certGrade;
    }

    public void setCertGrade(String certGrade) {
        this.certGrade = certGrade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.type);
        dest.writeString(this.assistant);
        dest.writeString(this.shopName);
        dest.writeString(this.manager);
        dest.writeString(this.address);
        dest.writeString(this.services);
        dest.writeString(this.certGrade);
    }

    public PetShop() {
    }

    protected PetShop(Parcel in) {
        this.city = in.readString();
        this.district = in.readString();
        this.type = in.readString();
        this.assistant = in.readString();
        this.shopName = in.readString();
        this.manager = in.readString();
        this.address = in.readString();
        this.services = in.readString();
        this.certGrade = in.readString();
    }

    public static final Creator<PetShop> CREATOR = new Creator<PetShop>() {
        @Override
        public PetShop createFromParcel(Parcel source) {
            return new PetShop(source);
        }

        @Override
        public PetShop[] newArray(int size) {
            return new PetShop[size];
        }
    };
}
