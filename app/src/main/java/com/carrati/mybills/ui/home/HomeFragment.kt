package com.carrati.mybills.app.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Conta
import com.carrati.domain.models.Response
import com.carrati.domain.models.Usuario
import com.carrati.mybills.app.R
import com.carrati.mybills.app.databinding.FragmentHomeBinding
import com.carrati.mybills.app.ui.login.LoginActivity
import com.carrati.mybills.app.ui.main.IBinding
import com.carrati.mybills.app.ui.main.ISupportActionBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), FirebaseAuth.AuthStateListener {

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding
    private val adapter = ContasAdapter(listOf())

    private lateinit var calendario: MaterialCalendarView
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var usuario: Usuario
    private var selectedPeriod: String? = null
    private var itensCarregados: Int = 0

    private var observerUsuario = Observer<Usuario> { processResponseUsuario(it) }
    private var observerCriarConta = Observer<Response> { processResponseCriarConta(it) }
    private var observerListarConta = Observer<Response> { processResponseListarConta(it) }
    private var observerBalanco = Observer<Response> { processResponseBalanco(it) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.rvList.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        (requireActivity() as ISupportActionBar).getAB()?.elevation = 0F

        calendario = (requireActivity() as IBinding).getFromActivity().calendario

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

        configCalendario()
        viewModel.getUsuario()
        viewModel.usuarioLiveData?.observe(viewLifecycleOwner, observerUsuario)

        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        binding.btCriarConta.setOnClickListener {
            launchCustomAlertDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::usuario.isInitialized) {
            itensCarregados = 0
            viewModel.listarContasLiveData.observe(viewLifecycleOwner, observerListarConta)
            viewModel.carregarContas(usuario.uid!!)

            viewModel.balancoLiveData.observe(viewLifecycleOwner, observerBalanco)
            viewModel.carregarBalanco(usuario.uid!!, selectedPeriod!!)
        }
    }

    private fun configCalendario() {
        val month = String.format("%02d", calendario.currentDate.month)
        selectedPeriod = "${calendario.currentDate.year}-$month"

        calendario.setOnMonthChangedListener { _, date ->

            val monthNew = String.format("%02d", date.month)
            selectedPeriod = "${date.year}-$monthNew"

            viewModel.balancoLiveData.observe(viewLifecycleOwner, observerBalanco)
            viewModel.carregarBalanco(usuario.uid!!, selectedPeriod!!)
        }
    }

    private fun processResponseUsuario(usuario: Usuario) {
        this.usuario = usuario
        (requireActivity() as ISupportActionBar).getAB()?.title = "Olá, ${usuario.name}"
        viewModel.usuarioLiveData?.removeObserver(observerUsuario)

        viewModel.listarContasLiveData.observe(viewLifecycleOwner, observerListarConta)
        viewModel.carregarContas(usuario.uid!!)

        viewModel.balancoLiveData.observe(viewLifecycleOwner, observerBalanco)
        viewModel.carregarBalanco(usuario.uid!!, selectedPeriod!!)
    }

    @SuppressLint("SetTextI18n")
    private fun processResponseBalanco(response: Response?) {
        when (response?.status) {
            Response.Status.LOADING -> {
                binding.llErro.root.isVisible = false
                binding.llLoading.isVisible = true
            }
            Response.Status.SUCCESS -> {
                itensCarregados++
                if (itensCarregados >= 2) binding.llLoading.isVisible = false

                viewModel.balancoLiveData.removeObserver(observerBalanco)
                viewModel.balancoLiveData.value = Response.loading()

                if (response.data is HashMap<*, *>) {
                    val despesas = (response.data as HashMap<String, Double>)["despesas"]
                    val receitas = (response.data as HashMap<String, Double>)["receitas"]

                    binding.tvDespesas.text = String.format("R$%.2f", despesas)
                    binding.tvReceitas.text = String.format("R$%.2f", receitas)
                }
            }
            Response.Status.ERROR -> {
                binding.llErro.root.isVisible = true
                binding.llLoading.isVisible = false
            }
            else -> {}
        }
    }

    @SuppressLint("SetTextI18n")
    private fun processResponseListarConta(response: Response?) {
        when (response?.status) {
            Response.Status.LOADING -> {
                binding.llErro.root.isVisible = false
                binding.llLoading.isVisible = true
            }
            Response.Status.SUCCESS -> {
                itensCarregados++
                if (itensCarregados >= 2) binding.llLoading.isVisible = false

                viewModel.listarContasLiveData.removeObserver(observerListarConta)
                viewModel.listarContasLiveData.value = Response.loading()

                var saldoTotal = 0.0
                if (response.data is List<*>) {
                    val list = (response.data as List<*>).map {
                        it as Conta
                        saldoTotal += it.saldo ?: 0.0
                        it
                    }
                    binding.tvSaldo.text = String.format("R$%.2f", saldoTotal)

                    adapter.updateItens(list)
                }
            }
            Response.Status.ERROR -> {
                binding.llErro.root.isVisible = false
                binding.llLoading.isVisible = true
            }
            else -> {}
        }
    }

    private fun processResponseCriarConta(response: Response?) {
        when (response?.status) {
            Response.Status.LOADING -> {
                binding.llErro.root.isVisible = true
                binding.llLoading.isVisible = false
            }
            Response.Status.SUCCESS -> {
                viewModel.criarContaLiveData.removeObserver(observerCriarConta)
                binding.llLoading.isVisible = true
                Toast.makeText(requireContext(), "Conta salva.", Toast.LENGTH_LONG).show()

                viewModel.listarContasLiveData.observe(viewLifecycleOwner, observerListarConta)
                viewModel.carregarContas(usuario.uid!!)
            }
            Response.Status.ERROR -> {
                binding.llErro.root.isVisible = false
                binding.llLoading.isVisible = true
                Toast.makeText(
                    requireContext(),
                    "Erro ao salvar conta. Tente novamente mais tarde.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {}
        }
    }

    private fun launchCustomAlertDialog() {
        val customAlertDialogView = LayoutInflater.from(requireContext()).inflate(
            R.layout.dialog_nova_conta,
            null,
            false
        )
        val nameTextField = customAlertDialogView.findViewById<TextInputLayout>(R.id.tv_nome_conta)
        val saldoTextField = customAlertDialogView.findViewById<TextInputLayout>(
            R.id.tv_saldo_inicial
        )

        // Building the Alert dialog using materialAlertDialogBuilder instance
        val dialog = materialAlertDialogBuilder.setView(customAlertDialogView)
            .setTitle("Adicionar Conta")
            .setMessage("Preencha os dados da conta:")
            .setPositiveButton("OK") { _, _ -> }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (nameTextField.editText?.text.isNullOrEmpty()) {
                nameTextField.isErrorEnabled = true
                nameTextField.error = "Adicione um nome"
            } else {
                val novaConta = Conta().apply {
                    nome = nameTextField.editText?.text.toString()
                    var doubleValue = 0.0
                    try {
                        doubleValue =
                            java.lang.Double.parseDouble(
                                saldoTextField.editText?.text.toString()
                            )
                    } catch (_: NumberFormatException) { }
                    saldo = doubleValue
                }

                viewModel.criarContaLiveData.observe(viewLifecycleOwner, observerCriarConta)
                viewModel.criarConta(usuario.uid!!, novaConta)

                dialog.dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAPI().getFirebaseAuth().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAPI().getFirebaseAuth().removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        if (firebaseAuth.currentUser == null) {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_button -> {
                viewModel.signOutFirebase()
                googleSignInClient.signOut()
                true
            }
            else -> NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) ||
                super.onOptionsItemSelected(
                    item
                )
        }
    }
}
