package id.bizdir.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.ui.fragment.CityFragment;
import id.bizdir.ui.fragment.ProvinceFragment;
import id.bizdir.ui.fragment.ViewPagerTab2ListViewFragment;

/**
 * Created by Hendry on 07/05/2015.
 */
public class ChooseLocationStatePagerAdapter extends CacheFragmentStatePagerAdapter {

    private static final String[] TITLES = App.getContext().getResources()
            .getStringArray(R.array.location_tab);

    public ChooseLocationStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment createItem(int position) {
        Fragment f;
        switch (position) {
            case 0:
                CityFragment cityFragment = new CityFragment();
                f = cityFragment.newInstance();
                break;
            case 1:
                ProvinceFragment provinceFragment = new ProvinceFragment();
                f = provinceFragment.newInstance();
                break;
            default:
                ViewPagerTab2ListViewFragment defaultFragment = new ViewPagerTab2ListViewFragment();
                f = defaultFragment;
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position].toUpperCase();
    }
}
