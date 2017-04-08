package lin.leila.petshopinspector;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.utils.PetShopUtils;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity implements MapFragment.OnMarkerClickListener {

    private DrawerLayout mDrawerLayout;
    //FloatingActionButton fab;
    com.getbase.floatingactionbutton.FloatingActionButton phoneAction;
    com.getbase.floatingactionbutton.FloatingActionButton photoAction;
    com.getbase.floatingactionbutton.FloatingActionButton emailAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        findView();
        emailAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailDialogMain();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ShopListFragment(), "Shop List");
        adapter.addFragment(new MapFragment(), "MAP");
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

        item.addSubMenu("sdf");

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_data_version:
                                makeText(MainActivity.this, "Version : " + PetShopUtils.getPetShopDataVerion(), Toast.LENGTH_SHORT).show();
                            case R.id.nav_home:
                                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_messages:
                                Toast.makeText(MainActivity.this, "Privacy", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_contact:
                                Toast.makeText(MainActivity.this, "Contact", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        if (R.id.nav_data_version == menuItem.getItemId()) {
                            makeText(MainActivity.this, "Version : " + PetShopUtils.getPetShopDataVerion(), Toast.LENGTH_SHORT).show();
                        }

                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void callMarkerShop(Object o) {
        PetShop petShop =  (PetShop) o;
        Intent intent = new Intent(this, ShopDetailActivity.class);
        intent.putExtra(ShopDetailActivity.EXTRA_NAME, petShop);

        startActivity(intent);
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
    public void findView(){
        phoneAction = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.button_phone_main);
        photoAction = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.button_photo_main);
        emailAction = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.button_email_main);
    }

    public void EmailDialogMain() {
        MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                .customView(R.layout.email_selection_fragment, true)
                .title("檢舉 ")
                .positiveText("Email")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    // positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View positive = dialog.getActionButton(DialogAction.POSITIVE);
                        //passwordInput = (EditText) dialog.getCustomView().findViewById(R.id.password);
                        CheckBox checkbox = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword);
                        CheckBox checkbox2 = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword2);
                        CheckBox checkbox3 = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword3);
                        CheckBox checkbox4 = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword4);

                        String emailtitle = "檢舉 ";
                        String emailsubject = new String();
                        //save input
                        emailsubject = "敬啟者，\r\n";

                        emailsubject = emailsubject + "\r\n該店寵物登記資料如下：\r\n" +
                                "日期："+ "\r\n盼貴單位能妥善處理，並公布或回覆，謝謝。";

                        String emailaddress = "test@gmail.com";
                        /*if (emailaddress == null) {
                            emailaddress = "test@gmail.com";
                        }*/

                        String uriText = "mailto:" + emailaddress +
                                "?subject=" + Uri.encode(emailtitle) +
                                "&body=" + Uri.encode(emailsubject);

                        Uri uri = Uri.parse(uriText);

                        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                        /* if(photoUri!=null){
                            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(photoUri));}*/
                        //sendIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
                        sendIntent.setData(uri);


                        // it.setType("image/jpeg");
                        //sendIntent.putExtra(android.content.Intent.EXTRA_STREAM, photoUri);
                        //sendIntent.setData(uri);

                        startActivity(Intent.createChooser(sendIntent, "Please attach image/video in email"));

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                })
                .show();

    }
}
