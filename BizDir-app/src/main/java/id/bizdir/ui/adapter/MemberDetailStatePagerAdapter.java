package id.bizdir.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.model.Anggota;
import id.bizdir.ui.fragment.MemberKadinDetailContactFragment;
import id.bizdir.ui.fragment.MemberKadinDetailGalleryFragment;
import id.bizdir.ui.fragment.MemberKadinDetailProfileFragment;

/**
 * Created by Hendry on 07/05/2015.
 */
public class MemberDetailStatePagerAdapter extends CacheFragmentStatePagerAdapter {

    private static final String[] TITLES = App.getContext().getResources()
            .getStringArray(R.array.member_kadin_detail_tab);
    private Anggota memberKadin;

    public MemberDetailStatePagerAdapter(FragmentManager fm, Anggota memberKadin) {
        super(fm);
        this.memberKadin = memberKadin;
    }

    @Override
    protected Fragment createItem(int position) {
        Fragment f;
        final int pattern = position % 5;
        switch (pattern) {
            case 0:
                //f = new ViewPagerTab2ScrollViewFragment();
                MemberKadinDetailContactFragment contactFragment = new MemberKadinDetailContactFragment();
                f = contactFragment.newInstance(memberKadin);
                break;
            case 1:
                //f = new ViewPagerTab2RecyclerViewFragment();
                MemberKadinDetailProfileFragment profileFragment = new MemberKadinDetailProfileFragment();
                f = profileFragment.newInstance(memberKadin);
                break;
            case 2:
                //f = new ViewPagerTab2RecyclerViewFragment();
                MemberKadinDetailGalleryFragment galleryFragment = new MemberKadinDetailGalleryFragment();
                f = galleryFragment.newInstance(memberKadin);
                break;
            default:
                //f = new ViewPagerTab2ScrollViewFragment();
                MemberKadinDetailContactFragment defaultFragment = new MemberKadinDetailContactFragment();
                f = defaultFragment.newInstance(memberKadin);
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
        return TITLES[position];
    }
}
