package com.carrati.mybills.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Usuario
import com.carrati.mybills.R
import com.carrati.mybills.databinding.FragmentHomeBinding
import com.carrati.mybills.ui.login.LoginActivity
import com.carrati.mybills.ui.main.ISupportActionBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(), FirebaseAuth.AuthStateListener {

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var usuario: Usuario

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        (requireActivity() as ISupportActionBar).getAB()?.elevation = 5F

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