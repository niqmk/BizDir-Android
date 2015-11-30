package id.bizdir.ui.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import id.bizdir.R;
import id.bizdir.model.DownloadRoot;
import id.bizdir.model.DownloadSub;
import id.bizdir.ui.widget.AnimatedExpandableListView;
import id.bizdir.ui.widget.CircleButton;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 02/07/2015.
 */
public class DownloadAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private List<DownloadRoot> items;
    private final int layoutIdRoot;
    private final int layoutIdChild;
    private final Activity activity;
    private DownloadManager dm;
    private long enqueue;

    public DownloadAdapter(Activity activity, int layoutIdRoot, int layoutIdChild, List<DownloadRoot> items) {
        this.layoutIdRoot = layoutIdRoot;
        this.layoutIdChild = layoutIdChild;
        this.activity = activity;
        this.items = items;
    }

    private static class ChildHolder {
        ImageView imageFileType;
        TextView title;
        CircleButton button;
    }

    private static class GroupHolder {
        TextView title;
    }

    @Override
    public DownloadSub getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).getDownloadSubList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder holder;
        final DownloadSub item = getChild(groupPosition, childPosition);
        if (convertView == null) {
            holder = new ChildHolder();
            LayoutInflater inflator = activity.getLayoutInflater();
            convertView = inflator.inflate(layoutIdChild, null);
            //convertView = inflater.inflate(R.layout.item_child_download, parent, false);
            holder.imageFileType = (ImageView) convertView.findViewById(R.id.imageFileType);
            holder.title = (TextView) convertView.findViewById(R.id.textTitle);
            holder.button = (CircleButton) convertView.findViewById(R.id.buttonDownload);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        holder.title.setText(item.getName());
        int iconFileType = Helpers.getIconFileType(item.getFileType());
        holder.imageFileType.setImageResource(iconFileType);
        //File extStore = Environment.getExternalStorageDirectory();
        String fileName = item.getName() + "." + item.getFileType();
        File extStore = Environment.getExternalStoragePublicDirectory("bizdir");
        final File myFile = new File(extStore.getAbsolutePath() + "/" + fileName);

        if (myFile.exists()) {
            holder.button.setImageResource(R.drawable.ic_exit_to_app_white_24dp);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDownload(myFile);
                }
            });
        } else {
            holder.button.setImageResource(R.drawable.ic_file_download_white_24dp);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadFile(holder.button, item, myFile);
                }
            });
        }

        return convertView;
    }


    private void downloadFile(final CircleButton button, DownloadSub downloadSub, final File url) {

        boolean isavailable = Helpers.isInternetConnected(activity);
        if (isavailable) {
            final MaterialDialog progressDialog = new MaterialDialog.Builder(activity)
                    .title("Loading")
                    .content("Please wait...")
                    .progress(true, 0)
                    .cancelable(false)
                    .show();

            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                        //long downloadId = intent.getLongExtra(
                        //        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(enqueue);
                        Cursor c = dm.query(query);
                        if (c.moveToFirst()) {
                            int columnIndex = c
                                    .getColumnIndex(DownloadManager.COLUMN_STATUS);
                            if (DownloadManager.STATUS_SUCCESSFUL == c
                                    .getInt(columnIndex)) {
                                progressDialog.dismiss();
                                button.setImageResource(R.drawable.ic_exit_to_app_white_24dp);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showDownload(url);
                                    }
                                });
                                showDownload(url);
                            }
                        }
                    }
                }
            };

            activity.registerReceiver(receiver, new IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            if (!downloadSub.getUrl().isEmpty()) {
                Environment.getExternalStoragePublicDirectory("bizdir").mkdirs();
                dm = (DownloadManager) activity.getSystemService(Activity.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(downloadSub.getUrl())).
                        setDestinationInExternalPublicDir("bizdir", downloadSub.getName()
                                + "." + downloadSub.getFileType());
                enqueue = dm.enqueue(request);
            }
        } else {
            new MaterialDialog.Builder(activity)
                    .title(R.string.no_internet_connection_title)
                    .content(R.string.no_internet_connection)
                    .positiveText(R.string.button_ok)
                    .show();
        }
    }

    private void showDownload(File url) {
        //Intent i = new Intent();
        //i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        //activity.startActivity(i);
        try {
            Helpers.openFile(activity, url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return items.get(groupPosition).getDownloadSubList().size();
    }

    @Override
    public DownloadRoot getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        DownloadRoot item = getGroup(groupPosition);
        if (convertView == null) {
            holder = new GroupHolder();
            LayoutInflater inflator = activity.getLayoutInflater();
            convertView = inflator.inflate(layoutIdRoot, null);
            //convertView = inflater.inflate(R.layout.item_group_download, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.textTitle);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        holder.title.setText(item.getTitle());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

}
