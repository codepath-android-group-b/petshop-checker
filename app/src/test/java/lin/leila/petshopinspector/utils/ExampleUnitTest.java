package lin.leila.petshopinspector.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import lin.leila.petshopinspector.models.City;
import lin.leila.petshopinspector.models.District;
import lin.leila.petshopinspector.models.PetShop;

import static lin.leila.petshopinspector.utils.PetShopUtils.parseJson;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() {

        StringBuilder text = getJsonString();

        List<PetShop> petShops = new ArrayList<>();

        List<String> results = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(text.toString());

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                petShops.add(PetShopUtils.parseJson(object));

                results.add(parseJson(object).getAddress());
            }

        } catch (JSONException e) {
            throw new RuntimeException("parse data error");
        }


        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("test.md"));

            for (String result : results) {
                writer.write(result);
                writer.newLine();
            }

        } catch (IOException e) {
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
            }
        }
    }

    @Test
    public void test2() throws IOException {

        List<String> locationString = new ArrayList<>();

        String line;

        InputStream fis = new FileInputStream("location.md");
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);

        while ((line = br.readLine()) != null) {
            locationString.add(line);
        }

        System.out.println(locationString.size());

    }

    public StringBuilder getJsonString2() {
        StringBuilder text = new StringBuilder();
        File jsonFile = new File("animal-coa-cert.json");

        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("Loading data have error!");
        }
        return text;
    }

    public StringBuilder getJsonString() {
        StringBuilder text = new StringBuilder();
        File jsonFile = new File("animal-coa-cert.json");

        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("Loading data have error!");
        }
        return text;
    }


    /*
  cert_no	cert_date assistant		shop_name	county	manager	address	services	cert_grade	location
   */
    public JSONObject convertObjectToJson(PetShop petShop) {
        JSONObject object = new JSONObject();
        try {
            object.put("cert_no", petShop.getCertNo());
            object.put("cert_date", petShop.getCertDate());
            object.put("cert_grade", petShop.getCertGrade());


            object.put("assistant", petShop.getAssistant().replace("專任人員：", ""));
            object.put("shop_name", petShop.getShopName());

            String address = petShop.getAddress();
            String city = petShop.getCity();
            String district = parseDistrict(city, address);
            object.put("city", city);
            object.put("district", district);
            object.put("address", petShop.getAddress());

            List<String> services = petShop.getServices();

            object.put("services", parseServieObjectFrom(services));

            object.put("latitude", petShop.getLatitude());
            object.put("longitude", petShop.getLongitude());
            object.put("manager", petShop.getManager());

        } catch (JSONException e) {
            throw new RuntimeException("convert error");
        }
        return object;
    }


    @Test
    public void testJson() {
        StringBuilder text = getJsonString();
        List<PetShop> petShops = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(text.toString());

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                petShops.add(parseJson(object));
            }

        } catch (JSONException e) {
            throw new RuntimeException("parse data error");
        }

        JSONArray results = new JSONArray();


        for (PetShop petShop : petShops) {
            JSONObject object = convertObjectToJson(petShop);
            results.put(object);
        }

        writeJsonToFile(results);
    }

    public void writeJsonToFile(JSONArray array) {

        FileWriter file = null;
        try {
            file = new FileWriter("newJson.json");
            file.write(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private JSONArray parseServieObjectFrom(List<String> services) {

        JSONArray array = new JSONArray();

        for (String service : services) {
            array.put(service);
        }
        return array;
    }

    private String parseDistrict(String cityName, String address) {

        System.out.println(cityName + "[" + address + "]");

        if (address.equals("0903331309, 0952858002") || address.equals("0988822771")) {
            return "";
        }

        if (address.startsWith("嘉義市"))
            return "";

        if (address.startsWith("基市")) {
            address = address.replace("基市", "基隆市");
        }


        if (isEmpty(address) || isEmpty(cityName)) {
            throw new RuntimeException("can't find district");
        }


        cityName = cityName.replace("台", "臺");

        address = address.replace("台", "臺");


        List<City> cities = LocationUtils.getInstance().getCities();

        for (City city : cities) {
            String cityName2 = city.getName().replace("台", "臺");


            if (cityName.equals("新竹市")) {
                return "";
            }

            if (cityName2.equals(cityName)) {
                address = address.replaceAll("\\d+", "");
                address = address.trim();

                if (address.equals("基隆市愛一路號樓")) {
                    address = "基隆市仁愛區愛一路號樓";
                }

                if (address.equals("基隆市信一路號樓")) {
                    address = "基隆市信義區信一路號樓";
                }

                if (address.equals("基隆市安一路巷號樓")) {
                    address = "基隆市安樂區安一路巷號樓";
                }

                if (address.equals("基隆市北寧路巷、號")) {
                    address = "基隆市中正區北寧路巷、號";
                }

                for (District district : city.getDistrictList()) {
                    String districtName = district.getName();

                    if (address.indexOf(cityName + districtName) >= 0 || address.startsWith(districtName)) {
                        return districtName;
                    }

                    districtName = districtName.replaceAll("區", "市");

                    if (address.indexOf(cityName + districtName) >= 0 || address.startsWith(districtName)) {
                        return districtName;
                    }


                    districtName = districtName.replaceAll("區", "鎮");
                    if (address.indexOf(cityName + districtName) >= 0 || address.startsWith(districtName)) {
                        return districtName;
                    }


                    address = address.replace("桃園縣", "桃園市");
                    if (address.indexOf(cityName + districtName) >= 0 || address.startsWith(districtName)) {
                        return districtName;
                    }

                    address = address.replace("嘉義市", "嘉義縣");
                    if (address.indexOf(cityName + districtName) >= 0 || address.startsWith(districtName)) {
                        return districtName;
                    }

                    address = address.replace("雲林市", "雲林縣");
                    if (address.indexOf(cityName + districtName) >= 0 || address.startsWith(districtName)) {
                        return districtName;
                    }
                    address = address.replace("金門市", "金門縣");
                    if (address.indexOf(cityName + districtName) >= 0 || address.startsWith(districtName)) {
                        return districtName;
                    }
                }
                throw new RuntimeException("2district is not found " + cityName + " [" + city.getName() + "]?" + address);
            }
        }
        throw new RuntimeException("3district is not found!" + cityName + " " + address);
    }

    public boolean isEmpty(String message) {

        return message == null || message.equals("");
    }
}