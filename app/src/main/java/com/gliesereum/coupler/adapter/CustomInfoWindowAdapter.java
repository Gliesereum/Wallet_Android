package com.gliesereum.coupler.adapter;

import android.app.Activity;
import android.text.SpannableString;
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
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        ImageView logoImg = view.findViewById(R.id.badge);
//        Map<String, String> markerLogo = new HashMap<>();
//        markerLogo = FastSave.getInstance().getObject(MARKER_LOGO, Map.class);
//        if (markerLogo!=null && markerLogo.get(marker.getSnippet())!=null){
//            Picasso.get().load(markerLogo.get(marker.getSnippet())).into(logoImg);
//        }else {
//            logoImg.setImageResource(R.mipmap.ic_launcher_round);
//        }
        logoImg.setImageResource(R.mipmap.ic_launcher_round);

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

