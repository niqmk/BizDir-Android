package id.bizdir.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.AnggotaCategoryHelper;
import id.bizdir.model.AnggotaCategory;
import id.bizdir.model.City;
import id.bizdir.model.Province;
import id.bizdir.ui.activity.ChooseLocationActivity;
import id.bizdir.ui.activity.MainActivity;
import id.bizdir.ui.activity.SubCategoryActivity;
import id.bizdir.ui.adapter.CategoryAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * Created by Hendry on 20/04/2015.
 */
public class CategoryMainHeaderGridViewFragment extends Fragment {

    //private ImageView mImageView;
    private GridViewWithHeaderAndFooter mGridView;
    private List<AnggotaCategory> listCategory;
    private City city;
    private Province province;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public CategoryMainHeaderGridViewFragment newInstance() {
        CategoryMainHeaderGridViewFragment fragment = new CategoryMainHeaderGridViewFragment();
        return fragment;
    }

    public CategoryMainHeaderGridViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View rootView = localInflater.inflate(R.layout.fragment_category_headergridview_main, container, false);

        //View rootView = inflater.inflate(R.layout.fragment_category_headergridview_main, container, false);
        //LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View headerLayout = localInflater.inflate(R.layout.advertising_view, null);
        //View footerLayout = layoutInflater.inflate(R.layout.footer_view, null);
        ImageView imageButtonAds = (ImageView) headerLayout.findViewById(R.id.imageButtonAds);
        ImageView imageAds = (ImageView) headerLayout.findViewById(R.id.image);
        mGridView = (GridViewWithHeaderAndFooter) rootView.findViewById(R.id.gridView);
        mGridView.addHeaderView(headerLayout);
        //mGridView.addFooterView(footerLayout);
        mGridView.setOnItemClickListener(setOnItemClickListener);
        setDataSource();
        Helpers.getLocalAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_GROUP);
        Helpers.getRemoteAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_GROUP);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.size() > 0) {
            menu.clear();
        }
        inflater.inflate(R.menu.menu_fragment_category, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_location:
                chooseLocation();
                return true;
            case R.id.action_home:
                ((MainActivity) getActivity()).onSectionAttached(Const.PAGE_HOME_SEARCH);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseLocation() {
        Intent intent = new Intent(getActivity(), ChooseLocationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        boolean isDataChange = App.getDataMemberIsChanged();
        if (isDataChange) {
            getData();
        }
        super.onResume();
    }

    private void getData() {
        city = App.getCity();
        province = App.getProvince();

        if (city == null && province == null) {
            city = new City();
            city.setId(0);
            city.setName(getString(R.string.location_indonesia));
            city.setProvinceId(0);
            city.setName(getString(R.string.location_indonesia));
            province = new Province();
            province.setId(0);
            province.setName(getString(R.string.location_indonesia));
            App.setCity(city);
            App.setProvince(province);
        } else {

            if (province.getId() > 0) {
                //textRegion.setText(province.getName());
                //urlIconRegion = province.getPicture();
                //showWeather(false);
            } else {
                //textRegion.setText(province.getName());
                //urlIconRegion = "";
                //showWeather(false);
            }

            if (city.getId() > 0) {
                //textRegion.setText(city.getName());
                //showWeather(true);
            } else {
                if (city.getProvinceId() > 0) {
                    //textRegion.setText(city.getProvinceName());
                } else {
                    //textRegion.setText(city.getName());
                }
                //showWeather(false);
            }
        }
        //showIcon(urlIconRegion);
        App.setDataMemberIsChanged(false);
    }

    /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((MainActivity) activity).onSectionAttached(
        //        getArguments().getInt(ARG_SECTION_NUMBER));
    }*/

    private void setDataSource() {
        AnggotaCategoryHelper categoryHelper = new AnggotaCategoryHelper();
        //listCategory = categoryHelper.getAllForListView(false);
        listCategory = categoryHelper.getAll();
        ArrayAdapter<AnggotaCategory> adapter = new CategoryAdapter(getActivity(),
                R.layout.item_gridview_category, listCategory);
        mGridView.setAdapter(adapter);
    }

    AdapterView.OnItemClickListener setOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AnggotaCategory category = listCategory.get(position);
            if (category != null) {
                Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                intent.putExtra(Const.TOOLBAR_TITLE, category.getTitle());
                intent.putExtra(Const.OBJECT_INDEX, category.getId());
                startActivity(intent);
            }
        }
    };

}
