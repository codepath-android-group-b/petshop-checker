package lin.leila.petshopinspector;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lin.leila.petshopinspector.fragments.CustomerPickImageBaseDialog;
import lin.leila.petshopinspector.fragments.CustomerPickImageDialog;
import lin.leila.petshopinspector.models.EmailAddress;
import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.models.PhoneBook;
import lin.leila.petshopinspector.utils.FileUtils;
import lin.leila.petshopinspector.utils.IntentUtils;
import lin.leila.petshopinspector.utils.PetShopUtils;

public class MainActivity extends AppCompatActivity implements MapFragment.OnMarkerClickListener
        , ActivityCompat.OnRequestPermissionsResultCallback
        , CustomerPickImageBaseDialog.OnCancelClickListener, IPickResult {

    public static final String ALL_CITY = "全台";
    private DrawerLayout mDrawerLayout;
    com.getbase.floatingactionbutton.FloatingActionButton phoneAction;
    com.getbase.floatingactionbutton.FloatingActionButton emailAction;

    private String phoneSelectedCity;

    private ViewPager viewPager;

    private Adapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        findView();
        emailAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog();
            }
        });

        phoneAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if (!TextUtils.isEmpty(query)) {

                    ShopListFragment shopListFragment = getShopListFragment();

                    if (shopListFragment != null) {
                        shopListFragment.filterShopByShopName(query);
                        viewPager.setCurrentItem(0, true);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private ShopListFragment getShopListFragment() {

        Fragment fragment = adapter.getItem(0);

        if (fragment != null && fragment instanceof ShopListFragment) {
            return (ShopListFragment) fragment;
        } else {
            return null;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ShopListFragment(), "店家清單");
        adapter.addFragment(new MapFragment(), "從地圖上瀏覽店家");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        Menu item = navigationView.getMenu();
        String strVersion = getResources().getString(R.string.title_data_version)
                + PetShopUtils.getPetShopDataVerion();
        item.findItem(R.id.nav_data_version).setTitle(strVersion);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_data_version:
//                                Snackbar.make(getCurrentFocus(), "Version : " + PetShopUtils.getPetShopDataVerion(), Snackbar.LENGTH_LONG).show();
//                                Toast.makeText(MainActivity.this, "Version : " + PetShopUtils.getPetShopDataVerion(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_home:
                                viewPager.setCurrentItem(0);
//                                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_messages:
                                Intent msgIntent = new Intent(context, PrivacyActivity.class);
                                startActivity(msgIntent);
//                                Toast.makeText(MainActivity.this, "Privacy", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_contact:
                                Uri uri = Uri.parse("mailto:petshopiapp@gmail.com");
                                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                                startActivity(it);
//                                Toast.makeText(MainActivity.this, "Contact", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void callMarkerShop(Object o) {
        PetShop petShop = (PetShop) o;
        Intent intent = new Intent(this, ShopDetailActivity.class);
        intent.putExtra(ShopDetailActivity.EXTRA_NAME, petShop);

        startActivity(intent);
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {
            emailDialogMain(pickResult);
        } else {
            Toast.makeText(this, pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    public void findView() {
        phoneAction = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.button_phone_main);
        emailAction = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.button_email_main);
    }

    public void emailDialogMain(final PickResult pickResult) {

        MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                .customView(R.layout.email_selection_fragment, true)
                .title("檢舉 ")
                .positiveText("Email")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    // positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        CheckBox cbNoRegistration = (CheckBox) dialog.getCustomView().findViewById(R.id.cbNoRegistration);
                        CheckBox cbWrongAddress = (CheckBox) dialog.getCustomView().findViewById(R.id.cbWrongAddress);
                        EditText etPetShopName = (EditText) dialog.getCustomView().findViewById(R.id.etPetShopName);
                        EditText etPetShopAddress = (EditText) dialog.getCustomView().findViewById(R.id.etPetShopAddress);

                        EditText etOtherReason = (EditText) dialog.getCustomView().findViewById(R.id.etOtherReason);

                        Spinner emailSpCity = (Spinner) dialog.getCustomView().findViewById(R.id.emailSpCity);
                        String city = (String) emailSpCity.getSelectedItem();

                        CheckBox cbAskCCForUs = (CheckBox) dialog.getCustomView().findViewById(R.id.cbAskCCForUs);

                        String emailAddress;

                        EmailAddress emailUtils = new EmailAddress();

                        if (TextUtils.isEmpty(city)) {
                            emailAddress = emailUtils.getEmailAddress(ALL_CITY);
                        } else {
                            emailAddress = emailUtils.getEmailAddress(city);
                        }

                        String emailSubject = genEmailTitle(etPetShopName);
                        String emailBody = genEmailBody(cbNoRegistration, cbWrongAddress, etPetShopName, etPetShopAddress, etOtherReason);

                        String uriText = "mailto:" + emailAddress +
                                "?subject=" + Uri.encode(emailSubject) +
                                "&body=" + Uri.encode(emailBody);

                        Uri uri = Uri.parse(uriText);

                        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                        sendIntent.setData(uri);


                        if (cbAskCCForUs.isChecked()) {
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
                }).show();

        if (pickResult != null) {
            ImageView imageView = (ImageView) dialog.getCustomView().findViewById(R.id.ivPhoto);
            imageView.setImageBitmap(pickResult.getBitmap());
        }
    }

    private String genEmailBody(CheckBox cbNoRegistration, CheckBox cbWrongAddress, EditText etPetShopName, EditText etPetShopAddress,EditText etOtherReason) {
        StringBuilder body = new StringBuilder();
        body.append("敬啟者，\r\n");
        body.append("寵物店名:" + etPetShopName.getText() + "\r\n");
        body.append("地址:" + etPetShopAddress.getText() + "\r\n");
        body.append("檢舉項目 : \r\n");

        if(cbNoRegistration.isChecked()) {
            body.append("     " + cbNoRegistration.getText() +"\r\n");
        }

        if(cbWrongAddress.isChecked()) {
            body.append("     " + cbWrongAddress.getText() +"\r\n");
        }

        if(!TextUtils.isEmpty(etOtherReason.getText())) {
            body.append("     " + etOtherReason.getText() +"\r\n");
        }

        body.append("盼貴單位能妥善處理，並公布或回覆，謝謝。");

        return body.toString();
    }

    private String genEmailTitle(EditText etPetShopName) {
        StringBuilder title = new StringBuilder("檢舉 ");
        if (!TextUtils.isEmpty(etPetShopName.getText())) {
            title.append(":");
            title.append(etPetShopName.getText());
        }
        return title.toString();
    }

    public void photoDialog() {

        CustomerPickImageDialog dialog = CustomerPickImageDialog.build(new PickSetup().setCancelText("下一步"));

        dialog.setOnCancelClickListener(this);

        dialog.show(this);
    }

    @Override
    public void onCancelClick() {
        emailDialogMain(null);
    }


    public void phoneDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                .customView(R.layout.phone_selection_fragment, true)
                .title("檢舉 ")
                .positiveText("撥打檢舉專線")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    // positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Spinner spCity = (Spinner) dialog.getCustomView().findViewById(R.id.phoneSpCity);
                        phoneSelectedCity = (String) spCity.getSelectedItem();
                        callPhoneToAdmin();
                    }
                }).show();
    }

    public void callPhoneToAdmin() {
        String phoneNumber;
        PhoneBook phoneBook = new PhoneBook();
        if (TextUtils.isEmpty(phoneSelectedCity)) {
            phoneNumber = phoneBook.getPhoneNumber(ALL_CITY);
        } else {
            phoneNumber = phoneBook.getPhoneNumber(phoneSelectedCity);
        }
        Intent phoneIntent = IntentUtils.createCallPhoneIntent(this, phoneNumber);
        if (phoneIntent != null)
            startActivity(phoneIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IntentUtils.PHONE_DIALOG_RESULT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneToAdmin();
            }
        } else {
            return;
        }
    }
}