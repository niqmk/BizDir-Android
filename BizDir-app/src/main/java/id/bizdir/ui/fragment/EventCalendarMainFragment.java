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
import id.bizdir.model.EventCategory;
import id.bizdir.modelhelper.EventCategoryHelper;
import id.bizdir.ui.adapter.EventCalendarStatePagerAdapter;
import id.bizdir.ui.widget.SlidingTabLayout;

/**
 * Created by Hendry on 20/04/2015.
 */
public class EventCalendarMainFragment extends Fragment {

    private ViewPager mPager;
    private EventCalendarStatePagerAdapter mPagerAdapter;
    //private ImageView mImageView;
    //private GridViewWithHeaderAndFooter mGridView;
    //private List<Category> listCategory;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public EventCalendarMainFragment newInstance() {
        EventCalendarMainFragment fragment = new EventCalendarMainFragment();
        return fragment;
    }

    public EventCalendarMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View rootView = localInflater.inflate(R.layout.fragment_event_calendar_main, container, false);

        //View rootView = inflater.inflate(R.layout.fragment_event_calendar_main, container, false);
        EventCategoryHelper eventCategoryHelper = new EventCategoryHelper();
        List<EventCategory> listCategory = eventCategoryHelper.getAll();
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPagerAdapter = new EventCalendarStatePagerAdapter(getFragmentManager(), listCategory);
        mPager.setAdapter(mPagerAdapter);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.material_drawer_accent));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);

        //LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        //View mainActivity = layoutInflater.inflate(R.layout.activity_main, null);
        //DrawShadowFrameLayout drawShadowFrameLayout = (DrawShadowFrameLayout) mainActivity.findViewById(R.id.frame_container);
        //drawShadowFrameLayout.setShadowVisible(false, true);
        //View headerLayout = layoutInflater.inflate(R.layout.advertising_view, null);
        //View footerLayout = layoutInflater.inflate(R.layout.footer_view, null);
        //mImageView = (ImageView) headerLayout.findViewById(R.id.image);
        // mGridView = (GridViewWithHeaderAndFooter) rootView.findViewById(R.id.gridView);
        // mGridView.addHeaderView(headerLayout);
        //mGridView.addFooterView(footerLayout);
        //mGridView.setOnItemClickListener(setOnItemClickListener);
        //setDataSource();
        return rootView;
    }


}
