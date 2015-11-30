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
import id.bizdir.model.ForumThread;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 20/04/2015.
 */
public class ForumThreadTrendingAdapter extends ArrayAdapter<ForumThread> {

    private final List<ForumThread> list;
    private final Activity context;
    private final int layoutId;

    public ForumThreadTrendingAdapter(Activity context, int layoutId, List<ForumThread> list) {
        super(context, layoutId, list);
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView title;
        protected TextView textReplyCount;
        protected TextView textViewCount;
        protected TextView textLatestDate;
        protected TextView threadGroup;
        protected ImageView imagePencil;
        protected ImageView imageEye;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.textReplyCount = (TextView) view.findViewById(R.id.textReplyCount);
            viewHolder.textViewCount = (TextView) view.findViewById(R.id.textViewCount);
            viewHolder.textLatestDate = (TextView) view.findViewById(R.id.textLatestDate);
            viewHolder.threadGroup = (TextView) view.findViewById(R.id.threadGroup);
            viewHolder.imagePencil = (ImageView) view.findViewById(R.id.imagePencil);
            viewHolder.imageEye = (ImageView) view.findViewById(R.id.imageEye);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        ForumThread obj = list.get(position);
        holder.title.setText(obj.getTitle().trim());
        holder.textReplyCount.setText(Integer.toString(obj.getReplyCount()));
        holder.textViewCount.setText(Integer.toString(obj.getViewCount()));
        holder.textLatestDate.setText(Helpers.DateToStringPublishDate(obj.getCreateDate()));
        holder.threadGroup.setText(obj.getCategoryTitle());
        Drawable iconPencil = Helpers.getIcon(FontAwesome.Icon.faw_pencil_square_o,
                15, context.getResources().getColor(R.color.white));
        holder.imagePencil.setImageDrawable(iconPencil);
        Drawable iconEye = Helpers.getIcon(FontAwesome.Icon.faw_eye,
                15, context.getResources().getColor(R.color.white));
        holder.imageEye.setImageDrawable(iconEye);
        return view;
    }
}
