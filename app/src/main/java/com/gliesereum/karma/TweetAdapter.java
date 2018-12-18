package com.gliesereum.karma;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gliesereum.karma.data.network.json.car.AllCarResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {

    //    private List<CarInfo> carsList = new ArrayList<>();
    private List<AllCarResponse> allCarsList = new ArrayList<>();


    @NonNull
    @Override
    public TweetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_car_info, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TweetViewHolder holder, int position) {
        holder.bind(allCarsList.get(position));
    }

    @Override
    public int getItemCount() {
        return allCarsList.size();
    }

    // Предоставляет прямую ссылку на каждый View-компонент
    // Используется для кэширования View-компонентов и последующего быстрого доступа к ним
    class TweetViewHolder extends RecyclerView.ViewHolder {
        // Ваш ViewHolder должен содержать переменные для всех
        // в процессе работы пользователя со списком
        // View-компонентов, которым вы хотите задавать какие-либо свойства
        private Button button2;
        private TextView brandId;
        private TextView modelId;
        private TextView yearId;
        private TextView userId;
        private TextView registrationNumber;
        private TextView description;
        private TextView interior;
        private TextView carBody;
        private TextView colour;

        // Мы также создали конструктор, который принимает на вход View-компонент строкИ
        // и ищет все дочерние компоненты
        public TweetViewHolder(View itemView) {
            super(itemView);
            brandId = itemView.findViewById(R.id.brandId);
            modelId = itemView.findViewById(R.id.modelId);
            yearId = itemView.findViewById(R.id.yearId);
            userId = itemView.findViewById(R.id.userId);
            registrationNumber = itemView.findViewById(R.id.registrationNumber);
            description = itemView.findViewById(R.id.description);
            interior = itemView.findViewById(R.id.interior);
            carBody = itemView.findViewById(R.id.carBody);
            colour = itemView.findViewById(R.id.colour);

        }

        public void bind(AllCarResponse carInfo) {
            brandId.setText(carInfo.getBrand().getName());
            modelId.setText(carInfo.getModel().getName());
            yearId.setText(carInfo.getYear().getName());
            registrationNumber.setText(carInfo.getRegistrationNumber());
            description.setText(carInfo.getDescription());
            interior.setText(carInfo.getInterior());
            carBody.setText(carInfo.getCarBody());
            colour.setText(carInfo.getCarBody());
        }
    }

    public void setItems(List<AllCarResponse> cars) {
        allCarsList.addAll(cars);
        notifyDataSetChanged();
    }
}
