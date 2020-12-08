package com.carrati.mybills.ui.main

import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.carrati.mybills.R
import com.carrati.mybills.databinding.ActivityMainBinding
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo


class MainActivity : AppCompatActivity(), ISupportActionBar {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var animatorReceita: YoYo.YoYoString? = null
    private var animatorDespesa: YoYo.YoYoString? = null
    private var animatorTransferencia: YoYo.YoYoString? = null

    private var fabExpanded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.navView.background = null

        val navController = this.findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_transacoes)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        contractFab()
        binding.fab.setOnClickListener{
            if(!fabExpanded) {
                expandFab()
            } else {
                contractFab()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun expandFab(){
        rotateFab(binding.fab, true)

        if(animatorReceita != null && animatorReceita!!.isRunning) animatorReceita?.stop(true)
        if(animatorDespesa != null && animatorDespesa!!.isRunning) animatorDespesa?.stop(true)
        if(animatorTransferencia != null && animatorTransferencia!!.isRunning) animatorTransferencia?.stop(
            true
        )

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

    private fun contractFab(){
        rotateFab(binding.fab, false)

        if(animatorReceita != null && animatorReceita!!.isRunning) animatorReceita?.stop(true)
        if(animatorDespesa != null && animatorDespesa!!.isRunning) animatorDespesa?.stop(true)
        if(animatorTransferencia != null && animatorTransferencia!!.isRunning) animatorTransferencia?.stop(
            true
        )

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

    override fun getAB(): ActionBar? {
        return supportActionBar
    }
}

interface ISupportActionBar {
    fun getAB(): ActionBar?
}