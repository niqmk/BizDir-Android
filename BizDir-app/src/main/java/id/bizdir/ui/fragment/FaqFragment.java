package id.bizdir.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.bizdir.R;
import id.bizdir.modelhelper.CommonHelper;
import id.bizdir.model.Common;
import id.bizdir.ui.activity.MainActivity;
import id.bizdir.util.Const;

/**
 * Created by Hendry on 02/05/2015.
 */
public class FaqFragment extends Fragment {

    private Common common;
    private TextView textViewContent;

    public FaqFragment newInstance() {
        FaqFragment fragment = new FaqFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
        }
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.fragment_faq, container, false);

        //View view = inflater.inflate(R.layout.fragment_faq, container, false);
        textViewContent = (TextView) view.findViewById(R.id.textViewContent);
        getData();
        setData();
        return view;
    }

    private void setData() {
        if (common != null) {
            textViewContent.setText(Html.fromHtml(common.getDescription()));
        }
    }

    private void getData() {
        CommonHelper commonHelper = new CommonHelper();
        common = commonHelper.getFaq();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.size() > 0) {
            menu.clear();
        }
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                ((MainActivity) getActivity()).onSectionAttached(Const.PAGE_HOME_SEARCH);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}