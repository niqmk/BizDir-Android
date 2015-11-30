package id.bizdir.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.AnggotaHelper;
import id.bizdir.model.Anggota;
import id.bizdir.ui.activity.MainActivity;
import id.bizdir.ui.adapter.MemberKadinAdapter;
import id.bizdir.util.Const;

/**
 * Created by Hendry on 02/05/2015.
 */
public class FavoriteFragment extends Fragment {

    private List<Anggota> memberKadinList;
    private ListView listFavorite;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int progressInterval = 700;
    private TextView textNoData;

    public FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.fragment_favorite, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        listFavorite = (ListView) view.findViewById(R.id.listFavorite);
        textNoData = (TextView) view.findViewById(R.id.textNoData);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        bindData();
                        mSwipeRefreshLayout.setRefreshing(false);
                        App.setDataMemberIsChanged(false);
                    }
                }, progressInterval);
            }
        });

        getData();
        bindData();
        return view;
    }

    @Override
    public void onResume() {
        boolean isDataChange = App.getDataMemberIsChanged();
        if (isDataChange) {
            RefreshData();
        }
        super.onResume();
    }

    private void RefreshData() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
                bindData();
                mSwipeRefreshLayout.setRefreshing(false);
                App.setDataMemberIsChanged(false);
            }
        }, progressInterval);
    }

    private void getData() {
        AnggotaHelper memberKadinHelper = new AnggotaHelper();
        memberKadinList = memberKadinHelper.getFavoriteAll();
    }

    private void bindData() {
        MemberKadinAdapter memberKadinAdapter = new MemberKadinAdapter(getActivity(),
                R.layout.item_menu_member,
                memberKadinList);
        listFavorite.setAdapter(memberKadinAdapter);
        if (memberKadinList.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.size() > 0) {
            menu.clear();
        }
        inflater.inflate(R.menu.fragment_favorite, menu);
        /*
        MenuItem item =
                menu.add(Menu.NONE, R.id.menu_news_business, Menu.NONE, R.string.action_change_news);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(Helpers.getActionIcon("faw_newspaper_o", false));
        */
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite_refresh:
                RefreshData();
                return true;
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