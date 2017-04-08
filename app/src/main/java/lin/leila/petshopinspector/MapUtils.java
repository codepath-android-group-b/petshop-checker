package lin.leila.petshopinspector;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by Leila on 2017/3/20.
 */

public class MapUtils {
    public static BitmapDescriptor createBubble(Context context, int style, String title) {
        IconGenerator iconGenerator = new IconGenerator(context);
        iconGenerator.setStyle(style);
        Bitmap bitmap = iconGenerator.makeIcon(title);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        return bitmapDescriptor;
    }

    public static Marker addMarker(GoogleMap map, LatLng point, String title,
                                   BitmapDescriptor bitmapDescriptor) {
        return addMarker(map, point, title, bitmapDescriptor, null);
    }

    public static Marker addMarker(GoogleMap map, LatLng point, String title) {
        return addMarker(map, point, title, null, null);
    }

    public static Marker addMarker(GoogleMap map, LatLng point, String title, Object tag) {
        return addMarker(map, point, title, null, tag);
    }

    public static Marker addMarker(GoogleMap map, LatLng point, String title,
                                   BitmapDescriptor bitmapDescriptor, Object tag) {
        // Creates and adds marker to the map
        MarkerOptions options = new MarkerOptions()
                .position(point)
                .title(title);

        if (bitmapDescriptor != null)
            options.icon(bitmapDescriptor);

        Marker marker = map.addMarker(options);

        if (tag != null)
            marker.setTag(tag);

        return marker;
    }
}
