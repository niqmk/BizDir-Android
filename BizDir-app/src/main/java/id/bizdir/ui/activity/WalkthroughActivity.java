package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;

import java.util.List;

import id.bizdir.model.WalkThrough;
import id.bizdir.modelhelper.WalkthroughHelper;
import id.bizdir.ui.fragment.FragmentWalkthrough;

public class WalkthroughActivity extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {
        WalkthroughHelper walkthroughHelper = new WalkthroughHelper();
        List<WalkThrough> walkThroughList = walkthroughHelper.getAll();
        if (walkThroughList.size() > 0) {
            for (WalkThrough walkThrough : walkThroughList) {
                String imageUrl = walkThrough.getImage();
                if (!TextUtils.isEmpty(imageUrl)) {
                    addSlide(new FragmentWalkthrough().newInstance(imageUrl));
                }

            }
        } else {
            loadMainActivity();
        }
    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
        //Toast.makeText(getApplicationContext(), getString(R.string.skip), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    public void getStarted(View v) {
        loadMainActivity();
    }
}
