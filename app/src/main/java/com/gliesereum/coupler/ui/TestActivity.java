package com.gliesereum.coupler.ui;

import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.github.okdroid.checkablechipview.CheckableChipView;
import com.gliesereum.coupler.R;
import com.gliesereum.coupler.data.network.json.carwash.FilterCarWashBody;
import com.gliesereum.coupler.data.network.json.service.ServiceResponse;
import com.gliesereum.coupler.util.FastSave;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gliesereum.coupler.util.Constants.BUSINESS_CATEGORY_ID;
import static com.gliesereum.coupler.util.Constants.CAR_ID;
import static com.gliesereum.coupler.util.Constants.FILTER_CARWASH_BODY;
import static com.gliesereum.coupler.util.Constants.SERVICE_ID_LIST;
import static com.gliesereum.coupler.util.Constants.SERVICE_LIST;
import static com.gliesereum.coupler.util.Constants.UPDATE_MAP;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView closeImg;
    private LinearLayout filterLianerLayout;
    private List<ServiceResponse> serviceList;
    private MaterialButton acceptBtn;
    private MaterialButton cancelBtn;
    private Set<String> serviceIdList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        setupWindowAnimations();
        initData();
        initView();
        fillList();
    }

    private void fillList() {
        if (serviceList != null) {
            for (int i = 0; i < serviceList.size(); i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 4, 0, 4);
                CheckableChipView checkableChipView = new CheckableChipView(TestActivity.this);
                checkableChipView.setText(serviceList.get(i).getName());
                checkableChipView.setTag(serviceList.get(i).getId());
                if (serviceIdList.contains(serviceList.get(i).getId())) {
                    checkableChipView.setChecked(true);
                }
                checkableChipView.setOutlineCornerRadius(10f);
                checkableChipView.setBackgroundColor(getResources().getColor(R.color.white));
                checkableChipView.setOutlineColor(getResources().getColor(R.color.black));
                checkableChipView.setCheckedColor(getResources().getColor(R.color.accent));
                checkableChipView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckableChipView) v).isChecked()) {
                            serviceIdList.add((String) v.getTag());
                        } else {
                            serviceIdList.remove((String) v.getTag());
                        }
                    }
                });
                filterLianerLayout.addView(checkableChipView, layoutParams);
            }
        }
    }

    private void initData() {
        FastSave.init(getApplicationContext());
        serviceList = FastSave.getInstance().getObjectsList(SERVICE_LIST, ServiceResponse.class);
        if (FastSave.getInstance().getObject(SERVICE_ID_LIST, Set.class) != null) {
            serviceIdList = FastSave.getInstance().getObject(SERVICE_ID_LIST, Set.class);
        } else {
            serviceIdList = new HashSet<>();
        }
    }


    private void initView() {
        closeImg = findViewById(R.id.closeImg);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.no_animation, R.anim.exit);
            }
        });
        filterLianerLayout = findViewById(R.id.filterLianerLayout);
        acceptBtn = findViewById(R.id.acceptBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        acceptBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acceptBtn:
                FastSave.getInstance().saveBoolean(UPDATE_MAP, true);
                FilterCarWashBody filterCarWashBody = new FilterCarWashBody();
                filterCarWashBody.setTargetId(FastSave.getInstance().getString(CAR_ID, null));
                filterCarWashBody.setBusinessCategoryId(FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, ""));
                filterCarWashBody.setServiceIds(new ArrayList<>(serviceIdList));
                FastSave.getInstance().saveObject(SERVICE_ID_LIST, serviceIdList);
                FastSave.getInstance().saveObject(FILTER_CARWASH_BODY, filterCarWashBody);
                finish();
                overridePendingTransition(R.anim.no_animation, R.anim.exit);
                break;
            case R.id.cancelBtn:
                FastSave.getInstance().saveBoolean(UPDATE_MAP, true);
                FastSave.getInstance().saveObject(SERVICE_ID_LIST, new HashSet<>());
                FastSave.getInstance().saveObject(FILTER_CARWASH_BODY, new FilterCarWashBody(FastSave.getInstance().getString(BUSINESS_CATEGORY_ID, "")));
                finish();
                overridePendingTransition(R.anim.no_animation, R.anim.exit);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.exit);
    }
}
