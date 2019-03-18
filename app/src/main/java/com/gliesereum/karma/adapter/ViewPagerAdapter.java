package com.gliesereum.karma.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;
import com.gliesereum.karma.IconPowerMenuItem;
import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.APIClient;
import com.gliesereum.karma.data.network.APIInterface;
import com.gliesereum.karma.data.network.json.car.AllCarResponse;
import com.gliesereum.karma.data.network.json.car.CarDeleteResponse;
import com.gliesereum.karma.ui.CarListActivity;
import com.gliesereum.karma.util.ErrorHandler;
import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;

import org.json.JSONObject;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;


public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<AllCarResponse> mListData;
    private APIInterface apiInterface;
    private ErrorHandler errorHandler;
    private String TAG = "TAG";

    public ViewPagerAdapter(Context context, List<AllCarResponse> listDate) {
        this.context = context;
        mListData = listDate;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.view_item1, container, false);
        final TextView brandName = view.findViewById(R.id.brandName);
        final TextView modelName = view.findViewById(R.id.modelName);
        final ImageButton editBtn = view.findViewById(R.id.editBtn);
        brandName.setText(mListData.get(position).getBrand().getName());
        modelName.setText(mListData.get(position).getModel().getName());
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomPowerMenu customPowerMenu = new CustomPowerMenu.Builder<>(context, new IconMenuAdapter())
                        .addItem(new IconPowerMenuItem("Удалить", mListData.get(position).getId()))
                        .setOnMenuItemClickListener(onIconMenuItemClickListener)
                        .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                        .setMenuRadius(10f)
                        .setMenuShadow(10f)
                        .build();
                customPowerMenu.showAsDropDown(view);
            }
        });
        container.addView(view);
        return view;
    }

    private OnMenuItemClickListener<IconPowerMenuItem> onIconMenuItemClickListener = new OnMenuItemClickListener<IconPowerMenuItem>() {
        @Override
        public void onItemClick(int position, IconPowerMenuItem item) {
            switch (position) {
                case 0:
                    deleteCar(item.getId());
                    break;
            }
        }
    };

    private void deleteCar(String id) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CarDeleteResponse> call = apiInterface.deleteCar(FastSave.getInstance().getString(ACCESS_TOKEN, ""), id);
        call.enqueue(new Callback<CarDeleteResponse>() {
            @Override
            public void onResponse(Call<CarDeleteResponse> call, Response<CarDeleteResponse> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, "Машина удалена успешно", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, CarListActivity.class));
                } else {
                    if (response.code() == 204) {
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorHandler.showError(jObjError.getInt("code"));
                        } catch (Exception e) {
                            errorHandler.showCustomError(e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CarDeleteResponse> call, Throwable t) {
                errorHandler.showCustomError(t.getMessage());
            }
        });
    }

}
