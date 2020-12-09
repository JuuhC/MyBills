package com.carrati.mybills.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carrati.domain.models.Conta
import com.carrati.mybills.R
import kotlinx.android.synthetic.main.item_conta.view.*

class ContasAdapter(private var list: List<Conta>): RecyclerView.Adapter<ContasAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conta, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(this.list[position])
    }

    fun updateItens(itensNovos: List<Conta>){
        this.list = itensNovos
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(item: Conta) = with(itemView){
            itemView.tv_nome_conta.text = item.nome
            itemView.tv_saldo_conta.text = String.format("R$%.2f", item.saldo)
        }
    }
}