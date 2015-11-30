package id.bizdir.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.bizdir.R;
import id.bizdir.model.ForumPost;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 20/04/2015.
 */
public class ForumPostAdapter extends ArrayAdapter<ForumPost> {

    private final List<ForumPost> list;
    private final Activity context;
    private final int layoutId;

    public ForumPostAdapter(Activity context, int layoutId, List<ForumPost> list) {
        super(context, layoutId, list);
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
    }

    static class ViewHolder {
        protected ImageView iconProfile;
        protected TextView nameProfile;
        protected TextView postDate;
        protected TextView postNumber;
        protected TextView textPost;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.iconProfile = (ImageView) view.findViewById(R.id.iconProfile);
            viewHolder.nameProfile = (TextView) view.findViewById(R.id.nameProfile);
            viewHolder.postDate = (TextView) view.findViewById(R.id.postDate);
            viewHolder.postNumber = (TextView) view.findViewById(R.id.postNumber);
            viewHolder.textPost = (TextView) view.findViewById(R.id.textPost);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        ForumPost obj = list.get(position);
        final String userTypeAdmin = context.getString(R.string.user_type_admin);
        int userColor = context.getResources().getColor(R.color.orange);
        if (obj.getUser().toLowerCase().equals(userTypeAdmin.toLowerCase())) {
            userColor = context.getResources().getColor(R.color.material_drawer_accent);
        }
        holder.iconProfile.setImageDrawable(Helpers.getIconProfileName(obj.getUser(), userColor));
        holder.nameProfile.setText(obj.getUser());
        holder.postDate.setText(Helpers.DateToStringPublishDate(obj.getCreateDate()));
        holder.postNumber.setText("#" + Integer.toString(position + 1));
        holder.textPost.setText(obj.getContent().trim());
        return view;
    }
}
