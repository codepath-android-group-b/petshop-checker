package lin.leila.petshopinspector;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.io.IOException;

import lin.leila.petshopinspector.fragments.CustomerPickImageBaseDialog;
import lin.leila.petshopinspector.fragments.CustomerPickImageDialog;
import lin.leila.petshopinspector.fragments.WorkaroundMapFragment;
import lin.leila.petshopinspector.models.EmailAddress;
import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.models.PhoneBook;
import lin.leila.petshopinspector.utils.FileUtils;
import lin.leila.petshopinspector.utils.IntentUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Leila on 2017/3/16.
 */

@RuntimePermissions
public class ShopDetailActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMapLoadedCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        CustomerPickImageBaseDialog.OnCancelClickListener,
        IPickResult {

    public static final String EXTRA_NAME = "shop_name";
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 300000;  /* 60 secs */
    private long FASTEST_INTERVAL = 100000; /* 100 secs */

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private PetShop shopDetail = new PetShop();

    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appbar;
    Toolbar toolbar;
    TextView tvItem1;
    TextView tvItem2;
    TextView tvItem3;
    TextView tvAddr;
    TextView tvAssistant;
    TextView tvGrade;
    TextView tvValidDate;
    TextView tvCertNo;
    ProgressBar progressBar;

    private EmailAddress emailAddress = new EmailAddress();
    private PhoneBook phoneBook = new PhoneBook();
    EditText passwordInput;

    com.getbase.floatingactionbutton.FloatingActionButton phoneAction;
    com.getbase.floatingactionbutton.FloatingActionButton emailAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        shopDetail = intent.getParcelableExtra(EXTRA_NAME);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(shopDetail.getShopName());

        if (TextUtils.isEmpty(getResources().getString(R.string.google_maps_api_key))) {
            throw new IllegalStateException("You forgot to supply a Google Maps API key");
        }

        findView();
        init(shopDetail);

    }

    @Override
    protected void onStart() {
        super.onStart();
        connectClient();
    }

    /*
     * Called when the Activity is no longer visible.
	 */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    public void init(final PetShop shopDetail) {

        tvAddr.setText(shopDetail.getAddress());
        tvAssistant.setText(shopDetail.getAssistant());
        tvGrade.setText(shopDetail.getCertGrade());
        tvValidDate.setText(shopDetail.getCertDate());
        tvCertNo.setText(shopDetail.getCertNo());

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if ((verticalOffset * (-1)) > 400) {
                    mapFragment.getView().setVisibility(View.INVISIBLE);
                    collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
                } else {
                    mapFragment.getView().setVisibility(View.VISIBLE);
                    collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.colorBlack));
                }
            }
        });

        phoneAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneToAdmin();
            }
        });
        emailAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog();
            }
        });
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
//            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }

        ifItemExisted(shopDetail);

    }

    public void findView() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        tvItem1 = (TextView) findViewById(R.id.tvItem1);
        tvItem2 = (TextView) findViewById(R.id.tvItem2);
        tvItem3 = (TextView) findViewById(R.id.tvItem3);
        tvAddr = (TextView) findViewById(R.id.tvAddr);
        tvAssistant = (TextView) findViewById(R.id.tvAssistant);
        tvGrade = (TextView) findViewById(R.id.tvGrade);
        tvValidDate = (TextView) findViewById(R.id.tvValidDate);
        tvCertNo = (TextView) findViewById(R.id.tvCertNo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        phoneAction = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.button_phone);
        emailAction = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.button_email);
    }

    public void ifItemExisted(PetShop shopDetail) {
        final String[] items = {"買賣", "寄養", "繁殖"};
        TextView[] tvItems = {tvItem1, tvItem2, tvItem3};

        for (int i = 0; i < items.length; i++) {
            tvItems[i].setText(items[i]);
            if (shopDetail.getServices().indexOf(items[i]) >= 0) {
                tvItems[i].setBackground(getResources().getDrawable(R.drawable.round_corner_valid_item));
            } else {
                tvItems[i].setBackground(getResources().getDrawable(R.drawable.round_corner_item_invalid));
                tvItems[i].setTextColor(getResources().getColor(R.color.colorTextInvalid));
            }
        }
    }


    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar); //parent scrollview in xml, give your scrollview id value

            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            collapsingToolbarLayout.requestDisallowInterceptTouchEvent(true);
                        }
                    });
            ShopDetailActivityPermissionsDispatcher.getMyLocationWithCheck(this);
            map.setOnMapLoadedCallback(this);
        } else {
//            Snackbar.make(coordinatorLayout, "Error - Map was null!!", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapLoaded() {
        progressBar.setVisibility(View.GONE);
        LatLng shopLocation = new LatLng(shopDetail.getLatitude(), shopDetail.getLongitude());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(shopLocation, 15);
        map.animateCamera(cameraUpdate);
        MapUtils.addMarker(map, shopLocation, shopDetail.getShopName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == IntentUtils.PHONE_DIALOG_RESULT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneToAdmin();
            } else {
                return;
            }
        } else {
            ShopDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        }
    }


    @SuppressWarnings("all")
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void getMyLocation() {
        if (map != null) {
            // Now that map has loaded, let's get our location!
            map.setMyLocationEnabled(true);
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            connectClient();
        }
    }

    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                }

        }
    }


    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
//             Snackbar.make(coordinatorLayout,
//                     "GPS定位完成", Snackbar.LENGTH_LONG).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            map.animateCamera(cameraUpdate);
        } else {
//            Log.d("DEBUG-GPS", "Current location was null, enable GPS on emulator!");
//            Snackbar.make(coordinatorLayout,
//                    "無GPS定位", Snackbar.LENGTH_LONG).show();
        }
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d("DEBUG", msg);
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Snackbar.make(coordinatorLayout, "Disconnected. Please re-connect.", Snackbar.LENGTH_LONG).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Snackbar.make(coordinatorLayout, "Network lost. Please re-connect.", Snackbar.LENGTH_LONG).show();
        }
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Snackbar.make(coordinatorLayout,
                    "地圖服務存取使用量已達上限，無法使用", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancelClick() {
        EmailDialog(null);
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    public void EmailDialog(final PickResult pickResult) {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(ShopDetailActivity.this)
                .customView(R.layout.email_template_fragment, true)
                .title("檢舉 " + shopDetail.getShopName())
                .positiveText("Email")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    // positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View positive = dialog.getActionButton(DialogAction.POSITIVE);
                        passwordInput = (EditText) dialog.getCustomView().findViewById(R.id.password);
                        CheckBox checkbox = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword);
                        CheckBox checkbox2 = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword2);
                        CheckBox checkbox3 = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword3);
                        CheckBox checkbox4 = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword4);
                        CheckBox checkbox5 = (CheckBox) dialog.getCustomView().findViewById(R.id.addBCC);

                        ImageView imageView = (ImageView) dialog.getCustomView().findViewById(R.id.imageView);


                        String emailtitle = "檢舉 " + shopDetail.getShopName();
                        String emailsubject = new String();
                        //save input
                        emailsubject = "敬啟者，\r\n" + "本人於行經" + shopDetail.getAddress() +
                                "，發現" + shopDetail.getShopName() + "有違法情事：\r\n"
                                + passwordInput.getText().toString() + "\r\n";
                        if (checkbox.isChecked()) {
                            emailsubject = emailsubject + checkbox.getText().toString() + "\r\n";
                        }
                        if (checkbox2.isChecked()) {
                            emailsubject = emailsubject + checkbox2.getText().toString() + "\r\n";
                        }
                        if (checkbox3.isChecked()) {
                            emailsubject = emailsubject + checkbox3.getText().toString() + "\r\n";
                        }
                        if (checkbox4.isChecked()) {
                            emailsubject = emailsubject + checkbox4.getText().toString() + "\r\n";
                        }

                        emailsubject = emailsubject + "\r\n該店寵物登記資料如下：\r\n" +
                                "日期：" + shopDetail.getCertDate() + "\r\n" +
                                "等級：" + shopDetail.getCertGrade() + "\r\n" +
                                "證號：" + shopDetail.getCertNo() + "\r\n" +
                                "登記項目" + shopDetail.getServices() + "\r\n" + "\r\n盼貴單位能妥善處理，並公布或回覆，謝謝。";

                        String emailaddress = emailAddress.getEmailAddress(shopDetail.getCity());
                        if (emailaddress == null) {
                            emailaddress = "test@gmail.com";
                        }

                        String uriText = "mailto:" + emailaddress + "," + "yichuchen.tiawan@gmail.com";
                        uriText = uriText + "?subject=" + Uri.encode(emailtitle) +
                                "&body=" + Uri.encode(emailsubject);

                        Uri uri = Uri.parse(uriText);

                        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                        sendIntent.setData(uri);

                        if (checkbox5.isChecked()) {
                            sendIntent.putExtra(Intent.EXTRA_CC, new String[] {"petshopiapp@gmail.com"});
                        }

                        if (pickResult != null) {
                            Uri imageUri = null;
                            if (!FileUtils.isExternalFile(pickResult.getPath())) {
                                try {
                                    File file = FileUtils.exportFile(new File(pickResult.getPath()));
                                    imageUri = Uri.fromFile(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                imageUri = pickResult.getUri();
                            }
                            sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                        }

                        startActivity(Intent.createChooser(sendIntent, "Please attach image/video in email"));
                    }
                });

        MaterialDialog dialog = builder.show();

        if (pickResult != null) {
            ImageView imageView = (ImageView) dialog.getCustomView().findViewById(R.id.imageView);
            imageView.setImageBitmap(pickResult.getBitmap());
        }
    }

    public void callPhoneToAdmin() {
        String phoneNumber = phoneBook.getPhoneNumber(shopDetail.getCity());
        Intent phoneIntent = IntentUtils.createCallPhoneIntent(this,phoneNumber);

        if(phoneIntent != null)
            startActivity(phoneIntent);
    }

    public void photoDialog() {
        CustomerPickImageDialog dialog = CustomerPickImageDialog.build(new PickSetup());

        dialog.setOnCancelClickListener(this);

        dialog.show(this);
    }

    @Override
    public void onPickResult(final PickResult r) {
        if (r.getError() == null) {
            EmailDialog(r);
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}
