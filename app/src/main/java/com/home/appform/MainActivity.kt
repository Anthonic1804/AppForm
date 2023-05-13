package com.home.appform

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.home.appform.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main) -> DEFAULT ANDROID
        setContentView(binding.root)// VIEWBINDING

        val depto = arrayOf("Ahuachapán", "Santa Ana", "Sonsonate", "Chalatenango", "Cuscatlan",
            "San Salvador", "San Miguel", "La Unión")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            depto) 
        binding.etPais.setAdapter(adapter)
        binding.etPais.setOnItemClickListener { parent, view, position, id ->
            binding.etDireccion.requestFocus()
            Toast.makeText(this, depto[position], Toast.LENGTH_LONG).show()
        }

        binding.etFecha.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()

            picker.addOnPositiveButtonClickListener {
                val dateStr = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }.format(it)
                binding.etFecha.setText(dateStr)
            }

            picker.show(supportFragmentManager, picker.toString())
        }

    }

    //FUNCION PARA MOSTRAR EL MENU CREADO EN LA ACTIVIDAD
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //FUNCION PARA DETECTAR LOS CLICK EN EL MENU
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_send -> {
                if(validFields()) {
                    //CAPTURANDO EL VALOR DEL IMPUT NAME FORMA DEFAULT
                    val name: String =
                        findViewById<TextInputEditText>(R.id.etNombre).text.toString().trim()

                    //CAPTURANDO POR MEDIO DE VIEWBINDING
                    val surname = binding.etApellido.text.toString().trim()
                    val altura = binding.etAltura.text.toString().trim()
                    val fecha = binding.etFecha.text.toString().trim()
                    val pais = binding.etPais.text.toString().trim()
                    val lugar = binding.etDireccion.text.toString().trim()
                    val notas = binding.etNotas.text.toString().trim()

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle(getString(R.string.dialog_title))
                    builder.setMessage(joinData(name, surname, altura, fecha, pais, lugar, notas))
                    builder.setIcon(R.drawable.ic_info)
                    builder.setCancelable(false)
                    builder.setPositiveButton(getString(R.string.alert_confirmar)) { _, _ ->
                        with(binding){
                            etNombre.text?.clear()
                            etApellido.text?.clear()
                            etAltura.text?.clear()
                            etFecha.text?.clear()
                            etPais.text?.clear()
                            etDireccion.text?.clear()
                            etNotas.text?.clear()
                        }
                    }
                    builder.setNegativeButton(getString(R.string.dialog_cancelar), null)

                    val dialog: AlertDialog = builder.create() // INICIALIZANDO EL ALERT
                    dialog.show()//MOSTRANDO EL ALERT
                }
            }
            R.id.action_temp -> {
                Toast.makeText(this@MainActivity, "Click en Temp", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun joinData(vararg fields: String): String{
        var result: String = ""
        fields.forEach { camposFormulario->
            if(fields.isNotEmpty()){
                result += "$camposFormulario\n"
            }
        }
        return result
    }

    private fun validFields() : Boolean{
        var isValid = true

        //VALIDANDO EL CAMPO NOMBRE
        if(binding.etNombre.text.isNullOrEmpty()){
            binding.tilNombre.run {
                error = getString(R.string.help_require)
                requestFocus()
            }
            isValid = false
        }else{
            binding.tilNombre.error = null
        }

        //VALIDANDO EL CAMPO APELLIDO
        if(binding.etApellido.text.isNullOrEmpty()){
            binding.tilApellido.run {
                error = getString(R.string.help_require)
                requestFocus()
            }
            isValid = false
        }else{
            binding.tilApellido.error = null
        }

        //VALIDANDO EL CAMPO ESTATURA
        if(binding.etAltura.text.isNullOrEmpty()){
            binding.etAltura.run {
                error = getString(R.string.help_require)
                requestFocus()
            }
            isValid = false
        }else if(binding.etAltura.text.toString().toInt() < 50){
            binding.etAltura.run {
                error = getString(R.string.help_min)
                requestFocus()
            }
            isValid = false
        }else{
            binding.etAltura.error = null
        }

        return isValid
    }

}