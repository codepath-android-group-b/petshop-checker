package lin.leila.petshopinspector.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javiosyc on 2017/3/18.
 */

public class PetShop implements Parcelable {
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
    private String certNo;
    private String city;
    private String district;
    private String assistant;
    private String shopName;
    private String manager;
    private String address;
    private String services;
    private String certGrade;
    private String certDate;
    private double latitude;
    private double longitude;

    public PetShop() {
    }

    protected PetShop(Parcel in) {
        this.certNo = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.assistant = in.readString();
        this.shopName = in.readString();
        this.manager = in.readString();
        this.address = in.readString();
        this.services = in.readString();
        this.certGrade = in.readString();
        this.certDate = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static PetShop parseJson(JSONObject object) {

        PetShop petShop = new PetShop();

        try {
            petShop.setCertGrade(object.getString("cert_grade"));
            petShop.setCertNo(object.getString("cert_no"));
            petShop.setAddress(object.getString("address"));
            petShop.setManager(object.getString("manager"));

            petShop.setCity(object.getString("city"));
            petShop.setCertDate(object.getString("cert_date"));
            petShop.setAssistant(object.getString("assistant"));

            petShop.setDistrict(object.getString("district"));
            petShop.setLatitude(object.getDouble("latitude"));


            JSONArray services = object.getJSONArray("services");

            StringBuilder serviceBuilder = new StringBuilder();
            for (int i = 0; i < services.length(); i++) {
                serviceBuilder.append(services.get(i).toString() + " ");
            }

            petShop.setServices(serviceBuilder.toString());

            petShop.setShopName(object.getString("shop_name"));
            petShop.setLongitude(object.getDouble("longitude"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return petShop;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getCertDate() {
        return certDate;
    }

    public void setCertDate(String certDate) {
        this.certDate = certDate;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.certNo);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.assistant);
        dest.writeString(this.shopName);
        dest.writeString(this.manager);
        dest.writeString(this.address);
        dest.writeString(this.services);
        dest.writeString(this.certGrade);
        dest.writeString(this.certDate);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }
}
