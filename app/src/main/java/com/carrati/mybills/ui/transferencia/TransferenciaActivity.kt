package com.carrati.mybills.app.ui.transferencia

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.carrati.domain.models.Conta
import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.Usuario
import com.carrati.mybills.app.R
import com.carrati.mybills.app.databinding.ActivityTransferenciaBinding
import java.time.LocalDate
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferenciaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransferenciaBinding
    private val viewModel: TransferenciaViewModel by viewModel()
    private lateinit var usuario: Usuario
    private var selectedPeriod: String? = null

    private var observerListarConta = Observer<Response> { processResponseListarConta(it) }
    private var observerSalvar = Observer<Response> { processResponseSave(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transferencia)
        binding.viewModel = viewModel

        viewModel.getUsuario()
        viewModel.usuarioLiveData?.observe(this) {
            this.usuario = it

            viewModel.listarContasLiveData.observe(this, observerListarConta)
            viewModel.carregarContas(it.uid!!)
        }

        binding.fabSave.setOnClickListener { saveTransacao() }
        binding.edtData.setOnClickListener { inflateCalendar() }
        binding.edtValor.requestFocus()
    }

    private fun saveTransacao() {
        if (binding.edtData.text.toString().isBlank()) {
            binding.tvDataLayout.error = "Selecione uma data"
            return
        } else binding.tvDataLayout.error = null

        if (binding.spinnerConta1.text.toString().isBlank()) {
            binding.spinnerContaLayout1.error = "Campo obrigatório"
            return
        } else binding.spinnerContaLayout1.error = null

        if (binding.spinnerConta2.text.toString().isBlank()) {
            binding.spinnerContaLayout2.error = "Campo obrigatório"
            return
        } else binding.spinnerContaLayout2.error = null

        if (binding.spinnerConta1.text.toString() == binding.spinnerConta2.text.toString()) {
            binding.spinnerContaLayout2.error = "Não é possivel transferir para mesma conta"
            return
        } else binding.spinnerContaLayout2.error = null

        if (binding.edtValor.text.toString() == "" || binding.edtValor.text.toString() == "0.00") {
            Toast.makeText(this, "Valor não pode ser zero", Toast.LENGTH_LONG).show()
            return
        }

        val transacao1 = Transacao().apply {
            this.tipo = "despesa"
            this.data = binding.edtData.text.toString()
            this.nome = "Transferencia para ${binding.spinnerConta2.text}"
            this.conta = binding.spinnerConta1.text.toString()
            var doubleValue = 0.0
            try {
                doubleValue = java.lang.Double.parseDouble(binding.edtValor.text.toString())
            } catch (_: NumberFormatException) { }
            this.valor = doubleValue
            this.efetuado = true
        }

        val transacao2 = Transacao().apply {
            this.tipo = "receita"
            this.data = binding.edtData.text.toString()
            this.nome = "Transferencia de ${binding.spinnerConta1.text}"
            this.conta = binding.spinnerConta2.text.toString()
            var doubleValue = 0.0
            try {
                doubleValue = java.lang.Double.parseDouble(binding.edtValor.text.toString())
            } catch (_: NumberFormatException) { }
            this.valor = doubleValue
            this.efetuado = true
        }

        //viewModel.salvarTransacao(usuario.uid!!, selectedPeriod!!, transacao1, transacao2)
        //viewModel.transferenciaLiveData.observe(this, observerSalvar)
    }

    private fun processResponseSave(response: Response?) {
        when (response?.status) {
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
                Toast.makeText(
                    this,
                    "Erro ao salvar transacao. Tente novamente mais tarde.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {}
        }
    }

    private fun processResponseListarConta(response: Response?) {
        when (response?.status) {
            Response.Status.LOADING -> {
                viewModel.loading.set(true)
            }
            Response.Status.SUCCESS -> {
                viewModel.loading.set(false)

                viewModel.listarContasLiveData.removeObserver(observerListarConta)
                viewModel.listarContasLiveData.value = Response.loading()

                if (response.data is List<*>) {
                    val list = (response.data as List<*>).map { (it as Conta).nome }
                    Log.e("listarConta", "entrou")
                    Log.e("listarConta", list.toString())

                    val spinnerAdapter = object : ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        list
                    ) {
                        private val filter_that_does_nothing = object : Filter() {
                            override fun performFiltering(constraint: CharSequence?): FilterResults {
                                val results = FilterResults()
                                results.values = list
                                results.count = list.size
                                return results
                            }
                            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                                notifyDataSetChanged()
                            }
                        }

                        override fun getFilter(): Filter {
                            return filter_that_does_nothing
                        }
                    }

                    binding.spinnerConta1.setAdapter(spinnerAdapter)
                    binding.spinnerConta2.setAdapter(spinnerAdapter)
                }
            }
            Response.Status.ERROR -> {
                viewModel.loading.set(false)
                Toast.makeText(this, "Erro ao carregar contas.", Toast.LENGTH_LONG).show()
                this.finish()
            }
            else -> {}
        }
    }

    private fun inflateCalendar() {
        val mDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dayNew = if (day < 10) "0$day" else day.toString()
            val monthNew = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"

            binding.edtData.setText("$year-$monthNew-$dayNew")
            selectedPeriod = "$year-$monthNew"
        }

        val defaultDate = LocalDate.now()

        val calendar = Calendar.getInstance()
        calendar.set(defaultDate.year, defaultDate.monthValue - 1, defaultDate.dayOfMonth)

        val dialog = DatePickerDialog(
            this,
            mDateSetListener,
            defaultDate.year,
            defaultDate.monthValue - 1,
            defaultDate.dayOfMonth
        )

        dialog.datePicker.maxDate = calendar.timeInMillis
        dialog.show()
    }
}
