package com.threedev.machinetest.utils.network;


import com.threedev.machinetest.home.model.HomeModel;
import com.threedev.machinetest.login.model.LoginModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

//    @POST("/Common?")
//    @FormUrlEncoded
//    Call<RegistrationModel> registration(@Query("vMobileNo") String mobileno,
//                                         @Query("vSubject") String vSubject);


    @POST("login")
    @FormUrlEncoded
    Call<LoginModel> getLogin(@Field("email") String email,
                             @Field("password") String password);
//

    @GET("users?")
    Call<HomeModel> getHomeData(@Query("page") String page);
//
//    @GET("api/DeliveryBoy?")
//    Call<List<ProfileModel>> profileDetails(@Query("vDeliveryBoyId") String vDeliveryBoyId);






}
