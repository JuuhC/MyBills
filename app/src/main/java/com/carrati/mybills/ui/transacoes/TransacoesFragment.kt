package com.carrati.mybills.ui.transacoes

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.Usuario
import com.carrati.mybills.R
import com.carrati.mybills.databinding.FragmentTransacoesBinding
import com.carrati.mybills.ui.despesa.DespesaActivity
import com.carrati.mybills.ui.main.IBinding
import com.carrati.mybills.ui.main.ISupportActionBar
import com.carrati.mybills.ui.receita.ReceitaActivity
import com.carrati.mybills.utils.MarginItemDecoration
import com.carrati.mybills.utils.OneAnyParameterClickListener
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransacoesFragment : Fragment() {

    private val viewModel: TransacoesViewModel by viewModel()
    private lateinit var binding: FragmentTransacoesBinding
    private lateinit var adapter: TransacoesAdapter
    private lateinit var calendario: MaterialCalendarView

    private lateinit var usuario: Usuario
    private var selectedPeriod: String? = null

    private val observerUsuario = Observer<Usuario> { processResponseUsuario(it) }
    private val observerTransacoes = Observer<Response> { processResponseTransacoes(it) }
    private val observerDeletar = Observer<Response> { processReponseDeletar(it) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTransacoesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        binding.llErro.setOnClickListener { viewModel.getTransacoes(usuario.uid!!, selectedPeriod!!) }

        binding.rvList.addItemDecoration(MarginItemDecoration(24))

        (requireActivity() as ISupportActionBar).getAB()?.elevation = 0F
        setHasOptionsMenu(true)

        calendario = (requireActivity() as IBinding).getFromActivity().calendario
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUsuario()
        viewModel.usuarioLiveData?.observe(viewLifecycleOwner, observerUsuario)

        configCalendar()
        deleteTransaction()
    }

    override fun onResume() {
        super.onResume()
        if(this::usuario.isInitialized){
            viewModel.getTransacoes(usuario.uid!!, selectedPeriod!!)
            viewModel.transacoesLiveData.observe(viewLifecycleOwner, observerTransacoes)
        }
    }

    private fun configCalendar() {
        val month = String.format("%02d", calendario.currentDate.month)
        selectedPeriod = "${calendario.currentDate.year}-$month"

        calendario.setOnMonthChangedListener { _, date ->
            val monthNew = String.format("%02d", date.month)
            selectedPeriod = "${date.year}-$monthNew"

            viewModel.getTransacoes(usuario.uid!!, selectedPeriod!!)
            viewModel.transacoesLiveData.observe(viewLifecycleOwner, observerTransacoes)
        }
    }

    private fun deleteTransaction() {
        val itemTouch = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val dragFlags: Int = ItemTouchHelper.ACTION_STATE_IDLE
                val swipeFlags: Int = ItemTouchHelper.START
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Excluir Transação")
                    .setMessage("Você tem certeza que deseja excluir esta transação?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar") { _, _ ->
                        val position: Int = viewHolder.absoluteAdapterPosition
                        val transacao = adapter.itens[position]

                        viewModel.deletarTransacao(usuario.uid!!, selectedPeriod!!, position, transacao)
                        viewModel.deletarLiveData.observe(viewLifecycleOwner, observerDeletar)
                    }
                    .setNegativeButton("Cancelar") { _, _ ->
                        adapter.notifyDataSetChanged()
                    }
                    .create().show()
            }
        }
        ItemTouchHelper(itemTouch).attachToRecyclerView(binding.rvList)
    }

    private fun processResponseUsuario(usuario: Usuario){
        this.usuario = usuario
        viewModel.usuarioLiveData?.removeObserver(observerUsuario)

        viewModel.getTransacoes(usuario.uid!!, selectedPeriod!!)
        viewModel.transacoesLiveData.observe(viewLifecycleOwner, observerTransacoes)
    }

    private fun processResponseTransacoes(response: Response?){
        when(response?.status){
            Response.Status.LOADING -> {
                viewModel.isError.set(false)
                viewModel.loading.set(true)
            }
            Response.Status.SUCCESS -> {
                viewModel.loading.set(false)
                viewModel.transacoesLiveData.removeObserver(observerTransacoes)
                viewModel.transacoesLiveData.value = Response.loading()

                if(response.data is List<*>) {
                    adapter = TransacoesAdapter(response.data as List<Transacao>, editarDespesaListener, editarReceitaListener)
                    binding.rvList.adapter = adapter
                }
            }
            Response.Status.ERROR -> {
                viewModel.loading.set(false)
                viewModel.isError.set(true)
            }
            else -> {}
        }
    }

    private fun processReponseDeletar(response: Response?){
        when(response?.status){
            Response.Status.LOADING -> {
                viewModel.isError.set(false)
                viewModel.loading.set(true)
            }
            Response.Status.SUCCESS -> {
                viewModel.loading.set(false)
                viewModel.deletarLiveData.removeObserver(observerDeletar)
                viewModel.deletarLiveData.value = Response.loading()

                adapter.removerItem(response.data as Int)
            }
            Response.Status.ERROR -> {
                Log.e("erro delete", response.error.toString())
                viewModel.loading.set(false)
                viewModel.isError.set(true)
            }
            else -> {}
        }
    }

    private val editarDespesaListener = object: OneAnyParameterClickListener {
        override fun onClick(arg: Any) {
            val intent = Intent(requireActivity(), DespesaActivity::class.java)
            intent.putExtra("transacao", arg as Transacao)
            startActivity(intent)
        }
    }

    private val editarReceitaListener = object: OneAnyParameterClickListener {
        override fun onClick(arg: Any) {
            val intent = Intent(requireActivity(), ReceitaActivity::class.java)
            intent.putExtra("transacao", arg as Transacao)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_transacoes_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView

            val searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
            searchPlate.hint = "Pesquisar transação por nome"
            val searchPlateView: View = searchView.findViewById(androidx.appcompat.R.id.search_plate)
            searchPlateView.setBackgroundColor(
                getColor(
                    requireContext(),
                    android.R.color.transparent
                )
            )

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(this@TransacoesFragment::adapter.isInitialized) adapter.filtrarTransacoes(newText ?: "")
                    return false
                }
            })
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_search -> {
                item.expandActionView()
                item.actionView.requestFocus()
            }
            else -> NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(
                item
            )
        }
    }
}