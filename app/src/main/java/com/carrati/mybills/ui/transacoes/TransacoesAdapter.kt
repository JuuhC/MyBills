package com.carrati.mybills.ui.transacoes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.carrati.domain.models.Transacao
import com.carrati.mybills.databinding.ItemDespesaBinding
import com.carrati.mybills.databinding.ItemReceitaBinding
import com.carrati.mybills.utils.OneAnyParameterClickListener

class TransacoesAdapter(
    var itens: List<Transacao>,
    private var editarDespesaListener: OneAnyParameterClickListener,
    private var editarReceitaListener: OneAnyParameterClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DESPESA = 0
        private const val TYPE_RECEITA = 1
    }

    private var listaFiltrada = itens.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_DESPESA) {
            val binding = ItemDespesaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            DespesaViewHolder(binding)
        } else {
            val binding = ItemReceitaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceitaViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = listaFiltrada.size

    override fun getItemViewType(position: Int): Int {
        return if (listaFiltrada[position].tipo == "despesa") {
            TYPE_DESPESA
        } else {
            TYPE_RECEITA
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listaFiltrada[position]

        if (getItemViewType(position) == TYPE_DESPESA) {
            (holder as DespesaViewHolder).bind(item)

            if (item.nome!!.contains("transferencia", ignoreCase = true)) {
                holder.itemView.isClickable = false
            } else {
                holder.itemView.isClickable = true
                holder.itemView.setOnClickListener { editarDespesaListener.onClick(item) }
            }
        } else {
            (holder as ReceitaViewHolder).bind(item)

            if (item.nome!!.contains("transferencia", ignoreCase = true)) {
                holder.itemView.isClickable = false
            } else {
                holder.itemView.isClickable = true
                holder.itemView.setOnClickListener { editarReceitaListener.onClick(item) }
            }
        }
    }

    fun removerItem(transacao: Transacao) {
        val list = itens.toMutableList()
        list.removeIf { it.id == transacao.id }
        itens = list.toList()
        listaFiltrada.removeIf { it.id == transacao.id }
        notifyDataSetChanged()
    }

    fun filtrarTransacoes(filtro: String) {
        listaFiltrada = itens.toMutableList()
        if (filtro.isNotEmpty()) listaFiltrada.removeIf { !it.nome!!.contains(filtro) }
    }

    inner class DespesaViewHolder(
        private val binding: ItemDespesaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Transacao) {
            binding.itemNome.text = item.nome
            binding.itemData.text = item.data
            binding.itemValor.text = String.format("R$ %.2f", item.valor)
            binding.itemConta.text = item.conta
            binding.itemEfetuado.isVisible = item.efetuado?.not() ?: false
        }
    }

    inner class ReceitaViewHolder(
        private val binding: ItemReceitaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Transacao) {
            binding.itemNome.text = item.nome
            binding.itemData.text = item.data
            binding.itemValor.text = String.format("R$ %.2f", item.valor)
            binding.itemConta.text = item.conta
            binding.itemEfetuado.isVisible = item.efetuado?.not() ?: false
        }
    }
}
