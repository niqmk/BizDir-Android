package id.bizdir.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.ui.fragment.NewsBusinessFragment;
import id.bizdir.ui.fragment.NewsKadinListViewFragment;
import id.bizdir.ui.fragment.NewsStockFragment;
import id.bizdir.ui.fragment.PlaceholderFragment;

/**
 * Created by Hendry on 07/05/2015.
 */
public class NewsStatePagerAdapter extends CacheFragmentStatePagerAdapter {

    private static final String[] TITLES = App.getContext().getResources()
            .getStringArray(R.array.news_tab);

    public NewsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment createItem(int position) {
        Fragment f;
        //final int pattern = position % 5;
        switch (position) {
            case 0:
                //NewsKadinFragment newsKadinFragment = new NewsKadinFragment();
                NewsKadinListViewFragment newsKadinListViewFragment = new NewsKadinListViewFragment();
                f = newsKadinListViewFragment.newInstance();
                break;
            case 1:
                NewsBusinessFragment newsBusinessFragment = new NewsBusinessFragment();
                f = newsBusinessFragment.newInstance();
                //f = new NewsBusinessFragment();
                //f = ((NewsBusinessFragment) f).newInstance();
                break;
            case 2:
                NewsStockFragment newsStockFragment = new NewsStockFragment();
                f = newsStockFragment.newInstance();
                break;
            default:
                PlaceholderFragment defaultFragment = new PlaceholderFragment();
                f = defaultFragment.newInstance(position, "BizDir");
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
