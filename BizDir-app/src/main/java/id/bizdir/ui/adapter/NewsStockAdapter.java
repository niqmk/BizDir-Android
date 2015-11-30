package id.bizdir.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.util.ArrayList;
import java.util.List;

import id.bizdir.R;
import id.bizdir.model.NewsStock;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 20/04/2015.
 */
public class NewsStockAdapter extends ArrayAdapter<NewsStock> implements Filterable {

    private final List<NewsStock> list;
    private List<NewsStock> filterList;
    private NameFilter nameFilter;
    private final Activity context;
    private final int layoutId;

    public NewsStockAdapter(Activity context, int layoutId, List<NewsStock> list) {
        super(context, layoutId, list);
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        this.filterList = list;
        getFilter();
    }

    static class ViewHolder {
        protected TextView textStockCode;
        protected TextView textLast;
        protected TextView textCompanyName;
        protected TextView textChange;
        protected View frameChange;
        protected ImageView iconArrow;
    }

    @Override
    public Filter getFilter() {
        if (nameFilter == null) {
            nameFilter = new NameFilter();
        }
        return nameFilter;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return filterList.size();
    }

    @Override
    public NewsStock getItem(int i) {
        return filterList.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.textStockCode = (TextView) view.findViewById(R.id.textStockCode);
            viewHolder.textLast = (TextView) view.findViewById(R.id.textLast);
            viewHolder.textCompanyName = (TextView) view.findViewById(R.id.textCompanyName);
            viewHolder.textChange = (TextView) view.findViewById(R.id.textChange);
            viewHolder.frameChange = view.findViewById(R.id.frameChange);
            viewHolder.iconArrow = (ImageView) view.findViewById(R.id.iconArrow);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        //NewsStock obj = list.get(position);
        final NewsStock obj = getItem(position);
        holder.textStockCode.setText(obj.getStock_code());
        holder.textLast.setText(obj.getLast());
        holder.textCompanyName.setText(obj.getCompany());
        String change = obj.getChange();
        String percent = obj.getPercent_change();
        if (change.contains("(")) {
            change = change.replace("(", "-");
            change = change.replace(")", "");
            holder.frameChange.setBackgroundResource(R.drawable.bg_trans_red);
        } else {
            change = "+" + change;
            holder.frameChange.setBackgroundResource(R.drawable.bg_trans_green);
        }
        percent = "(" + percent + "%)";
        String precentChange = change + " " + percent;
        holder.textChange.setText(precentChange);
        Drawable icon = Helpers.getListViewIcon(FontAwesome.Icon.faw_angle_right, false);
        holder.iconArrow.setImageDrawable(icon);
        return view;
    }

    private class NameFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<NewsStock> tempList = new ArrayList<>();
                for (NewsStock obj : list) {
                    if (obj.getStock_code().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            filterList = (ArrayList<NewsStock>) results.values;
            notifyDataSetChanged();
        }
    }
}
