package id.bizdir.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.bizdir.R;
import id.bizdir.model.NewsKadin;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 20/04/2015.
 */
public class NewsKadinListViewAdapter extends ArrayAdapter<NewsKadin> {

    private final List<NewsKadin> list;
    private final Activity context;
    private final int layoutId;

    public NewsKadinListViewAdapter(Activity context, int layoutId, List<NewsKadin> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected ImageView imageNews;
        protected TextView textTitle;
        protected TextView textTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageNews = (ImageView) view.findViewById(R.id.imageNews);
            viewHolder.textTitle = (TextView) view.findViewById(R.id.textTitle);
            viewHolder.textTime = (TextView) view.findViewById(R.id.textTime);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        final NewsKadin newsKadin = list.get(position);
        if (!newsKadin.getThumb().isEmpty()) {
            Glide.with(holder.imageNews.getContext())
                    .load(newsKadin.getThumb())
                    .centerCrop()
                    .crossFade()
                    .into(holder.imageNews);
        } else {
            holder.imageNews.setImageResource(R.drawable.default_member);
        }
        holder.textTitle.setText(newsKadin.getTitle());
        holder.textTime.setText(Helpers.DateToStringPublishDate(newsKadin.getCreateDate()));
        return view;
    }
}
