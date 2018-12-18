package com.gliesereum.karma.util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.CarListActivity;
import com.gliesereum.karma.LoginActivity;
import com.gliesereum.karma.MapsActivity;
import com.gliesereum.karma.ProfileActivity;
import com.gliesereum.karma.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import androidx.appcompat.widget.Toolbar;

import static com.gliesereum.karma.util.Constants.IS_LOGIN;
import static com.gliesereum.karma.util.Constants.USER_NAME;
import static com.gliesereum.karma.util.Constants.USER_SECOND_NAME;

public class Util {
    private Activity activity;
    private Toolbar toolbar;


    public Util(Activity activity, Toolbar toolbar) {
        this.activity = activity;
        this.toolbar = toolbar;
    }

    public void addNavigation() {
        new DrawerBuilder().withActivity(activity).build();
        PrimaryDrawerItem mapsItem = new PrimaryDrawerItem().withName("Карта").withSelectable(false).withTag("maps").withIcon(R.drawable.ic_map_black_24dp);
        SecondaryDrawerItem car_listItem = new SecondaryDrawerItem().withName("Список авто").withSelectable(false).withTag("car_list");
        SecondaryDrawerItem profileItem = new SecondaryDrawerItem().withName("Мой Профиль").withSelectable(false).withTag("profile");
        SecondaryDrawerItem logoutItem = new SecondaryDrawerItem().withName("Выйти").withSelectable(false).withTag("logout");
        SecondaryDrawerItem loginItem = new SecondaryDrawerItem().withName("Вход/Регистрация").withSelectable(false).withTag("login");

        if (!FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
            car_listItem.withEnabled(false);
            profileItem.withEnabled(false);
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(FastSave.getInstance().getString(USER_NAME, "") + " " + FastSave.getInstance().getString(USER_SECOND_NAME, "")).withIcon(activity.getResources().getDrawable(R.drawable.profile))
                )
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
//                        return false;
//                    }
//                })
                .build();

        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(activity)
                .withToolbar(toolbar)
                .addDrawerItems(
                        mapsItem,
                        new DividerDrawerItem(),
                        car_listItem,
                        profileItem
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        Toast.makeText(activity.getApplicationContext(), String.valueOf(drawerItem.getTag().toString()), Toast.LENGTH_SHORT).show();
                        switch (drawerItem.getTag().toString()) {
                            case "maps":
                                activity.startActivity(new Intent(activity.getApplicationContext(), MapsActivity.class));
                                activity.finish();
                                break;
                            case "car_list":
                                activity.startActivity(new Intent(activity.getApplicationContext(), CarListActivity.class));
                                activity.finish();
                                break;
                            case "logout":
                                FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                                activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class));
                                activity.finish();
                                break;
                            case "profile":
                                activity.startActivity(new Intent(activity.getApplicationContext(), ProfileActivity.class));
                                break;
                            case "login":
                                activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class));
                                break;
                        }

                        return true;
                    }
                })
                .build();

        if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
            result.addItem(logoutItem);
        } else {
            result.addItem(loginItem);
        }

    }

    public static boolean checkExpirationToken(Long localDateTime) {
        if (localDateTime > System.currentTimeMillis()) {
            return true;
        } else {
            return false;
        }
    }
}
