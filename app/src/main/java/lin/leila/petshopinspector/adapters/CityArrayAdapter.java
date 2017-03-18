package lin.leila.petshopinspector.adapters;

import android.content.Context;
import android.graphics.Color;
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

public class CityArrayAdapter extends ArrayAdapter<City> {
    private Context context;

    private List<City> cities;

    private TextView tvCity;


    public CityArrayAdapter(Context context, List<City> cities) {
        super(context, android.R.layout.simple_list_item_1, cities);
        this.context = context;
        this.cities = cities;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.spinner_city_item, parent, false);
            tvCity = (TextView) convertView.findViewById(R.id.tvCity);
        }
        //find the image view
        tvCity.setText(cities.get(position).getName());
        return convertView;
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        textView.setTextColor(Color.BLACK);

        textView.setHeight(transferPixelsFromDp(30));
        textView.setText(cities.get(position).getName());
        return textView;
    }

    
    private int transferPixelsFromDp(int dps) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return  (int) (dps * scale + 0.5f);
    }
}
