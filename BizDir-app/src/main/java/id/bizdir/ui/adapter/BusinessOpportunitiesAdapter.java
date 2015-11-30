package id.bizdir.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.util.ArrayList;
import java.util.List;

import id.bizdir.R;
import id.bizdir.model.Opportunity;
import id.bizdir.util.Helpers;

import android.widget.Filter;

/**
 * Created by Hendry on 20/04/2015.
 */
public class BusinessOpportunitiesAdapter extends ArrayAdapter<Opportunity> implements Filterable {

    private final List<Opportunity> list;
    private final Activity context;
    private final int layoutId;
    private List<Opportunity> filterList;
    private NameFilter nameFilter;

    public BusinessOpportunitiesAdapter(Activity context, int layoutId, List<Opportunity> list) {
        super(context, layoutId, list);
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filterList = list;
        getFilter();
    }

    static class ViewHolder {
        protected TextView date;
        protected TextView dateMonthYear;
        protected TextView title;
        protected TextView textEmailCount;
        protected TextView textViewCount;
        protected ImageView imageEnvelope;
        protected ImageView imageEye;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.dateMonthYear = (TextView) view.findViewById(R.id.dateMonthYear);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.textEmailCount = (TextView) view.findViewById(R.id.textEmailCount);
            viewHolder.textViewCount = (TextView) view.findViewById(R.id.textViewCount);
            viewHolder.imageEnvelope = (ImageView) view.findViewById(R.id.imageEnvelope);
            viewHolder.imageEye = (ImageView) view.findViewById(R.id.imageEye);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        Opportunity opportunity = getItem(position);
        holder.date.setText(Helpers.DateToStringDay(opportunity.getStartDate()));
        holder.dateMonthYear.setText(Helpers.DateToStringMonthYear(opportunity.getStartDate()));
        holder.title.setText(opportunity.getTitle().trim());
        holder.textEmailCount.setText(Integer.toString(opportunity.getEmailCount()));
        holder.textViewCount.setText(Integer.toString(opportunity.getViewCount()));
        Drawable iconEnvelope = Helpers.getIcon(FontAwesome.Icon.faw_envelope,
                15, context.getResources().getColor(R.color.white));
        holder.imageEnvelope.setImageDrawable(iconEnvelope);
        Drawable iconEye = Helpers.getIcon(FontAwesome.Icon.faw_eye,
                15, context.getResources().getColor(R.color.white));
        holder.imageEye.setImageDrawable(iconEye);
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
    public Opportunity getItem(int i) {
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
                List<Opportunity> tempList = new ArrayList<>();
                for (Opportunity obj : list) {
                    if (obj.getTitle().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            obj.getDescription().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(obj);
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
            filterList = (ArrayList<Opportunity>) results.values;
            notifyDataSetChanged();
        }
    }
}
