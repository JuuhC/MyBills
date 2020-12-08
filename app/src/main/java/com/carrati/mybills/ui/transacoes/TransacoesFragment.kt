package com.carrati.mybills.ui.transacoes

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.Usuario
import com.carrati.mybills.R
import com.carrati.mybills.databinding.FragmentTransacoesBinding
import com.carrati.mybills.ui.main.ISupportActionBar
import com.carrati.mybills.utils.MarginItemDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransacoesFragment : Fragment() {

    private val viewModel: TransacoesViewModel by viewModel()
    private lateinit var binding: FragmentTransacoesBinding
    private var adapter = TransacoesAdapter(listOf())

    private lateinit var usuario: Usuario
    private var selectedPeriod: String? = null
    private var filtro: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransacoesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        binding.llErro.setOnClickListener { viewModel.getTransacoes(usuario.uid!!,
            selectedPeriod!!,
            filtro) }

        binding.rvList.addItemDecoration(MarginItemDecoration(24))

        (requireActivity() as ISupportActionBar).getAB()?.elevation = 0F
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUsuario()
        viewModel.usuarioLiveData?.observe(viewLifecycleOwner) {
            this.usuario = it
            viewModel.getTransacoes(usuario.uid!!, selectedPeriod!!, filtro)
            viewModel.transacoesLiveData.observe(viewLifecycleOwner) { processResponseTransacoes(it) }
        }

        configCalendar()
    }

    private fun configCalendar() {
        val month = String.format("%02d", binding.calendario.currentDate.month)
        selectedPeriod = "${binding.calendario.currentDate.year}-$month"

        binding.calendario.setOnMonthChangedListener { _, date ->
            val monthNew = String.format("%02d", date.month)
            selectedPeriod = "${date.year}-$monthNew"

            viewModel.getTransacoes(usuario.uid!!, selectedPeriod!!, filtro)
            viewModel.transacoesLiveData.observe(viewLifecycleOwner) { processResponseTransacoes(it) }
        }
    }

    private fun processResponseTransacoes(response: Response?){
        when(response?.status){
            Response.Status.LOADING -> {
                viewModel.isError.set(false)
                viewModel.loading.set(true)
            }
            Response.Status.SUCCESS -> {
                viewModel.loading.set(false)

                adapter = TransacoesAdapter(response.data as List<Transacao>)
                binding.rvList.adapter = adapter
            }
            Response.Status.ERROR -> {
                viewModel.loading.set(false)
                viewModel.isError.set(true)
            }
            else -> {}
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
                    Toast.makeText(requireContext(), query, Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
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