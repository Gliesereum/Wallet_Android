package com.gliesereum.coupler.ui;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gliesereum.coupler.R;
import com.gliesereum.coupler.adapter.RecordListAdapter;
import com.gliesereum.coupler.data.network.APIClient;
import com.gliesereum.coupler.data.network.APIInterface;
import com.gliesereum.coupler.data.network.CustomCallback;
import com.gliesereum.coupler.data.network.json.record_new.ContentItem;
import com.gliesereum.coupler.data.network.json.record_new.RecordNewResponse;
import com.gliesereum.coupler.util.ErrorHandler;
import com.gliesereum.coupler.util.FastSave;
import com.gliesereum.coupler.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler.util.Constants.RECORD_LIST_ACTIVITY;

//import com.appizona.yehiahd.fastsave.FastSave;

public class RecordListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecordListAdapter recordListAdapter;
    private List<ContentItem> recordsList = new ArrayList<>();
    private Map<String, String> carWashNameMap = new HashMap<>();
    private APIInterface API;
    private ErrorHandler errorHandler;
    private TextView splashTextView;
    private ProgressDialog progressDialog;
    //    private StompClient mStompClient;
    private NotificationManager notifManager;
    private CustomCallback customCallback;
    private String TAG = "activityTest";


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        initView();
        getAllRecord();
    }

    private void getAllRecord() {
            API.getAllRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                    .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<RecordNewResponse>() {
                        @Override
                        public void onSuccessful(Call<RecordNewResponse> call, Response<RecordNewResponse> response) {
                            recordsList = new ArrayList<>();
                            if (response.body() != null && response.body().getContent().size() > 0) {
                                for (int i = 0; i < response.body().getContent().size(); i++) {
                                    recordsList.add(response.body().getContent().get(i));
                                }
                                recordListAdapter.setItems(recordsList);
                            }
                            Log.d(TAG, "onSuccessful: ");
                            FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
                        }

                        @Override
                        public void onEmpty(Call<RecordNewResponse> call, Response<RecordNewResponse> response) {
                            splashTextView.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onEmpty: ");
                            FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
                        }
                    }));
    }

    private void initView() {
        FastSave.init(getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        errorHandler = new ErrorHandler(this, this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Список записей");
        setSupportActionBar(toolbar);
        splashTextView = findViewById(R.id.splashTextView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recordListAdapter = new RecordListAdapter(RecordListActivity.this);
        recyclerView.setAdapter(recordListAdapter);
        new Util(this, toolbar, 3).addNavigation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, false);
        Log.d(TAG, "onStop: ");
    }
}
