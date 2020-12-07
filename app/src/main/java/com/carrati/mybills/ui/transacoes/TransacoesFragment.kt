package com.carrati.mybills.ui.transacoes

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.carrati.mybills.R
import com.carrati.mybills.databinding.FragmentHomeBinding
import com.carrati.mybills.databinding.FragmentTransacoesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransacoesFragment : Fragment() {

    private val viewModel: TransacoesViewModel by viewModel()
    private lateinit var binding: FragmentTransacoesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentTransacoesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_transacoes_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(
                item
            )
        }
    }
}