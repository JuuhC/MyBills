package com.carrati.mybills.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Usuario
import com.carrati.mybills.R
import com.carrati.mybills.databinding.FragmentHomeBinding
import com.carrati.mybills.ui.login.LoginActivity
import com.carrati.mybills.ui.main.IBinding
import com.carrati.mybills.ui.main.ISupportActionBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(), FirebaseAuth.AuthStateListener {

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var calendario: MaterialCalendarView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var usuario: Usuario
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
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

        viewModel.getUsuario()
        viewModel.usuarioLiveData?.observe(viewLifecycleOwner) {
            this.usuario = it
            (requireActivity() as ISupportActionBar).getAB()?.title = "Olá, ${usuario.name}"
        }

        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        binding.btCriarConta.setOnClickListener {
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_nova_conta, null, false)
            launchCustomAlertDialog(view)
        }
    }

    private fun launchCustomAlertDialog(customAlertDialogView: View) {
        val nameTextField = customAlertDialogView.findViewById<TextInputLayout>(R.id.tv_nome_conta)
        val saldoTextField = customAlertDialogView.findViewById<TextInputLayout>(R.id.tv_saldo_inicial)

        // Building the Alert dialog using materialAlertDialogBuilder instance
        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setTitle("Details")
            .setMessage("Enter your basic details")
            .setPositiveButton("Add") { dialog, _ ->
                val name = nameTextField.editText?.text.toString()
                val saldo = saldoTextField.editText?.text.toString()
                
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                Toast.makeText(requireContext(), "Operation cancelled!", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            .show()
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
        if(firebaseAuth.currentUser == null){
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
            else -> NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(
                item
            )
        }
    }
}