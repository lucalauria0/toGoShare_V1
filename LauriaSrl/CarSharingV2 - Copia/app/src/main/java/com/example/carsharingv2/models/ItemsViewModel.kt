package com.example.carsharingv2.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemsViewModel(var prezzo:Int, var ref_proprietario:Int, var posti_disponibili:Int, var partenza:String, var destinazione:String, var modello:String, var img_auto:Bitmap?, var img_proprietario:Bitmap?, var nome:String, var email:String, var orario_partenza:String, var orario_destinazione:String) : Parcelable

