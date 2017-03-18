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
import java.util.Random;

import lin.leila.petshopinspector.adapters.CityArrayAdapter;
import lin.leila.petshopinspector.interfaces.PetShopInterface;
import lin.leila.petshopinspector.models.City;
import lin.leila.petshopinspector.models.PetShop;
import lin.leila.petshopinspector.models.PetShopQueryCondition;


/**
 * Created by Leila on 2017/3/16.
 */

public class ShopListFragment extends Fragment {

    ArrayAdapter adapterCity;
    ArrayAdapter adapterZone;
    ArrayAdapter adapterItem;
    RecyclerView recyclerView;
    Spinner spCity;
    Spinner spZone;
    Spinner spItem;


    private List<PetShop> petShops;

    private List<City> cities;

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

        petShopDb = PetshopInspectorApplication.getPetShopDB();

        petShops = new ArrayList<>();

        cities = petShopDb.getCity();


        findView();
        init();
        spCity.setSelection(0);
        spZone.setSelection(0);
        spItem.setSelection(0);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        simpleStringRecyclerViewAdapter = new SimpleStringRecyclerViewAdapter(getActivity(),
                petShops);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(simpleStringRecyclerViewAdapter);
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    private void findView() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview);
        spCity = (Spinner) getView().findViewById(R.id.spCity);
        spZone = (Spinner) getView().findViewById(R.id.spZone);
        spItem = (Spinner) getView().findViewById(R.id.spItem);
    }

    private void init() {
        adapterCity = new CityArrayAdapter(getContext(), cities);
        spCity.setAdapter(adapterCity);
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEBUG", position + "");

                PetShopQueryCondition condition = new PetShopQueryCondition();

                City city = cities.get(position);

                condition.setCity(city.getName());

                String dist = (String) spZone.getSelectedItem();

                Log.d("DEBUG", "dist =" + dist);

                condition.setService((String) spItem.getSelectedItem());

                List<PetShop> shops = petShopDb.getPetShop(condition);
                petShops.clear();
                petShops.addAll(shops);
                simpleStringRecyclerViewAdapter.notifyDataSetChanged();
                ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapterZone = ArrayAdapter.createFromResource(getContext(), R.array.filter_zone, R.layout.filter_item);
        spZone.setAdapter(adapterZone);
        adapterItem = ArrayAdapter.createFromResource(getContext(), R.array.filter_item, R.layout.filter_item);
        spItem.setAdapter(adapterItem);

        setupRecyclerView(recyclerView);
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        //        private final TypedValue mTypedValue = new TypedValue();
//        private int mBackground;
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
//
//            @Override
//            public String toString() {
//                return super.toString() + " '" + mTvShopName.getText();
//            }
        }
//
//        public String getValueAt(int position) {
//            return mValues.get(position);
//        }

        public SimpleStringRecyclerViewAdapter(Context context, List<PetShop> items) {
//            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
//            mBackground = mTypedValue.resourceId;
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
}

