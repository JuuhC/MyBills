package com.carrati.mybills.ui.receita

import android.app.DatePickerDialog
import android.os.Bundle
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
import com.carrati.mybills.app.databinding.ActivityReceitaBinding
import java.time.LocalDate
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReceitaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReceitaBinding
    private val viewModel: ReceitaViewModel by viewModel()

    private lateinit var usuario: Usuario
    private var selectedPeriod: String? = null
    private var transacao: Transacao? = null

    private var observerListarConta = Observer<Response> { processResponseListarConta(it) }
    private var observerSalvar = Observer<Response> { processResponseSave(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_receita)
        binding.viewModel = viewModel

        viewModel.getUsuario()
        viewModel.usuarioLiveData?.observe(this) {
            this.usuario = it

            viewModel.listarContasLiveData.observe(this, observerListarConta)
            viewModel.carregarContas(it.uid!!)
        }

        binding.fabSave.setOnClickListener { saveTransacao() }
        binding.edtData.setOnClickListener { inflateCalendar() }
        binding.switchEfetuado.setOnClickListener {
            binding.switchEfetuado.text = if (binding.switchEfetuado.isChecked) "Pago" else "Não pago"
        }

        transacao = intent.getSerializableExtra("transacao") as Transacao?
        if (transacao != null) {
            binding.edtData.setText(transacao!!.data ?: "")
            binding.edtDescr.setText(transacao!!.nome ?: "")
            binding.edtValor.setText(String.format("%.2f", transacao!!.valor ?: 0.0))
            binding.switchEfetuado.isChecked = transacao!!.efetuado ?: false

            selectedPeriod = transacao!!.data!!.subSequence(0, 7).toString()
        }

        binding.edtData.requestFocus()
    }

    private fun saveTransacao() {
        if (binding.edtData.text.toString() == "") {
            binding.tvDataLayout.isErrorEnabled = true
            binding.tvDataLayout.error = "Selecione uma data"
            return
        } else binding.tvDataLayout.isErrorEnabled = false

        if (binding.edtDescr.text.toString() == "") {
            binding.tvDescrLayout.isErrorEnabled = true
            binding.tvDescrLayout.error = "Adicione uma descrição"
            return
        } else binding.tvDescrLayout.isErrorEnabled = false

        if (binding.edtValor.text.toString() == "" || binding.edtValor.text.toString() == "0.00") {
            Toast.makeText(this, "Valor não pode ser zero", Toast.LENGTH_LONG).show()
            return
        }

        val transacaoNew = Transacao().apply {
            this.tipo = "receita"
            this.data = binding.edtData.text.toString()
            this.nome = binding.edtDescr.text.toString()
            this.conta = binding.spinner.text.toString()
            var doubleValue = 0.0
            try {
                doubleValue = java.lang.Double.parseDouble(
                    binding.edtValor.text.toString().replace(",", ".")
                )
            } catch (_: NumberFormatException) { }
            this.valor = doubleValue
            this.efetuado = binding.switchEfetuado.isChecked
        }

        viewModel.receitaLiveData.observe(this, observerSalvar)
        if (this.transacao != null) {
            transacaoNew.id = this.transacao!!.id
            viewModel.editarTransacao(usuario.uid!!, selectedPeriod!!, transacaoNew, this.transacao)
        } else {
            viewModel.salvarTransacao(usuario.uid!!, selectedPeriod!!, transacaoNew)
        }
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

                    val spinnerAdapter = object : ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        list
                    ) {
                        private val filter_that_does_nothing = object : Filter() {
                            override fun performFiltering(constraint: CharSequence?): FilterResults {
                                val results = FilterResults()
                                results.values = list
                                results.count = list.size
                                return results
                            }
                            override fun publishResults(constraint: CharSequence?,results: FilterResults?) {
                                notifyDataSetChanged()
                            }
                        }

                        override fun getFilter(): Filter {
                            return filter_that_does_nothing
                        }
                    }
                    spinnerAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item
                    )
                    binding.spinner.setAdapter(spinnerAdapter)
                    if (transacao != null) binding.spinner.setSelection(
                        list.indexOf(transacao!!.conta)
                    )
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

        DatePickerDialog(
            this,
            mDateSetListener,
            defaultDate.year,
            defaultDate.monthValue - 1,
            defaultDate.dayOfMonth
        ).show()
    }
}
