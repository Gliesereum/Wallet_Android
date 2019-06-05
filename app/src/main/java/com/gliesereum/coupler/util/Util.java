package com.gliesereum.coupler.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.gliesereum.coupler.BuildConfig;
import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler.data.network.json.notificatoin.RegistrationTokenDeleteResponse;
import com.gliesereum.coupler.ui.AboutActivity;
import com.gliesereum.coupler.ui.CarListActivity;
import com.gliesereum.coupler.ui.ChooseServiceNewActivity;
import com.gliesereum.coupler.ui.LoginActivity;
import com.gliesereum.coupler.ui.MapsActivity;
import com.gliesereum.coupler.ui.ProfileActivity;
import com.gliesereum.coupler.ui.RecordListActivity;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.BUSINESS_CATEGORY_NAME;
import static com.gliesereum.coupler.util.Constants.BUSINESS_CODE;
import static com.gliesereum.coupler.util.Constants.CAR_BRAND;
import static com.gliesereum.coupler.util.Constants.CAR_FILTER_LIST;
import static com.gliesereum.coupler.util.Constants.CAR_ID;
import static com.gliesereum.coupler.util.Constants.CAR_MODEL;
import static com.gliesereum.coupler.util.Constants.CAR_SERVICE_CLASS;
import static com.gliesereum.coupler.util.Constants.FIREBASE_TOKEN;
import static com.gliesereum.coupler.util.Constants.IS_LOGIN;
import static com.gliesereum.coupler.util.Constants.USER_AVATAR;
import static com.gliesereum.coupler.util.Constants.USER_NAME;
import static com.gliesereum.coupler.util.Constants.USER_SECOND_NAME;

public class Util {
    private Activity activity;
    private Toolbar toolbar;
    private int identifier;
    private APIInterface API;
    private CustomCallback customCallback;
    private LottieAlertDialog alertDialog;
    private Drawer result;


    public Util(Activity activity, Toolbar toolbar, int identifier) {
        this.activity = activity;
        this.toolbar = toolbar;
        this.identifier = identifier;
        FastSave.init(activity.getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(activity.getApplicationContext(), activity);
    }

    public void addNavigation() {
        new DrawerBuilder().withActivity(activity).build();
        PrimaryDrawerItem mapsItem = new PrimaryDrawerItem().withName("Карта" + FastSave.getInstance().getString(BUSINESS_CATEGORY_NAME, "")).withIdentifier(1).withTag("maps").withIcon(R.drawable.ic_outline_explore_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem serviceItem = new SecondaryDrawerItem().withName("Список сервисов").withIdentifier(8).withSelectable(false).withTag("service").withSelectable(false).withIcon(R.drawable.ic_outline_store_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem car_listItem = new SecondaryDrawerItem().withName("Список авто").withIdentifier(2).withTag("car_list").withSelectable(false).withIcon(R.drawable.ic_outline_directions_car_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem record_listItem = new SecondaryDrawerItem().withName("Список заказов").withIdentifier(3).withTag("record_list").withSelectable(false).withIcon(R.drawable.ic_outline_list_alt_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem profileItem = new SecondaryDrawerItem().withName("Мой Профиль").withIdentifier(4).withTag("profile").withSelectable(false).withIcon(R.drawable.ic_outline_account_circle_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem logoutItem = new SecondaryDrawerItem().withName("Выйти").withIdentifier(5).withSelectable(false).withTag("logout").withSelectable(false).withIcon(R.drawable.ic_outline_exit_to_app_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem loginItem = new SecondaryDrawerItem().withName("Вход").withIdentifier(6).withSelectable(false).withTag("login").withSelectable(false).withIcon(R.drawable.ic_outline_exit_to_app_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem aboutItem = new SecondaryDrawerItem().withName("О приложении").withIdentifier(9).withSelectable(false).withTag("about").withSelectable(false).withIcon(R.drawable.ic_outline_info_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem versionItem = new SecondaryDrawerItem().withName("v" + BuildConfig.VERSION_NAME + "beta").withIdentifier(7).withSelectable(false).withTag("version").withSelectable(false);

        if (!FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
            car_listItem.withEnabled(false);
            record_listItem.withEnabled(false);
            profileItem.withEnabled(false);
        }

        if (FastSave.getInstance().getString(BUSINESS_CODE, "").equals("")) {
            mapsItem.withEnabled(false);
        }

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.get().load(uri).placeholder(placeholder).transform(new CircleTransform()).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.get().cancelRequest(imageView);
            }
        });

        ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem();
        profileDrawerItem.withName(FastSave.getInstance().getString(USER_NAME, "") + " " + FastSave.getInstance().getString(USER_SECOND_NAME, ""));
        if (FastSave.getInstance().getString(USER_AVATAR, "").equals("")) {
            profileDrawerItem.withIcon(activity.getResources().getDrawable(R.mipmap.ic_launcher_round));
        } else {
            profileDrawerItem.withIcon(FastSave.getInstance().getString(USER_AVATAR, ""));
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
//                .withHeaderBackground(R.drawable.cover_karma_new)
                .withHeaderBackground(R.drawable.cover_karma_new)
                .addProfiles(profileDrawerItem)
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
//                        return false;
//                    }
//                })
                .build();

        DrawerBuilder drawerBuilder = new DrawerBuilder();
        drawerBuilder.withAccountHeader(headerResult);
        drawerBuilder.withActivity(activity);
        drawerBuilder.withToolbar(toolbar);
        drawerBuilder.withSelectedItem(identifier);
        drawerBuilder.addDrawerItems(
                mapsItem,
                new DividerDrawerItem(),
                serviceItem,
                car_listItem,
                record_listItem,
                profileItem
        );
        result = drawerBuilder.build();
        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch (drawerItem.getTag().toString()) {
                    case "maps":
                        activity.startActivity(new Intent(activity.getApplicationContext(), MapsActivity.class));
                        activity.finish();
                        result.closeDrawer();
                        break;
                    case "car_list":
                        activity.startActivity(new Intent(activity.getApplicationContext(), CarListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        result.closeDrawer();
                        break;
                    case "record_list":
                        activity.startActivity(new Intent(activity.getApplicationContext(), RecordListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        result.closeDrawer();
                        break;
                    case "logout":
                        if (result.isDrawerOpen()) {
                            result.closeDrawer();
                        }
                        alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_QUESTION)
                                .setTitle("Выход")
                                .setDescription("Вы действительно хотите выйти со своего профиля?")
                                .setPositiveText("Да")
                                .setNegativeText("Нет")
                                .setPositiveButtonColor(activity.getResources().getColor(R.color.md_red_A200))
                                .setPositiveListener(new ClickListener() {
                                    @Override
                                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                        deleteRegistrationToken();
                                        FastSave.getInstance().deleteValue(IS_LOGIN);
                                        FastSave.getInstance().deleteValue(USER_NAME);
                                        FastSave.getInstance().deleteValue(USER_SECOND_NAME);
                                        FastSave.getInstance().deleteValue(CAR_ID);
                                        FastSave.getInstance().deleteValue(CAR_BRAND);
                                        FastSave.getInstance().deleteValue(CAR_SERVICE_CLASS);
                                        FastSave.getInstance().deleteValue(CAR_MODEL);
                                        FastSave.getInstance().deleteValue(CAR_FILTER_LIST);
                                        FastSave.getInstance().deleteValue(ACCESS_TOKEN);
                                        activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                        activity.finish();
                                    }
                                })
                                .setNegativeListener(new ClickListener() {
                                    @Override
                                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                        alertDialog.dismiss();
                                    }
                                })
                                .build();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        break;
                    case "profile":
                        activity.startActivity(new Intent(activity.getApplicationContext(), ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        result.closeDrawer();
                        break;
                    case "login":
                        activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        activity.finish();
                        break;
                    case "service":
                        activity.startActivity(new Intent(activity.getApplicationContext(), ChooseServiceNewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
//                        activity.finish();
                        result.closeDrawer();
                        break;
                    case "about":
                        result.closeDrawer();
                        activity.startActivity(new Intent(activity.getApplicationContext(), AboutActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        break;
                }

                return true;
            }
        });


        if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
            result.addItem(logoutItem);
        } else {
            result.addItem(loginItem);
        }
        result.addItem(new DividerDrawerItem());
        result.addItem(aboutItem);
        result.addItem(versionItem);

    }

    private void deleteRegistrationToken() {
        API.deleteRegistrationToken(FastSave.getInstance().getString(ACCESS_TOKEN, ""), FastSave.getInstance().getString(FIREBASE_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<RegistrationTokenDeleteResponse>() {
                    @Override
                    public void onSuccessful(Call<RegistrationTokenDeleteResponse> call, Response<RegistrationTokenDeleteResponse> response) {

                    }

                    @Override
                    public void onEmpty(Call<RegistrationTokenDeleteResponse> call, Response<RegistrationTokenDeleteResponse> response) {

                    }
                }));

    }

    public static boolean checkExpirationToken(Long localDateTime) {
        if (localDateTime > System.currentTimeMillis()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getStringTime(Long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(millisecond));

    }

    public static String getStringDateTrue(Long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(millisecond);
        return format.format(calendar.getTime());

    }

    public static String getStringDate(Long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(millisecond));
    }

    public static boolean checkCarWashWorkTime(AllCarWashResponse carWash) {
        String dayOfWeek = "";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                dayOfWeek = "MONDAY";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "TUESDAY";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "WEDNESDAY";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "THURSDAY";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "FRIDAY";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "SATURDAY";
                break;
            case Calendar.SUNDAY:
                dayOfWeek = "SUNDAY";
                break;
        }

        for (int i = 0; i < carWash.getWorkTimes().size(); i++) {
            if (carWash.getWorkTimes().get(i).getDayOfWeek().equals(dayOfWeek) && carWash.getWorkTimes().get(i).isIsWork()) {
                if (carWash.getWorkTimes().get(i).getFrom() < (System.currentTimeMillis() + (carWash.getTimeZone() * 60000)) && carWash.getWorkTimes().get(i).getTo() > (System.currentTimeMillis() + (carWash.getTimeZone() * 60000))) {
                    Log.d("test_log", "car wash work");
                    return true;
                }
            }
        }
        Log.d("test_log", "car wash not work: ");
        return false;
    }

}
