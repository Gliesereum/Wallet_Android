package com.gliesereum.coupler.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.gliesereum.coupler.BuildConfig;
import com.gliesereum.coupler.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Element versionElement = new Element();
        versionElement.setTitle("Версия: " + " " + BuildConfig.VERSION_NAME + " beta");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.ic_logo_group)
                .addItem(versionElement)
                .setDescription("Coupler - сервис поиска услуг")
//                .addGroup("Connect with us")
                .addEmail("support@gliesereum.com")
                .addWebsite("https://coupler.app/")
//                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                .addPlayStore("com.gliesereum.coupler")
                .addGroup("Документы")
                .addWebsite("https://coupler.app/policy", "Политика конфиденциальности")
                .addWebsite("https://coupler.app/terms", "Условия использования")
                .create();

        setContentView(aboutPage);

    }
}
