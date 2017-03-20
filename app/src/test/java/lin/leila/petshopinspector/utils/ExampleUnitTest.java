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

import lin.leila.petshopinspector.models.PetShop;

import static lin.leila.petshopinspector.models.PetShop.parseJson;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() {

        StringBuilder text  = getJsonString();

        List<PetShop> petShops = new ArrayList<>();

        List<String> results = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(text.toString());

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                petShops.add(parseJson(object));

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

        List<String> locationString =   new ArrayList<>();

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
}