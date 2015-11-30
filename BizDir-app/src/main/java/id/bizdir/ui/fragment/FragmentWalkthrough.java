package id.bizdir.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import id.bizdir.R;

/**
 * Created by Hendry on 23/09/2015.
 */
public class FragmentWalkthrough extends Fragment {

    //private Integer resourceId;
    private String imageUrl;

    public FragmentWalkthrough newInstance(String imageUrl) {
        FragmentWalkthrough fragment = new FragmentWalkthrough();
        fragment.imageUrl = imageUrl;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
        }
        View view = inflater.inflate(R.layout.bizidir_walkthrough, container, false);
        final ImageView mainImage = (ImageView) view.findViewById(R.id.mainImage);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        Glide.with(getActivity())
                .load(imageUrl)
                .crossFade()
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(mainImage) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        //mainImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.advertising_image));
                        progressBar.setVisibility(View.GONE);
                    }
                });
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //switch (item.getItemId()) {
        //}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}