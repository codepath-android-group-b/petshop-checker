package lin.leila.petshopinspector.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by javiosyc on 2017/4/10.
 */

public class FileUtils {
    private static final String EXTERNAL_IMAGE_FOLDER= Environment.getExternalStorageDirectory() + File.separator + "petShop" + File.separator;

    public static File exportFile(File src) throws IOException {

        File dst = new File (EXTERNAL_IMAGE_FOLDER);
        if (!dst.exists()) {
            if (!dst.mkdir()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File expFile = new File(dst.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(expFile).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
        return expFile;
    }

    public static boolean isExternalFile(String path) {
        return path.startsWith(Environment.getExternalStorageDirectory().getPath());
    }
}
