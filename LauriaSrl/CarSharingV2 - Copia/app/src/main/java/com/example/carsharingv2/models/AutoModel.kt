package com.example.carsharingv2.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AutoModel(var prezzo:Int, var ref_proprietario:Int, var posti_disponibili:Int, var partenza:String, var destinazione:String, var modello:String, var immagine:Bitmap?, var orario_partenza: String, var orario_destinazione: String) : Parcelable

