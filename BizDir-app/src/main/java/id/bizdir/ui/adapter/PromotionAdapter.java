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
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import id.bizdir.R;
import id.bizdir.model.Promotion;

/**
 * Created by Hendry on 01/06/2015.
 */
public class PromotionAdapter extends ArrayAdapter<Promotion> implements Filterable {
    private List<Promotion> list;
    private List<Promotion> filterList;
    private NameFilter nameFilter;
    private Activity context;
    private int layoutId;

    public PromotionAdapter(Activity context, int layoutId, List<Promotion> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
        this.context = context;
        this.list = list;
        this.filterList = list;
        getFilter();
    }

    static class ViewHolder {
        protected ImageView promoImage;
        protected ProgressBar progressBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.promoImage = (ImageView) view.findViewById(R.id.promoImage);
            viewHolder.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        final ViewHolder holder = (ViewHolder) view.getTag();
        final Promotion promotion = getItem(position);

        if (!promotion.getPicture().isEmpty()) {
            Glide.with(holder.promoImage.getContext())
                    .load(promotion.getPicture())
                    .centerCrop()
                    .crossFade()
                            //.into(holder.promoImage);
                    .into(new GlideDrawableImageViewTarget(holder.promoImage) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.promoImage.setVisibility(View.GONE);
                        }
                    });
        } else {
            holder.promoImage.setImageResource(R.drawable.advertising_image);
        }

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
    public Promotion getItem(int i) {
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
                List<Promotion> tempList = new ArrayList<>();
                for (Promotion obj : list) {
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
            filterList = (ArrayList<Promotion>) results.values;
            notifyDataSetChanged();
        }
    }
}
