package com.carrati.mybills.ui.receita

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.Usuario
import com.carrati.mybills.R
import com.carrati.mybills.databinding.ActivityReceitaBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class ReceitaActivity: AppCompatActivity() {
    private lateinit var binding: ActivityReceitaBinding
    private val viewModel: ReceitaViewModel by viewModel()
    private lateinit var usuario: Usuario
    private var selectedPeriod: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_receita)
        binding.viewModel = viewModel

        viewModel.getUsuario()
        viewModel.usuarioLiveData?.observe(this) {
            this.usuario = it
        }

        binding.fabSave.setOnClickListener { saveTransacao() }
        binding.edtData.setOnClickListener { inflateCalendar() }
        binding.switchEfetuado.setOnClickListener {
            binding.switchEfetuado.text = if(binding.switchEfetuado.isChecked) "Pago" else "Não pago"
        }
    }

    private fun saveTransacao(){
        if(binding.edtData.text.toString() == ""){
            binding.tvDataLayout.isErrorEnabled = true
            binding.tvDataLayout.error = "Selecione uma data"
            return
        } else binding.tvDataLayout.isErrorEnabled = false

        if(binding.edtDescr.text.toString() == ""){
            binding.tvDescrLayout.isErrorEnabled = true
            binding.tvDescrLayout.error = "Adicione uma descrição"
            return
        } else binding.tvDescrLayout.isErrorEnabled = false

        if(binding.edtValor.text.toString() == "" || binding.edtValor.text.toString() == "0.00"){
            Toast.makeText(this, "Valor não pode ser zero", Toast.LENGTH_LONG).show()
            return
        }

        val transacao = Transacao().apply {
            this.tipo = "receita"
            this.data = binding.edtData.text.toString()
            this.nome = binding.edtDescr.text.toString()
            this.conta = "itau"//binding.spinnerConta.selectedItem.toString()
            var doubleValue = 0.0
            try {
                doubleValue = java.lang.Double.parseDouble(binding.edtValor.text.toString())
            } catch (e: NumberFormatException) { }
            this.valor = doubleValue
            this.efetuado = binding.switchEfetuado.isChecked
        }

        viewModel.salvarTransacao(usuario.uid!!, selectedPeriod!!, transacao)
        viewModel.receitaLiveData.observe(this) { processResponseSave(it) }
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