package com.example.carsharingv2.main.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.carsharingv2.databinding.FragmentAccountBinding
import com.example.carsharingv2.models.UtenteModel

class AccountFragment : Fragment() {

    lateinit var utente : UtenteModel
    lateinit var binding : FragmentAccountBinding
    private lateinit var icon: ImageView
    private val TAG = "AccountFragment"

    //comportamento di default
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){ //permesso per fotocamera controlla se ci sono
            isGranted ->
        if(isGranted){
            Log.i(TAG, "Permissi OK")
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        } else {
            Log.i(TAG, "NON OK")
        }
    }

    //controlla risultato della fotocamera, converti in bitmap e setta dentro l'imageview
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val res = result.data
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            icon.setImageBitmap(imageBitmap)
            Log.i(TAG, "OK")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)
        icon = binding.xAccountIcon

        //immagazinamento dati utente
        utente = requireArguments().getParcelable("utente")!!
        binding.xAccountIcon.setImageBitmap(utente.immagine)
        binding.xEmail.text = utente.email
        binding.xPassword.text = utente.pass
        binding.xName.text = utente.nome
        binding.xSurname.text = utente.cognome


        binding.xAccountButton.setOnClickListener {
            setupPermission()
        }



        return binding.root
    }

    //controlla permessi fotocamera, usa startforresult, se no usa requestpermission e la finestra di dialogo
    private fun setupPermission(){
        when{
            ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(requireContext(), "Permissi ricevuti.", Toast.LENGTH_SHORT).show()
                startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE))
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                val dialogBuilder = AlertDialog.Builder(this.context)
                dialogBuilder.setMessage("Per favore acconsenti ai permessi della fotocamera")
                dialogBuilder.setTitle("Permessi fotocamera")
                dialogBuilder.setPositiveButton("Scatta!"){
                        _, _ ->
                    requestPermission.launch(android.Manifest.permission.CAMERA)
                }
                val dialog = dialogBuilder.create()
                dialog.show()
            }
            else -> {
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

}