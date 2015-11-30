package id.bizdir.ui.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.bizdir.R;
import id.bizdir.model.AnggotaGallery;

/**
 * Created by Hendry on 20/04/2015.
 */
public class GalleryAdapter extends ArrayAdapter<AnggotaGallery> {

    private final List<AnggotaGallery> list;
    private final Activity context;
    private final int layoutId;

    public GalleryAdapter(Activity context, int layoutId, List<AnggotaGallery> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected ImageView icon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        AnggotaGallery obj = list.get(position);
        String imageUrlThumb = obj.getThumb();
        if (!TextUtils.isEmpty(imageUrlThumb)) {
            Glide.with(holder.icon.getContext())
                    .load(imageUrlThumb)
                    .centerCrop()
                    .crossFade()
                    .into(holder.icon);
        } else {
            holder.icon.setImageResource(R.drawable.default_member);
        }
        return view;
    }
}
