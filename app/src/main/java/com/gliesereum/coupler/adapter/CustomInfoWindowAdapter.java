package com.gliesereum.coupler.adapter;

import android.app.Activity;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gliesereum.coupler.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private final View mContents;


    public CustomInfoWindowAdapter(Activity activity) {
        Log.d("test_log", "CustomInfoWindowAdapter: ");
        mWindow = activity.getLayoutInflater().inflate(R.layout.custom_info_window, null);
        mContents = activity.getLayoutInflater().inflate(R.layout.custom_info_contents, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Log.d("MAPWINDOF", "getInfoWindow: ");
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Log.d("MAPWINDOF", "getInfoContents: ");
        render(marker, mContents);
        return mContents;
    }

    private void render(Marker marker, View view) {
        ((ImageView) view.findViewById(R.id.badge)).setImageResource(R.mipmap.ic_launcher_round);
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        String title = marker.getTitle();
        if (title != null) {
            titleUi.setText(title);
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
    }
}
