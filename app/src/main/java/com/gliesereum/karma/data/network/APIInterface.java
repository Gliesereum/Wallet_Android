package com.gliesereum.karma.data.network;

import com.gliesereum.karma.data.network.json.car.AllCarResponse;
import com.gliesereum.karma.data.network.json.car.BrandResponse;
import com.gliesereum.karma.data.network.json.car.CarInfo;
import com.gliesereum.karma.data.network.json.carwash.AllCarWashResponse;
import com.gliesereum.karma.data.network.json.code.CodeResponse;
import com.gliesereum.karma.data.network.json.code.SigninBody;
import com.gliesereum.karma.data.network.json.code.SignupBody;
import com.gliesereum.karma.data.network.json.user.User;
import com.gliesereum.karma.data.network.json.user.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("account/v1/phone/code")
    Call<CodeResponse> getPhoneCode(@Query("phone") String phone, @Query("isNew") String isNew);

    @POST("account/v1/auth/signin")
    Call<UserResponse> signIn(@Body SigninBody signinBody);

    @POST("account/v1/auth/signup")
    Call<UserResponse> signUp(@Body SignupBody signinBody);

    @PUT("account/v1/user")
    Call<User> updateUser(@Header("Authorization") String accessToken, @Body User user);

    @GET("karma/v1/car/brands")
    Call<List<BrandResponse>> getBrands();

    @GET("karma/v1/car/models/by/brand/{brandId}")
    Call<List<BrandResponse>> getModels(@Path("brandId") String id);

    @GET("karma/v1/car/years")
    Call<List<BrandResponse>> getYears();

    @GET("karma/v1/carwash")
    Call<List<AllCarWashResponse>> getAllCarWash();

    @GET("karma/v1/carwash/{carwashId}")
    Call<AllCarWashResponse> getCarWash(@Path("carwashId") String id);

    @GET("karma/v1/car/user")
    Call<List<AllCarResponse>> getAllCars(@Header("Authorization") String accessToken);

    @POST("karma/v1/car")
    Call<CarInfo> addCar(@Header("Authorization") String accessToken, @Body CarInfo object);


}
