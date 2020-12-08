package com.carrati.mybills.ui.transacoes

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.carrati.mybills.utils.inflate
import com.carrati.domain.models.Transacao
import com.carrati.mybills.R
import kotlinx.android.synthetic.main.item_despesa.view.*

class TransacoesAdapter(private var itens: List<Transacao>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DESPESA = 0
        private const val TYPE_RECEITA = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == TYPE_DESPESA)
            DespesaViewHolder(parent)
        else
            ReceitaViewHolder(parent)
    }

    override fun getItemCount(): Int = itens.size

    override fun getItemViewType(position: Int): Int {
        return if(itens[position].tipo == "despesa"){
            TYPE_DESPESA
        } else {
            TYPE_RECEITA
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == TYPE_DESPESA)
            (holder as DespesaViewHolder).bind(itens[position])
        else
            (holder as ReceitaViewHolder).bind(itens[position])
    }

    inner class DespesaViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(parent.inflate(R.layout.item_despesa)){
        fun bind(item: Transacao) = with(itemView){
            item_nome.text = item.nome
            item_data.text = item.data
            item_valor.text = String.format("R$ %.2f", item.valor)
            item_conta.text = item.conta
            item_efetuado.isVisible = item.efetuado?.not() ?: false
        }
    }

    inner class ReceitaViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(parent.inflate(R.layout.item_receita)){
        fun bind(item: Transacao) = with(itemView){
            item_nome.text = item.nome
            item_data.text = item.data
            item_valor.text = String.format("R$ %.2f", item.valor)
            item_conta.text = item.conta
            item_efetuado.isVisible = item.efetuado?.not() ?: false
        }
    }
}