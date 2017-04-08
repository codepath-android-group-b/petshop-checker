package lin.leila.petshopinspector.models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by YichuChen on 4/1/17.
 */

public class PhoneBook {

    private HashMap<String,String> phonelist;
    private List<String> cityList;

    public static void main(String[] args) {
        new EmailAddress();

    }

    public PhoneBook() {


        phonelist = new HashMap<String, String>();
        phonelist.put("台北市", "0287897158");
        phonelist.put("臺北市", "0287897158");
        phonelist.put("新北市", "0229596353");
        phonelist.put("基隆市", "0224238660");
        phonelist.put("宜蘭縣", "039602350");
        phonelist.put("桃園市", "033326742");
        phonelist.put("桃園縣", "033326742");//in case information is too old
        phonelist.put("新竹市", "035216121");
        phonelist.put("新竹縣", "035519548");
        phonelist.put("苗栗縣", "037320049");
        phonelist.put("台中市", "043869420");
        phonelist.put("臺中市", "043869420");
        phonelist.put("南投縣", "0492222542");
        phonelist.put("彰化縣", "047620774");
        phonelist.put("雲林縣", "055523250");
        phonelist.put("嘉義市", "052254321");
        phonelist.put("嘉義縣", "053620025");
        phonelist.put("台南市", "062130958");
        phonelist.put("臺南市", "062130958");
        phonelist.put("高雄市", "07223721");
        phonelist.put("屏東縣", "087224109");
        phonelist.put("花蓮縣", "038227431");
        phonelist.put("台東縣", "089233720");
        phonelist.put("臺東縣", "089233720");
        phonelist.put("澎湖縣", "069212839");
        phonelist.put("金門縣", "082336626");
        phonelist.put("連江縣", "083625348");
        for (String name:phonelist.keySet()) {
            getPhoneNumber(name);
        }


    }

    public String getPhoneNumber(String name) {

        return  phonelist.get(name);
    }


}
