package id.bizdir.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.ui.fragment.ForumCategoriesFragment;
import id.bizdir.ui.fragment.ForumTrendingTopicsFragment;
import id.bizdir.ui.fragment.PlaceholderFragment;

/**
 * Created by Hendry on 07/05/2015.
 */
public class ForumStatePagerAdapter extends CacheFragmentStatePagerAdapter {

    private static final String[] TITLES = App.getContext().getResources()
            .getStringArray(R.array.forum_tab);

    public ForumStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment createItem(int position) {
        Fragment f;
        switch (position) {
            case 0:
                ForumCategoriesFragment forumCategoriesFragment = new ForumCategoriesFragment();
                f = forumCategoriesFragment.newInstance();
                break;
            case 1:
                ForumTrendingTopicsFragment forumTrendingTopicsFragment = new ForumTrendingTopicsFragment();
                f = forumTrendingTopicsFragment.newInstance();
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
