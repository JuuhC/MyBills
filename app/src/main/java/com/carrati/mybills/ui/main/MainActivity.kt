package com.carrati.mybills.app.ui.main

import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.carrati.mybills.app.R
import com.carrati.mybills.app.databinding.ActivityMainBinding
import com.carrati.mybills.app.ui.despesa.DespesaActivity
import com.carrati.mybills.app.ui.receita.ReceitaActivity
import com.carrati.mybills.app.ui.transferencia.TransferenciaActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class MainActivity : AppCompatActivity(), ISupportActionBar, IBinding {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var animatorReceita: YoYo.YoYoString? = null
    private var animatorDespesa: YoYo.YoYoString? = null
    private var animatorTransferencia: YoYo.YoYoString? = null

    private var fabExpanded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.navView.background = null
        binding.lifecycleOwner = this
        binding.executePendingBindings()

        setContentView(binding.root)

        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_transacoes)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        contractFab()
        binding.fab.setOnClickListener {
            if (!fabExpanded) {
                expandFab()
            } else {
                contractFab()
            }
        }
        configureSubFabs()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun expandFab() {
        rotateFab(binding.fab, true)

        if (animatorReceita != null && animatorReceita!!.isRunning) animatorReceita?.stop(true)
        if (animatorDespesa != null && animatorDespesa!!.isRunning) animatorDespesa?.stop(true)
        if (animatorTransferencia != null && animatorTransferencia!!.isRunning) {
            animatorTransferencia?.stop(
                true
            )
        }

        animatorReceita = YoYo.with(Techniques.ZoomInUp)
            .duration(500)
            .playOn(binding.llReceita)

        animatorDespesa = YoYo.with(Techniques.ZoomInUp)
            .duration(500)
            .playOn(binding.llDespesa)

        animatorTransferencia = YoYo.with(Techniques.ZoomInUp)
            .duration(500)
            .playOn(binding.llTransferencia)

        fabExpanded = true
        binding.clFabMenu.setBackgroundColor(ContextCompat.getColor(this, R.color.black_translucid))
    }

    private fun contractFab() {
        rotateFab(binding.fab, false)

        if (animatorReceita != null && animatorReceita!!.isRunning) animatorReceita?.stop(true)
        if (animatorDespesa != null && animatorDespesa!!.isRunning) animatorDespesa?.stop(true)
        if (animatorTransferencia != null && animatorTransferencia!!.isRunning) {
            animatorTransferencia?.stop(true)
        }

        animatorReceita = YoYo.with(Techniques.ZoomOutDown)
            .duration(500)
            .playOn(binding.llReceita)

        animatorDespesa = YoYo.with(Techniques.ZoomOutDown)
            .duration(500)
            .playOn(binding.llDespesa)

        animatorTransferencia = YoYo.with(Techniques.ZoomOutDown)
            .duration(500)
            .playOn(binding.llTransferencia)

        fabExpanded = false
        binding.clFabMenu.setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )
    }

    private fun rotateFab(v: View, rotate: Boolean): Boolean {
        v.animate().setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {})
            .rotation(if (rotate) 135f else 0f)
        return rotate
    }

    private fun configureSubFabs() {
        binding.fabDespesa.setOnClickListener {
            val intent = Intent(this, DespesaActivity::class.java)
            startActivity(intent)
            contractFab()
            this.onStop()
        }
        binding.fabReceita.setOnClickListener {
            val intent = Intent(this, ReceitaActivity::class.java)
            startActivity(intent)
            contractFab()
            this.onStop()
        }
        binding.fabTransferencia.setOnClickListener {
            val intent = Intent(this, TransferenciaActivity::class.java)
            startActivity(intent)
            contractFab()
            this.onStop()
        }
    }

    override fun getAB(): ActionBar? {
        return supportActionBar
    }

    override fun getFromActivity(): ActivityMainBinding {
        return binding
    }
}

interface ISupportActionBar {
    fun getAB(): ActionBar?
}

interface IBinding {
    fun getFromActivity(): ActivityMainBinding
}
