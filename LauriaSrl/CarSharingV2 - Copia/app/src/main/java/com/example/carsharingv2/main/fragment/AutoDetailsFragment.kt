package com.example.carsharingv2.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.carsharingv2.R
import com.example.carsharingv2.controllers.DBManager
import com.example.carsharingv2.controllers.QueryCallback
import com.example.carsharingv2.databinding.FragmentAutoDetailsBinding
import com.example.carsharingv2.models.ItemsViewModel

class AutoDetailsFragment : Fragment() {

    private lateinit var binding : FragmentAutoDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAutoDetailsBinding.inflate(inflater)

        val itemsViewModel = arguments?.getParcelable<ItemsViewModel>("itemsViewModel")
        if (itemsViewModel != null) {
            binding.autoimmagine.setImageBitmap(itemsViewModel.img_auto)
            binding.orario1.text = itemsViewModel.orario_partenza.toString().trim()
            binding.orario2.text = itemsViewModel.orario_destinazione.toString().trim()
            binding.destinazione.text = itemsViewModel.destinazione.toString().trim()
            binding.email.text = itemsViewModel.email.toString().trim()
            binding.nome.text = itemsViewModel.nome.toString().trim()
            binding.partenza.text = itemsViewModel.partenza.toString().trim()
            binding.postiDisponibili.text = itemsViewModel.posti_disponibili.toString().trim() + " disponbili"
            if(itemsViewModel.posti_disponibili.toString().toInt() > 2) { //cambio opacita dei posti disponibili in ordine decrescente
                //verde
            } else if (itemsViewModel.posti_disponibili.toString().toInt() == 2) {
                binding.postoGreen.setAlpha(0.33F)
                binding.postoYellow.setAlpha(1F)
                binding.postoGreen.setAlpha(0.33F)
            } else if (itemsViewModel.posti_disponibili.toString().toInt() == 1) {
                binding.postoGreen.setAlpha(0.33F)
                binding.postoYellow.setAlpha(0.33F)
                binding.postoGreen.setAlpha(1F)
            } else {
                binding.postoGreen.setAlpha(0.33F)
                binding.postoYellow.setAlpha(0.33F)
                binding.postoGreen.setAlpha(0.33F)
            }
        }

        binding.indietro.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_left_to_right, R.anim.enter_right_to_left)
                .replace(R.id.xContainer, HomeFragment())
                .commit()
        }

        //controlla i posti disponibili e non fai funzionare il bottone se i posti disponibili sono == 0
        //manda un update al db decrementando i posti disponbili
        binding.prenotabutton.setOnClickListener {
            if (itemsViewModel!!.posti_disponibili.toString().toInt() <= 0) {
                Toast.makeText(
                    requireContext(),
                    "POSTI PIENI PER QUESTO ANNUNCIO. MI DISPIACE.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(), "POSTO PRENOTATO.", Toast.LENGTH_SHORT).show()

                DBManager().decrementaPosti(itemsViewModel, object  : QueryCallback {
                    override fun success(response: Any, success: String) {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.xContainer, HomeFragment())
                            .commit()
                        Toast.makeText(requireContext(), "POSTO DECREMENTATO", Toast.LENGTH_SHORT).show()
                    }

                    override fun failure(fail: String) {
                        Toast.makeText(requireContext(), "FALLITO", Toast.LENGTH_SHORT).show()
                    }

                })
            }

        }

        return binding.root
    }


}