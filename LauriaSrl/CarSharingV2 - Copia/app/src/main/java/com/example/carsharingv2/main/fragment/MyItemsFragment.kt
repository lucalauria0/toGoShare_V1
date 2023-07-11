package com.example.carsharingv2.main.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carsharingv2.R
import com.example.carsharingv2.controllers.CustomAdapter
import com.example.carsharingv2.controllers.DBManager
import com.example.carsharingv2.controllers.MyItemsAdapter
import com.example.carsharingv2.controllers.QueryCallback
import com.example.carsharingv2.databinding.FragmentMyItemsBinding
import com.example.carsharingv2.models.ItemsViewModel
import com.example.carsharingv2.models.UtenteModel


class MyItemsFragment : Fragment() {

    private lateinit var binding: FragmentMyItemsBinding
    private lateinit var itemsViewModel: ItemsViewModel
    private lateinit var adapter : MyItemsAdapter
    private lateinit var utente: UtenteModel
    var list = mutableListOf<ItemsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyItemsBinding.inflate(inflater)

        utente = requireArguments().getParcelable("utentee")!!
        Log.i("MyItemsFragment", utente.id.toString())

        binding.recyclerview2.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyItemsAdapter(list)
        binding.recyclerview2.adapter = adapter

        getMyitems(utente.id.toString().toInt())

        binding.floatingActionButton2.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("utente", utente)
            loadFragment(HomeFragment(), bundle)
        }

        return binding.root
    }


    fun getMyitems(id: Int) {
        DBManager().getMyItemsView(requireContext(), id, object : QueryCallback {
            override fun success(response: Any, success: String) {
                Log.i("MYITEMS", "REFRESHING")
                (binding.recyclerview2.adapter as MyItemsAdapter).macchine = response as MutableList<ItemsViewModel>
                (binding.recyclerview2.adapter as MyItemsAdapter).notifyDataSetChanged()
            }

            override fun failure(fail: String) {
                Toast.makeText(requireContext(), "Errore nel caricamento", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun loadFragment(fragment: Fragment, bundle: Bundle?){
        val transaction = parentFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.enter_left_to_right,
            R.anim.enter_right_to_left
        )
        transaction.replace(R.id.xContainer,fragment)
        fragment.arguments = bundle
        transaction.commit()
    }
}