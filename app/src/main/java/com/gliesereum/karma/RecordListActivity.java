package com.gliesereum.karma;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.adapter.RecordListAdapter;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.record.AllRecordResponse;
import com.gliesereum.karma.util.ErrorHandler;
import com.gliesereum.karma.util.Util;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.SERVICE_TYPE;
import static com.gliesereum.karma.util.Constants.TEST_LOG;
import static com.gliesereum.karma.util.Constants.USER_ID;

public class RecordListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecordListAdapter recordListAdapter;
    private List<AllRecordResponse> recordsList;
    private Map<String, String> carWashNameMap = new HashMap<>();
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private TextView splashTextView;
    private ProgressDialog progressDialog;
    private StompClient mStompClient;
    private NotificationManager notifManager;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
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
        showProgressDialog();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<AllRecordResponse>> call = apiInterface.getAllRecord(FastSave.getInstance().getString(ACCESS_TOKEN, ""), SERVICE_TYPE);
        call.enqueue(new Callback<List<AllRecordResponse>>() {
            @Override
            public void onResponse(Call<List<AllRecordResponse>> call, Response<List<AllRecordResponse>> response) {
                if (response.code() == 200) {
                    recordsList = response.body();
                    if (recordsList != null && recordsList.size() > 0) {
                        recordListAdapter.setItems(recordsList);
                    }

                } else {
                    if (response.code() == 204) {
                        splashTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(RecordListActivity.this, "У вас пока нет записей", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorHandler.showError(jObjError.getInt("code"));
                        } catch (Exception e) {
                            errorHandler.showCustomError(e.getMessage());
                            closeProgressDialog();
                        }
                    }
                }
                closeProgressDialog();
            }

            @Override
            public void onFailure(Call<List<AllRecordResponse>> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
                closeProgressDialog();
            }
        });
    }

    private void initView() {
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

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "Ща сек...", "Ща все сделаю...");
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TEST_LOG, "onDestroy: ");
        super.onDestroy();
        disconnectSocket();
    }
}
