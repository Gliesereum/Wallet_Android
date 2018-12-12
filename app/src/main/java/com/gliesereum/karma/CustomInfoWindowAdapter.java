package com.gliesereum.karma;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private final View mContents;
    private Activity activity;

    CustomInfoWindowAdapter(Activity activity) {
        this.activity = activity;
        mWindow = activity.getLayoutInflater().inflate(R.layout.custom_info_window, null);
        mContents = activity.getLayoutInflater().inflate(R.layout.custom_info_contents, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, mContents);
        return mContents;
    }

    private void render(Marker marker, View view) {
        int badge;
        // Use the equals() method on a Marker to check for equals.  Do not use ==.
        badge = R.drawable.badge_qld;
//        } else if (marker.equals(mAdelaide)) {
//            badge = R.drawable.badge_sa;
//        } else if (marker.equals(mSydney)) {
//            badge = R.drawable.badge_nsw;
//        } else if (marker.equals(mMelbourne)) {
//            badge = R.drawable.badge_victoria;
//        } else if (marker.equals(mPerth)) {
//            badge = R.drawable.badge_wa;
//        } else if (marker.equals(mDarwin1)) {
//            badge = R.drawable.badge_nt;
//        } else if (marker.equals(mDarwin2)) {
//            badge = R.drawable.badge_nt;
//        } else if (marker.equals(mDarwin3)) {
//            badge = R.drawable.badge_nt;
//        } else if (marker.equals(mDarwin4)) {
//            badge = R.drawable.badge_nt;
//        } else {
//            // Passing 0 to setImageResource will clear the image view.
//            badge = 0;
//        }

        ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }

        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null && snippet.length() > 12) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
            snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }
    }
}

