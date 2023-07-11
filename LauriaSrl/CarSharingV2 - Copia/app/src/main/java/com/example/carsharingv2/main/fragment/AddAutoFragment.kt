package com.example.carsharingv2.main.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.carsharingv2.R
import com.example.carsharingv2.databinding.FragmentAddAutoBinding
import com.example.carsharingv2.models.AutoModel
import com.example.carsharingv2.models.UtenteModel

class AddAutoFragment : Fragment() {

    lateinit var binding: FragmentAddAutoBinding
    lateinit var utente : UtenteModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddAutoBinding.inflate(inflater)
        utente = requireArguments().getParcelable("utente")!!

        binding.xCondividiButton.setOnClickListener {
            //immagazzino dati nelle editext e li passo con un bundle ad addautomorefragment
            val prezzo = binding.xPrezzo.text.toString().toInt()
            val ref_proprietario = utente.id!!.toInt()
            val posti_disponibili = binding.xPostiDisponibili.text.toString().toInt()
            val partenza = binding.xPartenza.text.toString()
            val destinazione = binding.xDestinazione.text.toString()
            val modello = binding.xModello.text.toString()
            val img_auto : Bitmap? = null

            if(prezzo.toString().isEmpty()) {
                binding.xPrezzo.error = "Prezzo necessario"
                binding.xPrezzo.requestFocus()
                return@setOnClickListener
            }
            if(posti_disponibili.toString().isEmpty()) {
                binding.xPostiDisponibili.error = "Posti necessari"
                binding.xPostiDisponibili.requestFocus()
                return@setOnClickListener
            }
            if(partenza.toString().isEmpty()) {
                binding.xPartenza.error = "Partenza necessaria"
                binding.xPartenza.requestFocus()
                return@setOnClickListener
            }
            if(destinazione.toString().isEmpty()) {
                binding.xDestinazione.error = "Destinazione necessaria"
                binding.xDestinazione.requestFocus()
                return@setOnClickListener
            }

            val auto = AutoModel(
                prezzo,
                ref_proprietario,
                posti_disponibili,
                partenza,
                destinazione,
                modello,
                null,
                "yyyy-mm-gg 00:00:00",
                "yyyy-mm-gg 00:00:00"
            )
            Log.i("AddAutoFragment", auto.toString())

            val bundle = Bundle()
            bundle.putParcelable("automodel", auto)

            val addAutoMoreFragment = AddAutoMoreFragment.newInstance(bundle)

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.enter_right_to_left)
                .replace(R.id.xContainer, addAutoMoreFragment)
                .commit()
        }

        Log.i("utentee", utente.nome)
        Log.i("utentee", utente.cognome)

        // Inflate the layout for this fragment

        return binding.root
    }


}