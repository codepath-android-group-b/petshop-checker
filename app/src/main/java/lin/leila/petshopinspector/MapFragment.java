package lin.leila.petshopinspector;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import lin.leila.petshopinspector.interfaces.PetShopInterface;
import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.service.LoadingShopTask;

public class MapFragment extends Fragment implements GoogleMap.OnMapLoadedCallback {

    private SupportMapFragment mapFragment;

    private GoogleMap map;

    private PetShopInterface petShopDb;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findView();
        init();
    }


    private void init() {
        petShopDb = PetshopInspectorApplication.getPetShopDB();
    }

    private void findView() {
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentByTag("test");

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {

        }
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            map.setOnMapLoadedCallback(this);
        } else {
            // Snackbar.make(coordinatorLayout, "Error - Map was null!!", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapLoaded() {
        List<PetShop> result = petShopDb.getPetShopOrderByDistance(25.0049394, 121.5427091, 100);

        LoadingShopTask task = new LoadingShopTask();
        task.execute(getActivity(), result, map);
    }
}
