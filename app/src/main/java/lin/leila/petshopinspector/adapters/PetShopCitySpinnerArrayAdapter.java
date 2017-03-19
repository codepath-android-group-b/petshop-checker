package lin.leila.petshopinspector.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lin.leila.petshopinspector.R;
import lin.leila.petshopinspector.models.City;

/**
 * Created by javiosyc on 2017/2/21.
 */

public class PetShopCitySpinnerArrayAdapter extends ArrayAdapter<City> {
    private Context context;
    private List<City> petShopSpinners;
    private TextView tvCity;

    public PetShopCitySpinnerArrayAdapter(Context context, List<City> petShopSpinners) {
        super(context, android.R.layout.simple_list_item_1, petShopSpinners);
        this.context = context;
        this.petShopSpinners = petShopSpinners;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getDefaultView(position, convertView, parent, R.layout.spinner_city_item);
    }

    @Override
    public int getCount() {
        return petShopSpinners.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getDefaultView(position, convertView, parent, R.layout.spinner_dropdown_city_item);
    }

    private View getDefaultView(int position, View convertView, ViewGroup parent, int resourceId) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resourceId, parent, false);
            tvCity = (TextView) convertView.findViewById(R.id.tvCity);
        }

        petShopSpinners.get(position);

        tvCity.setText(petShopSpinners.get(position).getName());
        return convertView;
    }
}
