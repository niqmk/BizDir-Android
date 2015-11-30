package id.bizdir.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import java.util.List;

import id.bizdir.R;
import id.bizdir.modelhelper.AnggotaGalleryHelper;
import id.bizdir.model.Anggota;
import id.bizdir.model.AnggotaGallery;
import id.bizdir.ui.activity.ImageViewerActivity;
import id.bizdir.ui.adapter.GalleryAdapter;
import id.bizdir.util.Const;

/**
 * Created by Hendry on 02/05/2015.
 */
public class MemberKadinDetailGalleryFragment extends Fragment {

    private Anggota memberKadin;
    private List<AnggotaGallery> anggotaGalleryList;

    public MemberKadinDetailGalleryFragment newInstance(Anggota memberKadin) {
        MemberKadinDetailGalleryFragment fragment = new MemberKadinDetailGalleryFragment();
        fragment.memberKadin = memberKadin;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            memberKadin = savedInstanceState.getParcelable("memberKadin");
        }
        View view = inflater.inflate(R.layout.fragment_scrollview_member_gallery,
                container, false);

        final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);

        Activity parentActivity = getActivity();
        scrollView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        getData(view);
        return view;
    }

    private void getData(View view) {
        if (memberKadin != null) {
            TextView textNoData = (TextView) view.findViewById(R.id.textNoData);
            AnggotaGalleryHelper anggotaGalleryHelper = new AnggotaGalleryHelper();
            anggotaGalleryList = anggotaGalleryHelper.getAll(memberKadin.getId());
            if (anggotaGalleryList.size() > 0) {
                textNoData.setVisibility(View.GONE);
                ArrayAdapter<AnggotaGallery> adapter = new GalleryAdapter(getActivity(),
                        R.layout.item_anggota_gallery, anggotaGalleryList);
                GridView mGridView = (GridView) view.findViewById(R.id.gridView);
                mGridView.setAdapter(adapter);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AnggotaGallery obj = anggotaGalleryList.get(position);
                        if (obj != null) {
                            Intent intent = new Intent();
                            if (obj.getType().contains(Const.GALLERY_TYPE_VIDEO)) {
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse(obj.getMain()), "video/mp4");
                            } else {
                                intent = new Intent(getActivity(), ImageViewerActivity.class);
                                intent.putExtra(Const.URL_ADDRESS, obj.getMain());
                            }
                            startActivity(intent);
                        }
                    }
                });
            } else {
                textNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("memberKadin", memberKadin);
    }

}