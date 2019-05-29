package com.gliesereum.karma.ui;

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

import com.gliesereum.karma.R;
import com.gliesereum.karma.adapter.RecordListAdapter;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.CustomCallback;
import com.gliesereum.karma.data.network.json.record.AllRecordResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.FastSave;
import com.gliesereum.karma.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.CAR_ID;
import static com.gliesereum.karma.util.Constants.RECORD_LIST_ACTIVITY;

//import com.appizona.yehiahd.fastsave.FastSave;

public class RecordListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecordListAdapter recordListAdapter;
    private List<AllRecordResponse> recordsList = new ArrayList<>();
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
        if (!FastSave.getInstance().getString(CAR_ID, "").equals("")) {
            API.getAllRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""))
                    .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<List<AllRecordResponse>>() {
                        @Override
                        public void onSuccessful(Call<List<AllRecordResponse>> call, Response<List<AllRecordResponse>> response) {
                            recordsList = new ArrayList<>();
                            if (response.body() != null && response.body().size() > 0) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    if (response.body().get(i).getTargetId() != null) {
                                        recordsList.add(response.body().get(i));
                                    }
                                }
                                recordListAdapter.setItems(recordsList);
                            }
                            Log.d(TAG, "onSuccessful: ");
                            FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
                        }

                        @Override
                        public void onEmpty(Call<List<AllRecordResponse>> call, Response<List<AllRecordResponse>> response) {
                            splashTextView.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onEmpty: ");
                            FastSave.getInstance().saveBoolean(RECORD_LIST_ACTIVITY, true);
                        }
                    }));
        } else {
            splashTextView.setVisibility(View.VISIBLE);
        }
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
