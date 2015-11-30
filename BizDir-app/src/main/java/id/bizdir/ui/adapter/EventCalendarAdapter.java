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
import java.util.Date;
import java.util.List;

import id.bizdir.R;
import id.bizdir.model.Event;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 20/04/2015.
 */
public class EventCalendarAdapter extends ArrayAdapter<Event> implements Filterable {

    private final List<Event> list;
    private final Activity context;
    private final int layoutId;
    private List<Event> filterList;
    private NameFilter nameFilter;

    public EventCalendarAdapter(Activity context, int layoutId, List<Event> list) {
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
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        final Event obj = getItem(position);
        Date eventDate = obj.getStartDate();
        holder.date.setText(Helpers.DateToStringDay(eventDate));
        holder.dateMonthYear.setText(Helpers.DateToStringMonthYear(eventDate));
        holder.title.setText(obj.getTitle().trim());
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
    public Event getItem(int i) {
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
                List<Event> tempList = new ArrayList<>();
                for (Event obj : list) {
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
            filterList = (ArrayList<Event>) results.values;
            notifyDataSetChanged();
        }
    }
}
