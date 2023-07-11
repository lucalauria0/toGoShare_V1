package com.example.carsharingv2.main.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.carsharingv2.R
import com.example.carsharingv2.controllers.DBManager
import com.example.carsharingv2.controllers.QueryCallback
import com.example.carsharingv2.databinding.FragmentAddAutoMoreBinding
import com.example.carsharingv2.models.AutoModel
import java.text.SimpleDateFormat
import java.util.Locale

class AddAutoMoreFragment : Fragment() {

    private lateinit var binding: FragmentAddAutoMoreBinding
    private lateinit var calendar: Calendar
    lateinit var autoModel: AutoModel
    lateinit var dtPartenza : String
    lateinit var dtDestinazione : String
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    companion object { //non so perche usando semplicemente requireArguments().getParcelable("auto") non funzionava cosi ho fatto cosi
        fun newInstance(bundle: Bundle): AddAutoMoreFragment {
            val fragment = AddAutoMoreFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddAutoMoreBinding.inflate(inflater)
        calendar = Calendar.getInstance()


        binding.selezionadatatempopartenza.setOnClickListener {
            mostraDataPicker { dataSelezionata ->
                mostraTempoPicker { tempoSelezionato ->
                    val dateTime = calendarToSqlDateTime(dataSelezionata, tempoSelezionato)
                    dtPartenza = dateTime.toString()
                }
            }
        }

        binding.selezionadatatempoarrivo.setOnClickListener {
            mostraDataPicker { dataSelezionata ->
                mostraTempoPicker { tempoSelezionato ->
                    val dateTime = calendarToSqlDateTime(dataSelezionata, tempoSelezionato)
                    autoModel = requireArguments().getParcelable("automodel")!!
                    val prezzo = autoModel.prezzo
                    val ref_proprietario = autoModel.ref_proprietario
                    val posti_disponibili = autoModel.posti_disponibili
                    val partenza = autoModel.partenza
                    val destinazione = autoModel.destinazione
                    val modello = autoModel.modello
                    val immagine = autoModel.immagine
                    dtDestinazione = dateTime.toString()
                    autoModel = AutoModel(
                        prezzo,
                        ref_proprietario,
                        posti_disponibili,
                        partenza,
                        destinazione,
                        modello,
                        immagine,
                        dtPartenza,
                        dtDestinazione
                    )
                }
            }
        }

        binding.inserisci.setOnClickListener {
            Log.i("AddAutoMoreFragment", autoModel.toString())

            if(dtDestinazione.isEmpty() && dtPartenza.isEmpty()) {
                Toast.makeText(requireContext(), "Data non trovata", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            aggiungiAuto(autoModel)

        }



        return binding.root
    }

    //richiamo dbmanager e alla success richiamo returnhome per cambiare fragment in addaauto
    private fun aggiungiAuto(autoModel: AutoModel) {
        DBManager().addAuto(autoModel, object  : QueryCallback {
            override fun success(response: Any, success: String) {
                returnToHome()
                Toast.makeText(requireContext(), "ANNUNCIO AGGIUNTO!", Toast.LENGTH_SHORT).show()
            }
            override fun failure(fail: String) {
                Toast.makeText(requireContext(), fail, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun returnToHome() {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_left_to_right, R.anim.enter_right_to_left)
            .replace(R.id.xContainer, AutoAggiuntaFragment())
            .commit()
    }

    //prendo in input nel datepickerdialog le variabili selezionate e le memorizzo nel calendario, che seguirà sopra l'andamento mostrato
    //mostradata => mostratempo e immagazzina tutte le variabili in output in un variabile datetime da mandare poi al db insieme all automodel
    private fun mostraDataPicker(onDateSelected: (dataSelezionata: Calendar) -> Unit) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                onDateSelected(calendar)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun mostraTempoPicker(onTimeSelected: (tempoSelezionato: Calendar) -> Unit) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                onTimeSelected(calendar)
            },
            hour,
            minute,
            false
        )
        timePickerDialog.show()
    }

    private fun calendarToSqlDateTime(date: Calendar, time: Calendar): String {
        val dateTimeCalendar = Calendar.getInstance()
        dateTimeCalendar.set(
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH),
            time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE),
            0 // non servono
        )
        return dateTimeFormat.format(dateTimeCalendar.time)
    }
}