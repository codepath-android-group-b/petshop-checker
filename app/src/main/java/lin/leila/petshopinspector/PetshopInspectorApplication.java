package lin.leila.petshopinspector;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import lin.leila.petshopinspector.utils.AnimalDataUtils;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = RestApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class PetshopInspectorApplication extends Application {
    private static Context context;

    private final String DATA_FILE = "animal-coa-cert-copy.json";

    @Override
    public void onCreate() {
        super.onCreate();

        File file = new File(getFilesDir(), DATA_FILE);

        if (!file.isFile()) {
            Log.d("DEBUG", "file not found");
            InputStream inputStream = null;

            try {
                inputStream = AnimalDataUtils.getAnimalData(getAssets());

                loadAnimalDataFromJson(inputStream, file);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void loadAnimalDataFromJson(InputStream inputStream, File destFile) throws IOException {
        OutputStream output = new FileOutputStream(destFile);
        byte[] buffer = new byte[4 * 1024]; // or other buffer size
        int read;

        while ((read = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        output.flush();
    }

    /*
        public String loadJSONFromAsset() {
            String json = null;
            try {
                InputStream is = getAssets().open("animal-coa-cert.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }
    */
    public void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }
}