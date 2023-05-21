package com.carrati.mybills.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carrati.domain.models.Conta
import com.carrati.mybills.app.databinding.ItemContaBinding

class ContasAdapter(
    private var list: List<Conta>
) : RecyclerView.Adapter<ContasAdapter.ContaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ContaViewHolder {
        val binding = ItemContaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContaViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ContaViewHolder, position: Int) {
        holder.bind(this.list[position])
    }

    fun updateItens(itensNovos: List<Conta>) {
        this.list = itensNovos
        notifyDataSetChanged()
    }

    inner class ContaViewHolder(
        private val binding: ItemContaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Conta){
            binding.tvNomeConta.text = item.nome
            binding.tvSaldoConta.text = String.format("R$%.2f", item.saldo)
        }
    }
}
