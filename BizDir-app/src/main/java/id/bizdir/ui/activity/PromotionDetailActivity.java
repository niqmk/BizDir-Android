/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package id.bizdir.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.PromotionHelper;
import id.bizdir.model.Promotion;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;
import id.bizdir.util.ShareSocial;

public class PromotionDetailActivity extends AppCompatActivity {

    private Promotion promotion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_detail);
        Helpers.setLockOrientation(PromotionDetailActivity.this);
        getDataFromPreviousPage();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBar(actionBar);
        }
        //setTitle(promotion.getTitle());

        TextView textViewPromoInfo = (TextView) findViewById(R.id.textViewPromoInfo);
        textViewPromoInfo.setText(promotion.getTitle());

        TextView textViewDesc = (TextView) findViewById(R.id.textViewDesc);
        textViewDesc.setText(promotion.getDescription());

        loadBackdrop();
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        String jsonString = intent.getStringExtra(Const.OBJECT_JSON);
        PromotionHelper promotionHelper = new PromotionHelper();

        if (!TextUtils.isEmpty(jsonString)) {
            try {
                promotion = App.getGson().fromJson(jsonString, Promotion.class);
            } catch (Exception ignore) {
                promotion = promotionHelper.get(objectIndex);
            }
        } else {
            promotion = promotionHelper.get(objectIndex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu.size() > 0) {
            menu.clear();
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                shareThis();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(PromotionDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareThis() {
        String subject = promotion.getTitle();
        String message = promotion.getDescription();
        ShareSocial.postText(PromotionDetailActivity.this, subject, message);
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Glide.with(this)
                .load(promotion.getPicture())
                .centerCrop()
                .crossFade()
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                    }
                });
    }

}
