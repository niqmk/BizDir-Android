package id.bizdir.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.rengwuxian.materialedittext.MaterialEditText;

import id.bizdir.R;
import id.bizdir.model.Anggota;
import id.bizdir.ui.activity.GoogleMapsActivity;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 02/05/2015.
 */
public class MemberKadinDetailContactFragment extends Fragment {

    private Anggota memberKadin;

    public MemberKadinDetailContactFragment newInstance(Anggota memberKadin) {
        MemberKadinDetailContactFragment fragment = new MemberKadinDetailContactFragment();
        fragment.memberKadin = memberKadin;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            memberKadin = savedInstanceState.getParcelable("memberKadin");
        }
        View view = inflater.inflate(R.layout.fragment_scrollview_member_contact,
                container, false);

        final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);

        Activity parentActivity = getActivity();
        scrollView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        setForm(view);
        return view;
    }

    private void setForm(View view) {
        final TextView textName = (TextView) view.findViewById(R.id.textName);
        textName.setText(memberKadin.getName());

        final ImageView imageVerification = (ImageView) view.findViewById(R.id.imageVerification);
        final TextView textVerification = (TextView) view.findViewById(R.id.textVerification);
        if (memberKadin.getVerification() > 0) {
            imageVerification.setImageResource(R.drawable.ic_verification_on);
            textVerification.setText(getResources().getString(R.string.verification_on));
        } else {
            imageVerification.setImageResource(R.drawable.ic_verification_off);
            textVerification.setText(getResources().getString(R.string.verification_off));
        }

        final MaterialEditText editTextAddress = (MaterialEditText) view.findViewById(R.id.editTextAddress);
        editTextAddress.setText(memberKadin.getAddress());
        if (!memberKadin.getAddress().isEmpty()) {
            float latitude = Helpers.getFloatFromString(memberKadin.getLatitude());
            float longitude = Helpers.getFloatFromString(memberKadin.getLongitude());
            if (latitude != 0 && longitude != 0) {
                editTextAddress.setIconRight(Helpers.getEditTextIcon(FontAwesome.Icon.faw_map_marker, true));
                editTextAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setShowMap();
                        //Toast.makeText(getActivity(), "Sorry, this feature is still under construction. Please check back later!",
                        //        Toast.LENGTH_SHORT).show();
                    }
                });
                editTextAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            setShowMap();
                        }
                    }
                });
            }
        }

        final MaterialEditText editTextPhone = (MaterialEditText) view.findViewById(R.id.editTextPhone);
        editTextPhone.setText(memberKadin.getPhone());
        if (!memberKadin.getPhone().isEmpty()) {
            editTextPhone.setIconRight(Helpers.getEditTextIcon(FontAwesome.Icon.faw_phone, true));
            editTextPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPhoneCall(memberKadin.getPhone());
                }
            });
            editTextPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        setPhoneCall(memberKadin.getPhone());
                    }
                }
            });
        }

        final MaterialEditText editTextFax = (MaterialEditText) view.findViewById(R.id.editTextFax);
        editTextFax.setText(memberKadin.getFax());

        final MaterialEditText editTextEmail = (MaterialEditText) view.findViewById(R.id.editTextEmail);
        editTextEmail.setText(memberKadin.getEmail());
        if (!memberKadin.getEmail().isEmpty()) {
            editTextEmail.setIconRight(Helpers.getEditTextIcon(FontAwesome.Icon.faw_envelope_o, true));
            editTextEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSendEmail(memberKadin.getEmail());
                }
            });
            editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        setSendEmail(memberKadin.getEmail());
                    }
                }
            });
        }

        final MaterialEditText editTextWebsite = (MaterialEditText) view.findViewById(R.id.editTextWebsite);
        editTextWebsite.setText(memberKadin.getWeb());
        if (!memberKadin.getWeb().isEmpty()) {
            editTextWebsite.setIconRight(Helpers.getEditTextIcon(FontAwesome.Icon.faw_globe, true));
            editTextWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOpenWebsite(memberKadin.getWeb());
                }
            });
            editTextWebsite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        setOpenWebsite(memberKadin.getWeb());
                    }
                }
            });
        }
    }

    private void setShowMap() {
        BottomSheet.Builder sheetBuilder = new BottomSheet.Builder(getActivity(), R.style.BottomSheet_StyleDialog)
                .title(getActivity().getResources().getString(R.string.bottom_sheet_showonmap)).
                        listener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int index) {
                                Intent intent = new Intent(getActivity(), GoogleMapsActivity.class);
                                intent.putExtra(Const.OBJECT_INDEX, memberKadin.getId());
                                startActivity(intent);
                            }
                        });
        sheetBuilder.sheet(1, Helpers.getBottomIcon(FontAwesome.Icon.faw_map_marker, true), memberKadin.getName());
        sheetBuilder.build().show();
    }

    private void setPhoneCall(String phoneNumber) {
        phoneNumber = phoneNumber.replace("(hunting)", "");
        final String[] phones = Helpers.getArrayString(phoneNumber);
        int phoneCount = phones.length;
        if (phoneCount > 0) {
            BottomSheet.Builder sheetBuilder = new BottomSheet.Builder(getActivity(), R.style.BottomSheet_StyleDialog)
                    .title(getActivity().getResources().getString(R.string.bottom_sheet_phonecall)).
                            listener(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int index) {
                                    Helpers.setPhoneDial(phones[index - 1], getActivity());
                                    //Toast.makeText(getActivity(), "Sorry, this function is still under construction. Please check back later!", Toast.LENGTH_SHORT).show();
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

        /*
        new BottomSheet.Builder(getActivity(), R.style.BottomSheet_StyleDialog)
                .title(getActivity().getResources().getString(R.string.bottom_sheet_phonecall))
                .sheet(0, Helpers.getBottomIcon(Const.PHONE_ICON, true),
                        phoneNumber).
                listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                */
    }

    private void setSendEmail(String email) {
        final String[] emailList = Helpers.getArrayString(email);
        int emailCount = emailList.length;
        if (emailCount > 0) {
            BottomSheet.Builder sheetBuilder = new BottomSheet.Builder(getActivity(), R.style.BottomSheet_StyleDialog)
                    .title(getActivity().getResources().getString(R.string.bottom_sheet_sendemail)).
                            listener(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int index) {
                                    Helpers.setSendEmail(emailList[index - 1], "", "", getActivity());
                                    //Toast.makeText(getActivity(), "Sorry, this function is still under construction. Please check back later!", Toast.LENGTH_SHORT).show();
                                }
                            });
            int countId = 1;
            for (String mail : emailList) {
                sheetBuilder.sheet(countId, Helpers.getBottomIcon(FontAwesome.Icon.faw_envelope_o, true),
                        mail);
                countId++;
            }
            sheetBuilder.build().show();
        }
    }

    private void setOpenWebsite(String website) {
        final String[] websiteList = Helpers.getArrayString(website);
        int websiteCount = websiteList.length;
        if (websiteCount > 0) {
            BottomSheet.Builder sheetBuilder = new BottomSheet.Builder(getActivity(), R.style.BottomSheet_StyleDialog)
                    .title(getActivity().getResources().getString(R.string.bottom_sheet_openwebsite)).
                            listener(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int index) {
                                    Helpers.setOpenUrlOnWeb(websiteList[index - 1], getActivity());
                                    //Toast.makeText(getActivity(), "Sorry, this function is still under construction. Please check back later!", Toast.LENGTH_SHORT).show();
                                }
                            });
            int countId = 1;
            for (String web : websiteList) {
                sheetBuilder.sheet(countId, Helpers.getBottomIcon(FontAwesome.Icon.faw_globe, true),
                        web);
                countId++;
            }
            sheetBuilder.build().show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("memberKadin", memberKadin);
    }

}