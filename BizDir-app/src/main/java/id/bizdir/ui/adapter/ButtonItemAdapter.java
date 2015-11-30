package id.bizdir.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ArrayRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import id.bizdir.R;
import id.bizdir.ui.widget.CircleButton;

/**
 * Simple adapter example for custom items in the dialog
 */
public class ButtonItemAdapter extends BaseAdapter implements View.OnClickListener {

    private final Context mContext;
    private final CharSequence[] mItems;
    private final CharSequence[] mItemsInfo;

    public ButtonItemAdapter(Context context, @ArrayRes int arrayResId, @ArrayRes int arrayResIdInfo) {
        this(context, context.getResources().getTextArray(arrayResId),
                context.getResources().getTextArray(arrayResIdInfo));
    }

    private ButtonItemAdapter(Context context, CharSequence[] items, CharSequence[] itemsInfo) {
        this.mContext = context;
        this.mItems = items;
        this.mItemsInfo = itemsInfo;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public CharSequence getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_dialog_custom_list, null);
        ((TextView) convertView.findViewById(R.id.title)).setText(mItems[position]);
        CircleButton button = (CircleButton) convertView.findViewById(R.id.button);
        button.setTag(position);
        button.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        Integer index = (Integer) v.getTag();
        new MaterialDialog.Builder(mContext)
                .title(R.string.dialog_verification_ok_title)
                .content(mItemsInfo[index])
                .positiveText(R.string.button_ok)
                .show();
    }
}
