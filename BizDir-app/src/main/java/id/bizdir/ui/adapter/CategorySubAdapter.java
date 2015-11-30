package id.bizdir.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.util.List;

import id.bizdir.R;
import id.bizdir.model.AnggotaSubCategory;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 20/04/2015.
 */
public class CategorySubAdapter extends ArrayAdapter<AnggotaSubCategory> {

    private final List<AnggotaSubCategory> list;
    private final Activity context;
    private final int layoutId;

    public CategorySubAdapter(Activity context, int layoutId, List<AnggotaSubCategory> list) {
        super(context, layoutId, list);
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
    }

    static class ViewHolder {
        protected ImageView icon;
        protected TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        Drawable icon = Helpers.getListViewIcon(FontAwesome.Icon.faw_angle_right, false);
        holder.icon.setImageDrawable(icon);
        holder.name.setText(list.get(position).getTitle());
        return view;
    }
}
