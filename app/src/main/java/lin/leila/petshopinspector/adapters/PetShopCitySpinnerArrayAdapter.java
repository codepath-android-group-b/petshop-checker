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

    private static class ViewHolder {
        TextView tvFilterOpt;
    }

    public PetShopCitySpinnerArrayAdapter(Context context, List<City> petShopSpinners) {
        super(context, android.R.layout.simple_list_item_1, petShopSpinners);
        this.context = context;
        this.petShopSpinners = petShopSpinners;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getDefaultView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return petShopSpinners.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getDefaultView(position, convertView, parent);
    }

    private View getDefaultView(int position, View convertView, ViewGroup parent) {
        City city = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.filter_item, parent, false);
            viewHolder.tvFilterOpt = (TextView) convertView.findViewById(R.id.tvFilterOpt);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.tvFilterOpt.setText(city.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}
