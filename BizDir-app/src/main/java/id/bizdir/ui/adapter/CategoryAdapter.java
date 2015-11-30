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
import id.bizdir.model.AnggotaCategory;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 20/04/2015.
 */
public class CategoryAdapter extends ArrayAdapter<AnggotaCategory> {

    private final List<AnggotaCategory> list;
    private final Activity context;
    private final int layoutId;

    public CategoryAdapter(Activity context, int layoutId, List<AnggotaCategory> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
        this.context = context;
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
        AnggotaCategory obj = list.get(position);
        String imageId = obj.getIconId();
        if (imageId != null) {
            if (!imageId.isEmpty()) {
                //Drawable icon = Helpers.getListViewIcon(imageId, false);
                Drawable icon = Helpers.getListViewIcon(imageId, false);
                holder.icon.setImageDrawable(icon);
            } else {
                Drawable iconDefault = Helpers.getListViewIcon(FontAwesome.Icon.faw_comments_o, false);
                holder.icon.setImageDrawable(iconDefault);
            }
        } else {
            Drawable iconDefault = Helpers.getListViewIcon(FontAwesome.Icon.faw_comments_o, false);
            holder.icon.setImageDrawable(iconDefault);
        }
        holder.name.setText(obj.getTitle());
        return view;
    }
}
