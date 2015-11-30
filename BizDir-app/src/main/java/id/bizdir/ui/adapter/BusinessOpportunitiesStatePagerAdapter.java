package id.bizdir.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import java.util.List;
import java.util.Locale;

import id.bizdir.model.OpportunityCategory;
import id.bizdir.ui.fragment.BusinessOpportunitiesFragment;

/**
 * Created by Hendry on 07/05/2015.
 */
public class BusinessOpportunitiesStatePagerAdapter extends CacheFragmentStatePagerAdapter {

    private List<OpportunityCategory> opportunityCategoryList;

    public BusinessOpportunitiesStatePagerAdapter(FragmentManager fm, List<OpportunityCategory> opportunityCategoryList) {
        super(fm);
        this.opportunityCategoryList = opportunityCategoryList;
    }

    @Override
    protected Fragment createItem(int position) {
        Fragment f;
        int opportunityCategoryId = opportunityCategoryList.get(position).getId();
        switch (position) {
            default:
                BusinessOpportunitiesFragment businessOpportunitesFragment = new BusinessOpportunitiesFragment();
                f = businessOpportunitesFragment.newInstance(opportunityCategoryId);
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return opportunityCategoryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return opportunityCategoryList.get(position).getTitle().toUpperCase(Locale.ENGLISH);
    }
}
