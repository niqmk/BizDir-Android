package id.bizdir.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import java.util.List;
import java.util.Locale;

import id.bizdir.model.EventCategory;
import id.bizdir.ui.fragment.EventCalendarFragment;

/**
 * Created by Hendry on 07/05/2015.
 */
public class EventCalendarStatePagerAdapter extends CacheFragmentStatePagerAdapter {

    //private static final String[] TITLES = App.getContext().getResources()
    //       .getStringArray(R.array.event_calendar_tab);

    private List<EventCategory> listEventCategory;

    public EventCalendarStatePagerAdapter(FragmentManager fm, List<EventCategory> listEventCategory) {
        super(fm);
        this.listEventCategory = listEventCategory;
    }

    @Override
    protected Fragment createItem(int position) {
        Fragment f;
        //final int pattern = position % 5;
        int eventId = listEventCategory.get(position).getId();
        switch (position) {
            default:
                EventCalendarFragment eventCalendarFragment = new EventCalendarFragment();
                f = eventCalendarFragment.newInstance(eventId);
                //f = new ViewPagerTab2ScrollViewFragment();
                //PlaceholderFragment defaultFragment = new PlaceholderFragment();
                //f = defaultFragment;
                //ViewPagerTab2ListViewFragment viewPagerTab2ListViewFragment = new ViewPagerTab2ListViewFragment();
                //f = viewPagerTab2ListViewFragment;
                //f = PlaceholderFragment.newInstance(position);
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        //return TITLES.length;
        return listEventCategory.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return TITLES[position];
        return listEventCategory.get(position).getTitle().toUpperCase(Locale.ENGLISH);
    }
}
