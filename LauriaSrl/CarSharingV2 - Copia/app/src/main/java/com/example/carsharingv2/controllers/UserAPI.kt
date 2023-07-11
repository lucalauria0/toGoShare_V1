package com.example.carsharingv2.controllers

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserAPI{
    //select call
    @POST("postSelect/")
    @FormUrlEncoded
    fun select(@Field("query") query: String): Call<JsonObject>

    //update call
    @POST("postUpdate/")
    @FormUrlEncoded
    fun update(@Field("query") query: String): Call <JsonObject>
    //remove call
    @POST("postRemove/")
    @FormUrlEncoded
    fun remove(@Field("query") query: String): Call <JsonObject>
    //insert call
    @POST("postInsert/")
    @FormUrlEncoded
    fun insert(@Field("query") query: String): Call <JsonObject>
    //get call
    @GET
    fun image(@Url url: String) : Call <ResponseBody>

}