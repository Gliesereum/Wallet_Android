package com.gliesereum.coupler.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gliesereum.coupler.BuildConfig;
import com.gliesereum.coupler.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Element versionElement = new Element();
        versionElement.setTitle("Версия: " + " " + BuildConfig.VERSION_NAME);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.ic_logo_group)
                .setDescription("Тут должен быть какойто текст. Можно даже много текста")
                .addItem(versionElement)
//                .addItem(adsElement)
//                .addGroup("Connect with us")
                .addEmail("support@gliesereum.com")
                .addWebsite("https://coupler.app/")
//                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                .addPlayStore("com.gliesereum.coupler")
                .addWebsite("https://coupler.app/policy", "Политика конфидицифльности")
                .create();

        setContentView(aboutPage);

    }
}
