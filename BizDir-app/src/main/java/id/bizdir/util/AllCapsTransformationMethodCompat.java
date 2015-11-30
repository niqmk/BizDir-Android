package id.bizdir.util;

import android.graphics.Rect;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.view.View;

/**
 * Created by Hendry on 02/05/2015.
 */
public class AllCapsTransformationMethodCompat implements TransformationMethod {

    private static AllCapsTransformationMethodCompat sInstance;

    public static AllCapsTransformationMethodCompat getInstance() {
        if (sInstance == null) {
            sInstance = new AllCapsTransformationMethodCompat();
        }
        return sInstance;
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return !TextUtils.isEmpty(source)
                ?
                source.toString().toUpperCase(view.getContext().getResources().getConfiguration().locale)
                :
                source;
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText,
                               boolean focused, int direction, Rect previouslyFocusedRect) {
        // TODO Auto-generated method stub

    }

}