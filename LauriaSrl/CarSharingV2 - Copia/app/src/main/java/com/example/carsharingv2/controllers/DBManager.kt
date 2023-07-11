package com.example.carsharingv2.controllers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.graphics.scale
import com.example.carsharingv2.models.AutoModel
import com.example.carsharingv2.models.ItemsViewModel
import com.example.carsharingv2.models.UtenteModel
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DBManager
{


    fun esempioquery(username: String, password: String, callback: QueryCallback)
    {
        val query = "SELECT * FROM utente WHERE nick = '${username}' AND pass = '${password}';"

        APIService.retrofit.select(query).enqueue(
            object : Callback<JsonObject>
            {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("DBManager", "onResponse OK")
                    if (response.isSuccessful) {
                        Log.i("DBManager", "DB Success")
                        //query ok
                        val queryset = (response.body()?.get("queryset") as JsonArray)
                        if (queryset.size() > 0)
                        {
                            Log.i("DBManager", "queryset")
                            val list = mutableListOf<UtenteModel>()
                            for (item in queryset)
                            {
                                val utente = Gson().fromJson(item.asJsonObject, UtenteModel::class.java)

                            }


                        }
                        else
                        {
                            Log.i("DBManager", "Not Success")
                            //non c'è nulla

                        }

                    } else { //errore di formattazione
                        Log.i("DBManager", "errore di formattazione")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("DBManager", "onFailure")
                }

            }
        )
    }




    fun getAllAuto() : MutableList<AutoModel>
    {
        val list = mutableListOf<AutoModel>()
        val query = "SELECT * FROM auto ;"

        APIService.retrofit.select(query).enqueue(
            object : Callback<JsonObject>
            {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("DBManager", "onResponse OK")
                    if (response.isSuccessful) {
                        Log.i("DBManager", "DB Success")
                        //query ok
                        val queryset = (response.body()?.get("queryset") as JsonArray)
                        if (queryset.size() > 0)
                        {
                            Log.i("DBManager", "queryset")
                            for (item in queryset)
                            {
                                val auto = Gson().fromJson(item.asJsonObject, AutoModel::class.java)
                                list.add(auto)
                            }

                        }
                        else
                        {
                            Log.i("DBManager", "Not Success")
                            //non c'è nulla

                        }

                    } else { //errore di formattazione
                        Log.i("DBManager", "errore di formattazione")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("DBManager", "onFailure")
                }

            }
        )

        return list
    }

    fun addAuto(auto: AutoModel, callback: QueryCallback)
    {
        val queryTAG = "AddAutoQuery"
        val query = "INSERT INTO auto (prezzo, ref_proprietario, posti_disponibili, partenza, destinazione, modello, img_auto, orario_partenza, orario_destinazione) VALUES ( ${auto.prezzo} , ${auto.ref_proprietario}, ${auto.posti_disponibili} , '${auto.partenza}', '${auto.destinazione}' , '${auto.modello}', '${"media/images/auto_generica.jpg"}', '${auto.orario_partenza}' , '${auto.orario_destinazione}' );"

        APIService.retrofit.insert(query).enqueue(
            object : Callback<JsonObject>
            {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i(queryTAG,"onResponse OK")
                    if (response.isSuccessful) {
                        callback.success(queryTAG, "OK")
                        Log.i(queryTAG, "DB Success")
                        //startmainpage()
                    } else { //errore di formattazione
                        Log.i(queryTAG, "errore di formattazione")
                        callback.failure(queryTAG)
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i(queryTAG, "onFailure")
                    callback.failure(queryTAG)
                }
            }
        )
    }

    fun getImage(context: Context, jsonObject: JsonObject, img:String, callback: QueryCallback)
    {
        val url: String = jsonObject.get(img).asString
        Log.d("Picture", url)

        APIService.retrofit.image(url).enqueue(
            object : Callback<ResponseBody>
            {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
                {
                    if(response.isSuccessful)
                    {
                        if (response.body() != null)
                        {
                            val profilepicture = BitmapFactory.decodeStream(response.body()?.byteStream())
                            Log.d("Picture", profilepicture.toString())
                            callback.success(profilepicture, "OK")
                        }
                        else
                        {
                            callback.failure("ERROR ONRESPOSE BODY NULL")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable)
                {
                    callback.failure("ERROR ONFAILURE")
                }

            }
        )
    }





    fun getItemsView(context: Context, callback: QueryCallback) //per gli annunci nella home
    {
        val queryTAG = "GetItemsViewQuery"
        val query = "SELECT A.prezzo, A.ref_proprietario, A.posti_disponibili, A.partenza, A.destinazione, A.modello, A.img_auto, U.immagine, U.nome, U.email, A.orario_partenza, A.orario_destinazione FROM utente U, auto A WHERE U.id=A.ref_proprietario;"
        Log.e("login", "getutente")
        APIService.retrofit.select(query).enqueue(
            object : Callback<JsonObject>
            {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>)
                {
                    if(response.isSuccessful)
                    {
                        val queryset = (response.body()?.get("queryset") as JsonArray)
                        if (queryset.size() > 0)  {

                            Log.i(queryTAG, queryset.size().toString())
                            var list = mutableListOf<ItemsViewModel>()
                            for (item in queryset)
                            {
                                Log.i(queryTAG, item.toString())
                                getImage(context, item.asJsonObject, "img_auto", object :
                                    QueryCallback {
                                    override fun success(response: Any, success: String) {
                                        item.asJsonObject.remove("img_auto")
                                        item.asJsonObject.add("img_auto", null)
                                        val autoviewmodel = Gson().fromJson(item.asJsonObject, ItemsViewModel::class.java)
                                        autoviewmodel.img_auto = (response as Bitmap).scale(200, 150)
                                        list.add(autoviewmodel)
                                        Log.i(queryTAG, autoviewmodel.email)
                                        callback.success(list, "OKOK")
                                    }
                                    override fun failure(fail: String) {
                                        item.asJsonObject.remove("img_auto")
                                        item.asJsonObject.add("img_auto", null)
                                        val autoviewmodel = Gson().fromJson(item.asJsonObject, ItemsViewModel::class.java)
                                        autoviewmodel.img_auto = null
                                        list.add((autoviewmodel))
                                        callback.success(list, fail)
                                    }
                                } )


                            }

                        }
                        else//utente non trovato
                        {
                            callback.failure("ERRORE")
                            Log.e("login", "non trovato")
                        }
                    }
                    else//errore di formattazione
                    {
                        callback.failure("ERRORE")
                        Log.e("login", "formattazione")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable)//errore di rete
                {
                    callback.failure("ERRORE")
                }

            }
        )
    }






    fun getUtente(context: Context, username: String, password: String, callback: QueryCallback)
    {
        val query = "SELECT id, email, pass, nome, cognome, immagine FROM utente WHERE email = '${username}' AND pass = '${password}';"
        Log.e("login", "getutente")
        APIService.retrofit.select(query).enqueue(
            object : Callback<JsonObject>
            {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>)
                {
                    if(response.isSuccessful)
                    {
                        if((response.body()?.get("queryset") as JsonArray).size() == 1)//utente torvato
                        {
                            val utenteJson = (response.body()?.get("queryset") as JsonArray).get(0).asJsonObject
                            Log.e("login", "sii")

                            getImage(context, utenteJson, "immagine", object : QueryCallback
                            {
                                override fun success(response: Any, message: String)
                                {
                                    Log.e("getimage", "ok")
                                    utenteJson.remove("immagine")
                                    utenteJson.add("immagine", null)
                                    val utente = Gson().fromJson(utenteJson, UtenteModel::class.java)
                                    Log.i("utente", utente.email)
                                    Log.i("utente", utente.pass)
                                    utente.immagine = (response as Bitmap).scale(200, 200)
                                    Log.i("utente", utente.immagine.toString())
                                    callback.success(utente, "utente")
                                }

                                override fun failure(fail: String)
                                {
                                    callback.failure(fail)
                                    Log.e("getimage", fail)
                                }
                            })
                        }
                        else//utente non trovato
                        {
                            callback.failure("ERRORE")
                            Log.e("login", "non trovato")
                        }
                    }
                    else//errore di formattazione
                    {
                        callback.failure("ERRORE")
                        Log.e("login", "formattazione")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable)//errore di rete
                {
                    callback.failure("ERRORE")
                }

            }
        )
    }



    fun login(username: String, password: String, callback: QueryCallback)
    {
        val query = "SELECT * FROM utente WHERE nick = '${username}' AND pass = '${password}';"

        APIService.retrofit.select(query).enqueue(
            object : Callback<JsonObject>
            {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("DBmanager", "onResponse")
                    if (response.isSuccessful) {
                            //c'è qualcosa
                            Log.i("DBManager", "DB Successs")
                            //query ok
                            var utenteJson = (response.body()?.get("queryset") as JsonArray).get(0).asJsonObject
                            var utente = Gson().fromJson(utenteJson, UtenteModel::class.java)
                            Log.i("utente", utente.email)
                            Log.i("utente", utente.pass)
                            if((response.body()?.get("queryset") as JsonArray).size() == 1) {
                                Log.e("login", "ok")
                                callback.success(utente, "response ok")
                            }
                            else
                            {
                                Log.e("login", "Errata")
                                callback.failure("errore")
                            }

                    } else { //errore di formattazione
                        Log.i("DBManager", "errore")
                        callback.failure("errore")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("DBManager", "onFailure")
                    callback.failure("errore")
                }

            }
        )
    }





    fun signup(utenteModel: UtenteModel, callback: QueryCallback)
    {
        val query = "INSERT INTO utente (email, pass, nome, cognome, immagine) VALUES ('${utenteModel.email}', '${utenteModel.pass}', '${utenteModel.nome}', '${utenteModel.cognome}', '${"media/images/avatar.jpg"}');"
        Log.i("query", query)

        APIService.retrofit.insert(query).enqueue(
            object : Callback<JsonObject>
            {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("SignUpActivity", "onResponse OK")
                    if (response.isSuccessful) {
                        Log.i("SignUpActivity", "DB Success")
                        callback.success("OK", "OK")
                    } else { //errore di formattazione
                        Log.i("SignUpActivity", "errore di formattazione")
                        callback.failure("ERRORE DI FORMATTAZIONE")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("SignUpActivity", "onFailure")
                    callback.failure("ERRORE ONFAILURE")
                }
            }
        )
    }





    fun decrementaPosti(itemsViewModel: ItemsViewModel, callback: QueryCallback)
    {
        val query = "UPDATE auto SET posti_disponibili = ${--itemsViewModel.posti_disponibili} WHERE id = ${itemsViewModel.ref_proprietario};"
        val queryTAG = "DecermentaPostiQuery"

        APIService.retrofit.update(query).enqueue(
            object : Callback<JsonObject>
            {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i(queryTAG, "onResponse OK")
                    if (response.isSuccessful) {
                        Log.i(queryTAG, "DB Success")
                        callback.success("OK", "OK")
                    } else { //errore di formattazione
                        Log.i(queryTAG, "errore di formattazione")
                        callback.failure("ERRORE DI FORMATTAZIONE")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i(queryTAG, "onFailure")
                    callback.failure("ERRORE ONFAILURE")
                }
            }
        )
    }





    fun getMyItemsView(context: Context, id:Int, callback: QueryCallback) //per gli annunci nella home
    {
        val queryTAG = "GetMyItemsViewQuery"
        val query = "SELECT A.prezzo, A.ref_proprietario, A.posti_disponibili, A.partenza, A.destinazione, A.modello, A.img_auto, U.immagine, U.nome, U.email, A.orario_partenza, A.orario_destinazione FROM utente U, auto A WHERE U.id=A.ref_proprietario AND U.id=${id};"
        Log.e("login", "getutente")
        APIService.retrofit.select(query).enqueue(
            object : Callback<JsonObject>
            {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>)
                {
                    if(response.isSuccessful)
                    {
                        val queryset = (response.body()?.get("queryset") as JsonArray)
                        if (queryset.size() > 0)  {

                            Log.i(queryTAG, queryset.size().toString())
                            var list = mutableListOf<ItemsViewModel>()
                            for (item in queryset)
                            {
                                Log.i(queryTAG, item.toString())
                                getImage(context, item.asJsonObject, "img_auto", object :
                                    QueryCallback {
                                    override fun success(response: Any, success: String) {
                                        item.asJsonObject.remove("img_auto")
                                        item.asJsonObject.add("img_auto", null)
                                        val autoviewmodel = Gson().fromJson(item.asJsonObject, ItemsViewModel::class.java)
                                        autoviewmodel.img_auto = (response as Bitmap).scale(200, 150)
                                        list.add(autoviewmodel)
                                        Log.i(queryTAG, autoviewmodel.email)
                                        callback.success(list, "OKOK")
                                    }
                                    override fun failure(fail: String) {
                                        item.asJsonObject.remove("img_auto")
                                        item.asJsonObject.add("img_auto", null)
                                        val autoviewmodel = Gson().fromJson(item.asJsonObject, ItemsViewModel::class.java)
                                        autoviewmodel.img_auto = null
                                        list.add((autoviewmodel))
                                        callback.success(list, fail)
                                    }
                                } )


                            }

                        }
                        else//utente non trovato
                        {
                            callback.failure("ERRORE")
                            Log.e("login", "non trovato")
                        }
                    }
                    else//errore di formattazione
                    {
                        callback.failure("ERRORE")
                        Log.e("login", "formattazione")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable)//errore di rete
                {
                    callback.failure("ERRORE")
                }

            }
        )
    }

}