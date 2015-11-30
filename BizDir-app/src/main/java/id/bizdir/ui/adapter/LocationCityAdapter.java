package id.bizdir.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.bizdir.R;
import id.bizdir.model.City;

/**
 * Created by Hendry on 20/04/2015.
 */
public class LocationCityAdapter extends ArrayAdapter<City> implements Filterable {

    private List<City> list;
    private List<City> filterList;
    private NameFilter nameFilter;
    private final Activity context;
    private final int layoutId;

    public LocationCityAdapter(Activity context, int layoutId, List<City> list) {
        super(context, layoutId, list);
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filterList = list;
        getFilter();
    }

    static class ViewHolder {
        protected TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        //City obj = list.get(position);
        final City obj = getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(obj.getName());
        return view;
    }

    @Override
    public Filter getFilter() {
        if (nameFilter == null) {
            nameFilter = new NameFilter();
        }
        return nameFilter;
    }

    @Override
    public int getCount() {
        return filterList.size();
    }

    @Override
    public City getItem(int i) {
        return filterList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class NameFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<City> tempList = new ArrayList<>();
                for (City city : list) {
                    if (city.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(city);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = list.size();
                filterResults.values = list;
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterList = (ArrayList<City>) results.values;
            notifyDataSetChanged();
        }
    }
}
