package lin.leila.petshopinspector.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YichuChen on 3/22/17.
 */

public class EmailAddress {

    private HashMap<String,String> emaillist;


    public static void main(String[] args) {
        new EmailAddress();

    }

    public EmailAddress() {

        emaillist = new HashMap<String, String>();
        emaillist.put("台北市", "tcapo077@mail.gov.taipei");
        emaillist.put("臺北市", "tcapo077@mail.gov.taipei");
        emaillist.put("新北市", "agd2070100@ntpc.gov.tw");
        emaillist.put("基隆市", "kl24280677@gmail.com");
        emaillist.put("宜蘭縣", "animal@mail.e-land.gov.tw ");
        emaillist.put("桃園市", "tyadcc@mail.tycg.gov.tw ");
        emaillist.put("新竹市", "petshopinspector@gmail.com");
        emaillist.put("新竹縣", "petshopinspector@gmail.com");
        emaillist.put("苗栗縣", "changchunyi@ems.miaoli.gov.tw");
        emaillist.put("台中市", "petshopinspector@gmail.com");
        emaillist.put("臺中市", "petshopinspector@gmail.com");
        emaillist.put("南投縣", "ntahsc@nantou.gov.tw");
        emaillist.put("彰化縣", "petshopinspector@gmail.com");
        emaillist.put("雲林縣", "adccyl00003@mail.yunlin.gov.tw");
        emaillist.put("嘉義市", "cycg234@ems.chiayi.gov.tw");
        emaillist.put("嘉義縣", "ldcc25@mail.cyhg.gov.tw");
        emaillist.put("台南市", "alan@mail.tainan.gov.tw");
        emaillist.put("臺南市", "alan@mail.tainan.gov.tw");
        emaillist.put("高雄市", "petshopinspector@gmail.com");
        emaillist.put("屏東縣", "petshopinspector@gmail.com");
        emaillist.put("花蓮縣", "hapdcc@hl.gov.tw");
        emaillist.put("台東縣", "G4100@adcc.taitung.gov.tw");
        emaillist.put("臺東縣", "G4100@adcc.taitung.gov.tw");
        emaillist.put("澎湖縣", "petshopinspector@gmail.com");
        emaillist.put("金門縣", "petshopinspector@gmail.com");
        emaillist.put("連江縣", "petshopinspector@gmail.com");


        // 想印出HashMap裡的全部資料
        // 注意這裡印出的順序不按姓名、成績，也不按加入順序
        // 因為HashMap有自己的排序方式，若要按姓名排序可用TreeMap但效能較差
        for (String name:emaillist.keySet()) {
            getEmailAddress(name);
        }
        //System.out.println();



    }

    public String getEmailAddress(String name) {
        //System.out.println(name + "\t" + transcript.get(name));
        return  emaillist.get(name);
    }


}
