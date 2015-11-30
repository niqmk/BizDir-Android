package id.bizdir.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import id.bizdir.R;
import id.bizdir.model.Anggota;

/**
 * Created by Hendry on 02/05/2015.
 */
public class MemberKadinDetailProfileFragment extends Fragment {

    private Anggota memberKadin;

    public MemberKadinDetailProfileFragment newInstance(Anggota memberKadin) {
        MemberKadinDetailProfileFragment fragment = new MemberKadinDetailProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_scrollview_member_profile,
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
        textName.setText(memberKadin.getDescription());
        //textName.setText("Sorry, detail information is still under construction. Please check back later!");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("memberKadin", memberKadin);
    }

}