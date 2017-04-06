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
                                   String snippet,
                                   BitmapDescriptor bitmapDescriptor) {
        return addMarker(map, point, title, snippet, bitmapDescriptor, null);
    }

    public static Marker addMarker(GoogleMap map, LatLng point, String title,
                                   String snippet,
                                   BitmapDescriptor bitmapDescriptor, Object tag) {
        // Creates and adds marker to the map
        MarkerOptions options = new MarkerOptions()
                .position(point)
                .title(title)
                .snippet(snippet)
                .icon(bitmapDescriptor);

        Marker marker = map.addMarker(options);

        if (tag != null)
            marker.setTag(tag);

        return marker;
    }
}
