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
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import androidx.appcompat.widget.Toolbar;

import static com.gliesereum.karma.util.Constants.IS_LOGIN;

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

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
//                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(activity.getResources().getDrawable(R.drawable.profile))
                        new ProfileDrawerItem().withName("Mike Penz").withIcon(activity.getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


//create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(activity)
                .withToolbar(toolbar)
                .addDrawerItems(
                        mapsItem,
                        new DividerDrawerItem(),
                        car_listItem,
                        profileItem,
                        logoutItem
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
                        }

                        return true;
                    }
                })
                .build();
    }

    public static boolean checkExpirationToken(Long localDateTime) {
        if (localDateTime > System.currentTimeMillis()) {
            return true;
        } else {
            return false;
        }
    }
}
