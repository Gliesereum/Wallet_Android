package com.gliesereum.karma;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.car.AllCarResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.google.android.material.button.MaterialButton;
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

import org.json.JSONObject;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.IS_LOGIN;

public class CarListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TweetAdapter tweetAdapter;
    private List<AllCarResponse> carsList;
    private MaterialButton addCarBtn;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        FastSave.init(getApplicationContext());
        Log.d("TOKEN", FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        initView();
        getAllCars();
    }

    private void getAllCars() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<AllCarResponse>> call = apiInterface.getAllCars("Bearer " + FastSave.getInstance().getString(ACCESS_TOKEN, ""));
        call.enqueue(new Callback<List<AllCarResponse>>() {
            @Override
            public void onResponse(Call<List<AllCarResponse>> call, Response<List<AllCarResponse>> response) {
                if (response.code() == 200) {
                    carsList = response.body();
                    if (carsList != null && carsList.size() > 0) {
                        tweetAdapter.setItems(carsList);
                    } else {
                        Toast.makeText(CarListActivity.this, "Please add car", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorHandler.showError(jObjError.getInt("code"));
                    } catch (Exception e) {
                        errorHandler.showCustomError(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AllCarResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

    private void initView() {
        errorHandler = new ErrorHandler(this, this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetAdapter = new TweetAdapter();
        recyclerView.setAdapter(tweetAdapter);

        new DrawerBuilder().withActivity(this).build();
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Maps");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Car list").withSelectable(false);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(4).withName("Profile").withSelectable(false);
        SecondaryDrawerItem logoutItem = new SecondaryDrawerItem().withIdentifier(3).withName("LogOut").withSelectable(false);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile))
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
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        item3,
                        new SecondaryDrawerItem().withName("drawer_item_settings_code"),
                        logoutItem
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        Toast.makeText(CarListActivity.this, String.valueOf(drawerItem.getIdentifier()), Toast.LENGTH_SHORT).show();
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                startActivity(new Intent(CarListActivity.this, MapsActivity.class));
                                finish();
                                break;
                            case 2:
                                startActivity(new Intent(CarListActivity.this, CarListActivity.class));
                                finish();
                                break;
                            case 3:
                                FastSave.getInstance().saveBoolean(IS_LOGIN, false);
                                startActivity(new Intent(CarListActivity.this, LoginActivity.class));
                                finish();
                                break;
                        }

                        return true;
                    }
                })
                .build();
//modify an item of the drawer
//notify the drawer about the updated element. it will take care about everything else
        //to update only the name, badge, icon you can also use one of the quick methods
        addCarBtn = (MaterialButton) findViewById(R.id.addCarBtn);
        addCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarListActivity.this, TestActivity.class));
            }
        });
    }
}
