package com.example.carsharingv2.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UtenteModel(var id:Int?, var email: String, var pass: String, var nome:String, var cognome:String, var immagine:Bitmap?) : Parcelable

