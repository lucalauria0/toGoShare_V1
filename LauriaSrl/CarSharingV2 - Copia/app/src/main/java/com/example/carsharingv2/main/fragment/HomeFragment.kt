package com.example.carsharingv2.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carsharingv2.R
import com.example.carsharingv2.controllers.CustomAdapter
import com.example.carsharingv2.controllers.DBManager
import com.example.carsharingv2.controllers.QueryCallback
import com.example.carsharingv2.databinding.FragmentHomeBinding
import com.example.carsharingv2.models.ItemsViewModel
import com.example.carsharingv2.models.UtenteModel

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var itemsViewModel: ItemsViewModel
    lateinit var adapter : CustomAdapter
    lateinit var utente : UtenteModel
    var pos: Int? = null
    private var list = mutableListOf<ItemsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        utente = requireArguments().getParcelable("utente")!!
        val myItemsFragment = MyItemsFragment()

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        adapter = CustomAdapter(list)
        binding.recyclerview.adapter = adapter


        //passo nel fragment dettagli auto un bundle con i dettagli dell annuncio selezionato
        adapter.setOnItemClickListener(object : CustomAdapter.onItemClickListener {
            override fun onItemClick(itemsViewModel: ItemsViewModel) {
                val autoDetailFragment = AutoDetailsFragment()
                val bundle = Bundle()
                bundle.putParcelable("itemsViewModel", itemsViewModel)
                autoDetailFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_left_to_right, R.anim.enter_right_to_left)
                    .replace(R.id.xContainer, autoDetailFragment)
                    .commit()
            }
        })

        binding.floatingActionButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("utentee", utente)
            loadFragment(myItemsFragment, bundle)
        }

        //aggiorna alla vista del fragment la recyclerview
        DBManager().getItemsView(requireContext(), object : QueryCallback {
            override fun success(response: Any, success: String) {
                Log.i("RECYCLERVIEW", "REFRESHING")
                (binding.recyclerview.adapter as CustomAdapter).macchine = response as MutableList<ItemsViewModel>
                (binding.recyclerview.adapter as CustomAdapter).notifyDataSetChanged()
                list = response as MutableList<ItemsViewModel>
            }

            override fun failure(fail: String) {
                Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
            }
        })

        //searchview
        val searchView : SearchView = binding.searchView
        searchView.clearFocus()
        pos = 1
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(testo: String): Boolean {
                listaFiltrata(testo)
                return true
            }

        })

        //filtri
         val filtri = resources.getStringArray(R.array.filtri)
        val arrayadt = ArrayAdapter(requireContext(), R.layout.dropdown_item, filtri)
        binding.autoCompleteTextView.setAdapter(arrayadt)

        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedValue = arrayadt.getItem(position)
            Log.i("FILTRO SELEZIONATO: ", selectedValue.toString())
            pos = position
        }

        return binding.root
    }

    private fun listaFiltrata(testo: String) {
        val filteredList = mutableListOf<ItemsViewModel>()
        for (item in list) {
            var selected = item.destinazione.toString()
            if (pos == 0) {
                selected = item.partenza.toString()
            } else if (pos == 2) {
                selected = item.modello.toString()
            } else if (pos == 3) {
                selected = item.nome.toString()
            }

            val searchText = testo.toLowerCase()

            if (selected.contains(searchText, ignoreCase = true)) {
                filteredList.add(item)
            }
        }
        if(filteredList.isEmpty()) {
            Toast.makeText(requireContext(), "CAMPO VUOTO", Toast.LENGTH_SHORT).show()
        } else {
            adapter.setFilteredList(filteredList)
        }
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