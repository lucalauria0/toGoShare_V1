package com.example.carsharingv2.controllers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.carsharingv2.models.ItemsViewModel
import com.example.carsharingv2.R
import com.example.carsharingv2.databinding.CardViewDesignBinding

class MyItemsAdapter (var macchine: MutableList<ItemsViewModel>) : RecyclerView.Adapter<MyItemsAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(itemsViewModel: ItemsViewModel)
    }

    class ViewHolder(binding: CardViewDesignBinding) : RecyclerView.ViewHolder(binding.root) {
        val autoimg = binding.autoimg
        val modello = binding.modello
        val prezzo = binding.prezzo
        val nome_proprietario = binding.nomeProprietario
        val email_proprietario = binding.emailProprietario
        val partenza = binding.partenza
        val destinazione = binding.destinazione
        val posti_disponibili = binding.disponibili
        val orario_partenza = binding.orario1
        val orario_destinazione = binding.orario2
        val card_view = binding.cardViewDesign
    }

    fun setFilteredList(filteredList : MutableList<ItemsViewModel> ) {
        macchine = filteredList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val  view = CardViewDesignBinding.inflate(
             LayoutInflater.from(parent.context), parent, false
         )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = macchine[position]

        holder.autoimg.setImageBitmap(ItemsViewModel.img_auto)
        //holder.img_proprietario.setImageBitmap(ItemsViewModel.img_proprietario)
        holder.prezzo.text = "${ItemsViewModel.prezzo} $"
        holder.modello.text = ItemsViewModel.modello
        holder.nome_proprietario.text = ItemsViewModel.nome
        holder.email_proprietario.text = ItemsViewModel.email
        holder.partenza.text = ItemsViewModel.partenza
        holder.destinazione.text = ItemsViewModel.destinazione
        holder.posti_disponibili.text = "${ItemsViewModel.posti_disponibili} Disponibili"
        holder.orario_partenza.text = ItemsViewModel.orario_partenza
        holder.orario_destinazione.text = ItemsViewModel.orario_destinazione


        //animazione
        holder.card_view.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.recyclerview_animation
        ))
    }

    override fun getItemCount(): Int {
        return macchine.size
    }


}