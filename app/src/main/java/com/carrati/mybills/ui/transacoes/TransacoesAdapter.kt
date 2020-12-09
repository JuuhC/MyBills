package com.carrati.mybills.ui.transacoes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.carrati.mybills.utils.inflate
import com.carrati.domain.models.Transacao
import com.carrati.mybills.R
import com.carrati.mybills.utils.OneAnyParameterClickListener
import kotlinx.android.synthetic.main.item_despesa.view.*

class TransacoesAdapter(
    var itens: List<Transacao>,
    private var editarDespesaListener: OneAnyParameterClickListener,
    private var editarReceitaListener: OneAnyParameterClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DESPESA = 0
        private const val TYPE_RECEITA = 1
    }

    private var listaFiltrada = itens.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == TYPE_DESPESA)
            DespesaViewHolder(parent)
        else
            ReceitaViewHolder(parent)
    }

    override fun getItemCount(): Int = listaFiltrada.size

    override fun getItemViewType(position: Int): Int {
        return if(listaFiltrada[position].tipo == "despesa"){
            TYPE_DESPESA
        } else {
            TYPE_RECEITA
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listaFiltrada[position]

        if(getItemViewType(position) == TYPE_DESPESA) {
            (holder as DespesaViewHolder).bind(item)

            if( item.nome!!.contains("transferencia", ignoreCase = true) ) {
                holder.itemView.isClickable = false
            } else {
                holder.itemView.isClickable = true
                holder.itemView.setOnClickListener{ editarDespesaListener.onClick(item) }
            }
        } else {
            (holder as ReceitaViewHolder).bind(item)

            if( item.nome!!.contains("transferencia", ignoreCase = true) ) {
                holder.itemView.isClickable = false
            } else {
                holder.itemView.isClickable = true
                holder.itemView.setOnClickListener{ editarReceitaListener.onClick(item) }
            }
        }
    }

    fun removerItem(position: Int){
        val list = itens.toMutableList()
        list.removeAt(position)
        itens = list.toList()
        notifyItemRemoved(position)
    }

    fun filtrarTransacoes(filtro: String){
        listaFiltrada = itens.toMutableList()
        if(filtro.isNotEmpty()) listaFiltrada.removeIf { it.nome != filtro }
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