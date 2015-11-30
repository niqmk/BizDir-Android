package id.bizdir.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;

public class ShareSocial {

    public static void postText(Activity act, String subjectText, String messageText) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, messageText);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
        act.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

    public static void postHtmlText(Activity act, String subjectText, String messageText) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<p>" + messageText + "</p>"));
        act.startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    public static void postImage(Activity act, String imgPath) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse("file://" + imgPath);
        sharingIntent.setType("image/jpg");
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        act.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

}
