package id.bizdir.ui.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.util.List;

import id.bizdir.R;
import id.bizdir.model.Anggota;
import id.bizdir.ui.activity.MemberKadinDetailActivity;
import id.bizdir.ui.widget.CircleButton;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 20/04/2015.
 */
public class MemberKadinAdapter extends ArrayAdapter<Anggota> {

    private final List<Anggota> list;
    private final Activity context;
    private final int layoutId;

    public MemberKadinAdapter(Activity context, int layoutId, List<Anggota> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected View viewParent;
        protected RoundedImageView logoMember;
        protected ImageView logoVerified;
        protected TextView name;
        protected TextView city;
        protected CircleButton buttonCall;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.viewParent = view.findViewById(R.id.viewParent);
            viewHolder.logoMember = (RoundedImageView) view.findViewById(R.id.logoMember);
            viewHolder.logoVerified = (ImageView) view.findViewById(R.id.logoVerified);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.city = (TextView) view.findViewById(R.id.city);
            viewHolder.buttonCall = (CircleButton) view.findViewById(R.id.buttonCall);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        final Anggota memberKadin = list.get(position);
        String logoUrl = memberKadin.getThumb();
        if (!logoUrl.isEmpty()) {
            //holder.logoMember.setImageResource(R.drawable.default_member);
            Glide.with(holder.logoMember.getContext())
                    .load(logoUrl)
                    .centerCrop()
                    .crossFade()
                    .into(holder.logoMember);
        } else {
            holder.logoMember.setImageResource(R.drawable.default_member);
        }

        int isVerified = memberKadin.getVerification();
        if (isVerified > 0) {
            holder.logoVerified.setVisibility(View.VISIBLE);
        } else {
            holder.logoVerified.setVisibility(View.GONE);
        }
        if (!memberKadin.getPhone().isEmpty()) {
            holder.buttonCall.setVisibility(View.VISIBLE);
            holder.buttonCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPhone(memberKadin);
                    //click(position);
                }
            });
        } else {
            holder.buttonCall.setVisibility(View.GONE);
        }
        holder.name.setText(list.get(position).getName());
        holder.city.setText(list.get(position).getAddress());
        holder.viewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDetail(memberKadin);
            }
        });
        return view;
    }

    private void gotoDetail(Anggota memberKadin) {
        Intent intent = new Intent(getContext(), MemberKadinDetailActivity.class);
        intent.putExtra(Const.OBJECT_INDEX, memberKadin.getId());
        getContext().startActivity(intent);
    }

    private void callPhone(Anggota memberKadin) {
        String phoneNumber = memberKadin.getPhone();
        phoneNumber = phoneNumber.replace("(hunting)", "");
        final String[] phones = Helpers.getArrayString(phoneNumber);
        int phoneCount = phones.length;
        if (phoneCount > 0) {
            BottomSheet.Builder sheetBuilder = new BottomSheet.Builder(getContext(), R.style.BottomSheet_StyleDialog)
                    .title(getContext().getResources().getString(R.string.bottom_sheet_call) + " " +
                            memberKadin.getName()).
                            listener(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int index) {
                                    Helpers.setPhoneDial(phones[index - 1], context);
                                }
                            });
            int countId = 1;
            for (String phone : phones) {
                sheetBuilder.sheet(countId, Helpers.getBottomIcon(FontAwesome.Icon.faw_phone, true),
                        phone);
                countId++;
            }
            sheetBuilder.build().show();
        }
    }
}
