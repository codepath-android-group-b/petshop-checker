package lin.leila.petshopinspector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lin.leila.petshopinspector.adapters.PetShopCitySpinnerArrayAdapter;
import lin.leila.petshopinspector.adapters.PetShopDistSpinnerArrayAdapter;
import lin.leila.petshopinspector.interfaces.PetShopInterface;
import lin.leila.petshopinspector.models.City;
import lin.leila.petshopinspector.models.District;
import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.models.PetShopQueryCondition;


/**
 * Created by Leila on 2017/3/16.
 */

public class ShopListFragment extends Fragment {

    ArrayAdapter adapterCity;
    ArrayAdapter adapterDist;
    ArrayAdapter adapterItem;
    RecyclerView recyclerView;
    Spinner spCity;
    Spinner spZone;
    Spinner spItem;


    private List<PetShop> petShops;

    private List<City> cities;

    private List<District> districts;

    private PetShopInterface petShopDb;

    private SimpleStringRecyclerViewAdapter simpleStringRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        RecyclerView rv = (RecyclerView) inflater.inflate(
//                R.layout.fragment_shop_list, container, false);

        return inflater.inflate(R.layout.fragment_shop_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findView();
        init();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        simpleStringRecyclerViewAdapter = new SimpleStringRecyclerViewAdapter(getActivity(),
                petShops);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(simpleStringRecyclerViewAdapter);
    }

    private void findView() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview);
        spCity = (Spinner) getView().findViewById(R.id.spCity);
        spZone = (Spinner) getView().findViewById(R.id.spZone);
        spItem = (Spinner) getView().findViewById(R.id.spItem);
    }

    private void init() {
        petShopDb = PetshopInspectorApplication.getPetShopDB();
        petShops = new ArrayList<>();

        initSpinnerData();

        adapterCity = new PetShopCitySpinnerArrayAdapter(getContext(), cities);
        spCity.setAdapter(adapterCity);

        adapterDist = new PetShopDistSpinnerArrayAdapter(getContext(), districts);
        spZone.setAdapter(adapterDist);


        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEBUG", "spCity.onItemSelected");
                City city = cities.get(position);

                changeDistOptions(city);

                if (spZone.getSelectedItemPosition() == 0) {
                    District dist = districts.get(position);
                    String service = (String) spItem.getSelectedItem();

                    filterShopByCondition(city, dist, service);
                } else {
                    spZone.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEBUG", "spZone.onItemSelected");
                City city = (City) spCity.getSelectedItem();

                District dist = districts.get(position);

                String service = (String) spItem.getSelectedItem();

                filterShopByCondition(city, dist, service);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterItem = ArrayAdapter.createFromResource(getContext(), R.array.filter_item, R.layout.filter_item);
        spItem.setAdapter(adapterItem);

        spItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEBUG", "spItem.onItemSelected");
                City city = (City) spCity.getSelectedItem();
                District dist = (District) spZone.getSelectedItem();
                String service = (String) spItem.getSelectedItem();

                filterShopByCondition(city, dist, service);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        setupRecyclerView(recyclerView);
    }

    private void filterShopByCondition(City city, District dist, String service) {
        PetShopQueryCondition condition = new PetShopQueryCondition();
        condition.setCity(city);
        condition.setDistrict(dist);
        condition.setService(service);

        condition.setService((String) spItem.getSelectedItem());
        List<PetShop> shops = petShopDb.getPetShop(condition);
        petShops.clear();
        petShops.addAll(shops);
        simpleStringRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void initSpinnerData() {
        cities = new ArrayList<>();
        List<City> allCities = petShopDb.getCity();
        cities.add(getDefaultCityOption(allCities));
        cities.addAll(allCities);

        districts = new ArrayList<>();
        districts.add(new District(getResources().getString(R.string.selected_all_dist), 0));
    }

    private City getDefaultCityOption(List<City> allCities) {
        City defaultCity = new City();
        defaultCity.setName(getResources().getString(R.string.selected_all_cities));

        List<District> dist = new ArrayList<>();
        for (City city : cities) {
            dist.addAll(city.getDistrictList());
        }
        defaultCity.setDistrictList(dist);
        return defaultCity;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private List<PetShop> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public PetShop mBound;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTvShopName;
            public final TextView mTvShopAddr;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTvShopName = (TextView) view.findViewById(R.id.tvShopName);
                mTvShopAddr = (TextView) view.findViewById(R.id.tvShopAddr);
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<PetShop> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
//            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBound = mValues.get(position);

            holder.mTvShopName.setText(mValues.get(position).getShopName());
            holder.mTvShopAddr.setText(mValues.get(position).getAddress());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ShopDetailActivity.class);
                    intent.putExtra(ShopDetailActivity.EXTRA_NAME, holder.mBound);


                    context.startActivity(intent);
                }
            });


//            Glide.with(holder.mImageView.getContext())
//                    .load(R.drawable.ic_warning)
//                    .centerCrop()
//                    .into(holder.mImageView);


//            Glide.with(holder.mImageView.getContext())
//                    .load(Shops.getRandomShopDrawable())
//                    .fitCenter()
//                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

    private void changeDistOptions(City city) {
        resetDistOption();
        if (city.getId() != 0) {
            districts.addAll(city.getDistrictList());
        }
        adapterDist.notifyDataSetChanged();
    }

    private void resetDistOption() {
        if (districts.size() > 1) {
            districts.subList(1, districts.size()).clear();
        }
    }

    public void filterShopByShopName(String shopName) {
        List<PetShop> shops = petShopDb.getPetShopByName(shopName);
        petShops.clear();
        petShops.addAll(shops);
        simpleStringRecyclerViewAdapter.notifyDataSetChanged();
    }
}

