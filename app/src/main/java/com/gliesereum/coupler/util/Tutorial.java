package com.gliesereum.coupler.util;

import android.os.Bundle;

import com.gliesereum.coupler.R;
import com.hololo.tutorial.library.Step;

import static com.gliesereum.coupler.util.Constants.SHOW_TUTORIAL;

//import com.hololo.tutorial.library.TutorialActivity;


public class Tutorial extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.TutorialStyle);
        super.onCreate(savedInstanceState);

        FastSave.init(getApplicationContext());

        setPrevText("Назад");
        setNextText("Далее");
        setFinishText("Начать");
        setCancelText("Отмена");
        setGivePermissionText("Разрешить");

//        int perm = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        if (perm != PackageManager.PERMISSION_GRANTED) {
//            addFragment(new PermissionStep.Builder()
//                    .setTitle(getString(R.string.grant_access_title))
//                    .setContent(getString(R.string.grant_access))
//                    .setBackgroundColor(Color.parseColor("#292a2c"))
//                    .setDrawable(R.drawable.permission_icon)
//                    .setPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.REQUEST_LOCATION_PERMISSION})
//                    .build());
//        }

        addFragment(new Step.Builder()
                .setTitle("Добро пожаловать в Coupler!")
                .setContent("Все услуги у вас в кармане,\nгде бы вы ни находились")
                .setDrawable(R.drawable.ic_icon_step_01) // int top drawable
                .build());
//
        addFragment(new Step.Builder()
                .setTitle("Умная карта услуг")
                .setContent("Покажет подходящие варианты")
                .setDrawable(R.drawable.ic_icon_step_02) // int top drawable
                .build());
//
        addFragment(new Step.Builder()
                .setTitle("Запишет на услугу и приведет к месту назначения")
                .setContent("Даст скидку за накопленные бонусные баллы")
                .setDrawable(R.drawable.ic_icon_step_03) // int top drawable
                .build());
    }

    @Override
    public void finishTutorial() {
        FastSave.getInstance().saveBoolean(SHOW_TUTORIAL, false);
        finish();
    }

    @Override
    public void currentFragmentPosition(int position) {

    }
}
