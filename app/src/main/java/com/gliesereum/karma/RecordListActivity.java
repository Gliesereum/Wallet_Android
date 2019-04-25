package com.gliesereum.karma;

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

import com.gliesereum.karma.adapter.RecordListAdapter;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.CustomCallback;
import com.gliesereum.karma.data.network.json.record.AllRecordResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.FastSave;
import com.gliesereum.karma.util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.BUSINESS_CATEGORY_ID;
import static com.gliesereum.karma.util.Constants.CAR_ID;
import static com.gliesereum.karma.util.Constants.TEST_LOG;
import static com.gliesereum.karma.util.Constants.USER_ID;

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
    private StompClient mStompClient;
    private NotificationManager notifManager;
    private CustomCallback customCallback;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
//        Bundle bundle = getIntent().getExtras();
//        if(bundle.getString("action").equals("SHOW_DETAILS")) /*This indicates activity is launched from notification, not directly*/
//        {
//            //Data retrieved from notification payload send
////            String filed1 = bundle.getString("field1");
////            String filed2 = bundle.getString("field2");
//            Log.d("TAG_NOTIF", "onCreate: filed1");
//            Log.d("TAG_NOTIF", "onCreate: ");
//        }
//        FastSave.init(getApplicationContext());
        initView();
        getAllRecord();
        connectSocket();
    }

    private void reconnectSocket() {
        if (mStompClient != null && mStompClient.isConnected()) {
            mStompClient.disconnect();
        }
        Log.d(TEST_LOG, "reconnectSocket: ");
        connectSocket();
    }

    private void disconnectSocket() {
        if (mStompClient != null && mStompClient.isConnected()) {
            Log.d(TEST_LOG, "disconnectSocket: ");
            mStompClient.disconnect();
        }
    }

    private void connectSocket() {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "wss://dev.gliesereum.com/socket/websocket-app");
        mStompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d(TEST_LOG, "connectSocket: connection OPENED");
                    break;
                case ERROR:
                    Log.e(TEST_LOG, "connectSocket: Error", lifecycleEvent.getException());
                    reconnectSocket();
                    break;
                case CLOSED:
                    Log.d(TEST_LOG, "connectSocket: connection CLOSED");
                    break;
            }
        });
        mStompClient.connect();
        List<StompHeader> stompHeaders = new ArrayList<>();
        stompHeaders.add(new StompHeader("Authorization", FastSave.getInstance().getString(ACCESS_TOKEN, "")));
        mStompClient.topic("/topic/karma.userRecord." + FastSave.getInstance().getString(USER_ID, ""), stompHeaders).subscribe(topicMessage -> {
            try {
                Log.d(TEST_LOG, "jsonObject " + topicMessage.getPayload());
                AllRecordResponse jsonJavaRootObject = new Gson().fromJson(topicMessage.getPayload(), AllRecordResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TEST_LOG, "run: ");
                        for (int i = 0; i < recordsList.size(); i++) {
                            if (recordsList.get(i).getId().equals(jsonJavaRootObject.getId())) {
                                Log.d(TEST_LOG, "run: ");
                                recordsList.set(i, jsonJavaRootObject);
                                recordListAdapter = new RecordListAdapter(RecordListActivity.this);
                                recyclerView.setAdapter(recordListAdapter);
                                recordListAdapter.setItems(recordsList);
                            }
                        }

                    }
                });
            } catch (Exception e) {
                Log.e(TEST_LOG, "connectSocket: " + e.getMessage());
            }
        });
    }



    private void getAllRecord() {
        if (!FastSave.getInstance().getString(CAR_ID, "").equals("")) {
            API.getAllRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, ""))
                    .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<List<AllRecordResponse>>() {
                        @Override
                        public void onSuccessful(Call<List<AllRecordResponse>> call, Response<List<AllRecordResponse>> response) {
                            recordsList = response.body();
                            if (recordsList != null && recordsList.size() > 0) {
                                recordListAdapter.setItems(recordsList);
                            }
                        }

                        @Override
                        public void onEmpty(Call<List<AllRecordResponse>> call, Response<List<AllRecordResponse>> response) {
                            splashTextView.setVisibility(View.VISIBLE);
                        }
                    }));
        } else {
            splashTextView.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
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
    protected void onDestroy() {
        Log.d(TEST_LOG, "onDestroy: ");
        super.onDestroy();
        disconnectSocket();
    }
}
