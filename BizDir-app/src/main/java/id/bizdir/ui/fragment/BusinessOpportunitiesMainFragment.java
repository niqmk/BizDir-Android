package id.bizdir.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.bizdir.R;
import id.bizdir.model.OpportunityCategory;
import id.bizdir.modelhelper.OpportunityCategoryHelper;
import id.bizdir.ui.adapter.BusinessOpportunitiesStatePagerAdapter;
import id.bizdir.ui.widget.SlidingTabLayout;

/**
 * Created by Hendry on 20/04/2015.
 */
public class BusinessOpportunitiesMainFragment extends Fragment {

    private ViewPager mPager;
    private BusinessOpportunitiesStatePagerAdapter mPagerAdapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public BusinessOpportunitiesMainFragment newInstance() {
        BusinessOpportunitiesMainFragment fragment = new BusinessOpportunitiesMainFragment();
        return fragment;
    }

    public BusinessOpportunitiesMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View rootView = localInflater.inflate(R.layout.fragment_event_calendar_main, container, false);

        //View rootView = inflater.inflate(R.layout.fragment_event_calendar_main, container, false);
        OpportunityCategoryHelper opportunityCategoryHelper = new OpportunityCategoryHelper();
        List<OpportunityCategory> listCategory = opportunityCategoryHelper.getAll();
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPagerAdapter = new BusinessOpportunitiesStatePagerAdapter(getFragmentManager(), listCategory);
        mPager.setAdapter(mPagerAdapter);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.material_drawer_accent));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);
        return rootView;
    }

}
