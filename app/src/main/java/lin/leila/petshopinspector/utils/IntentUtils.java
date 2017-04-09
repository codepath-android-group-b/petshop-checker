package lin.leila.petshopinspector.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

/**
 * Created by javiosyc on 2017/4/10.
 */

public class IntentUtils {
    public final static int PHONE_DIALOG_RESULT = 20;


    public static Intent createCallPhoneIntent(Activity activity, String phoneNumber) {
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);

        phoneIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PHONE_DIALOG_RESULT);

            return null;
        } else {
            return phoneIntent;
        }
    }
}
