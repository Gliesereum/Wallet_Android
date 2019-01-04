package com.gliesereum.karma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.util.ErrorHandler;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private final View mContents;
    private Activity activity;
    private ProgressDialog progressDialog;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;


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
        ((ImageView) view.findViewById(R.id.badge)).setImageResource(R.drawable.badge_qld);
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        String title = marker.getTitle();
        if (title != null) {
            // Spannable carWashId allows us to edit the formatting of the text.
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
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }

//        getCarWash(marker.getSnippet(), view);


    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(activity, "title", "message");

    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

