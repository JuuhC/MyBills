package com.carrati.mybills.ui.transferencia

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.Usuario
import com.carrati.mybills.R
import com.carrati.mybills.databinding.ActivityReceitaBinding
import com.carrati.mybills.databinding.ActivityTransferenciaBinding
import com.carrati.mybills.ui.transacoes.TransacoesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class TransferenciaActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTransferenciaBinding
    private val viewModel: TransferenciaViewModel by viewModel()
    private lateinit var usuario: Usuario
    private var selectedPeriod: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transferencia)
        binding.viewModel = viewModel

        viewModel.getUsuario()
        viewModel.usuarioLiveData?.observe(this) {
            this.usuario = it
        }

        binding.fabSave.setOnClickListener { saveTransacao() }
        binding.edtData.setOnClickListener { inflateCalendar() }
    }

    private fun saveTransacao(){
        if(binding.edtData.text.toString() == ""){
            binding.tvDataLayout.isErrorEnabled = true
            binding.tvDataLayout.error = "Selecione uma data"
            return
        } else binding.tvDataLayout.isErrorEnabled = false

        if(binding.spinnerConta1.selectedItem.toString() == binding.spinnerConta2.selectedItem.toString()){
            binding.tvConta2.setTextAppearance(R.style.ErrorTextAppearance)
            binding.tvConta2.text = "Não é possivel transferir para mesma conta"
            return
        }

        if(binding.edtValor.text.toString() == "" || binding.edtValor.text.toString() == "0.00"){
            Toast.makeText(this, "Valor não pode ser zero", Toast.LENGTH_LONG).show()
            return
        }

        val transacao1 = Transacao().apply {
            this.tipo = "despesa"
            this.data = binding.edtData.text.toString()
            this.nome = "Transferencia para ${binding.spinnerConta2.selectedItem}"
            this.conta = binding.spinnerConta1.selectedItem.toString()
            var doubleValue = 0.0
            try {
                doubleValue = java.lang.Double.parseDouble(binding.edtValor.text.toString())
            } catch (e: NumberFormatException) { }
            this.valor = doubleValue
            this.efetuado = true
        }

        val transacao2 = Transacao().apply {
            this.tipo = "receita"
            this.data = binding.edtData.text.toString()
            this.nome = "Transferencia de ${binding.spinnerConta1.selectedItem}"
            this.conta = binding.spinnerConta2.selectedItem.toString()
            var doubleValue = 0.0
            try {
                doubleValue = java.lang.Double.parseDouble(binding.edtValor.text.toString())
            } catch (e: NumberFormatException) { }
            this.valor = doubleValue
            this.efetuado = true
        }

        viewModel.salvarTransacao(usuario.uid!!, selectedPeriod!!, transacao1, transacao2)
        viewModel.transferenciaLiveData.observe(this) { processResponseSave(it) }
    }

    private fun processResponseSave(response: Response?){
        when(response?.status){
            Response.Status.LOADING -> {
                viewModel.loading.set(true)
            }
            Response.Status.SUCCESS -> {
                viewModel.loading.set(false)
                Toast.makeText(this, "Transacao salva.", Toast.LENGTH_LONG).show()
                this.finish()
            }
            Response.Status.ERROR -> {
                viewModel.loading.set(false)
                Toast.makeText(this, "Erro ao salvar transacao. Tente novamente mais tarde.", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    private fun inflateCalendar(){
        val mDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dayNew = if (day < 10) "0${day}" else day.toString()
            val monthNew = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"

            binding.edtData.setText("${year}-${monthNew}-${dayNew}")
            selectedPeriod = "${year}-${monthNew}"
        }

        val defaultDate = LocalDate.now()

        DatePickerDialog(
            this,
            mDateSetListener,
            defaultDate.year, defaultDate.monthValue - 1, defaultDate.dayOfMonth
        ).show()
    }
}