package com.example.shcfinance

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    val arTransaction = arrayListOf<transaction>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val _rvTransaction = findViewById<RecyclerView>(R.id.rvTransaction)
        db.collection("tbTransaction").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val transactionObj = transaction(document.data.get("id").toString(), document.data.get("description").toString(), document.data.get("sum_of_money").toString().toInt(), document.data.get("type").toString(), document.data.get("id_category").toString(), document.data.get("date").toString())
                    arTransaction.add(transactionObj)
                }
                _rvTransaction.layoutManager = LinearLayoutManager(this)
                val adapterN = adapterTransaction(arTransaction)
                _rvTransaction.adapter = adapterN
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }

        val msg = intent.getStringExtra("msg")
        msg?.let {
            Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show()
        }

        val _btnTambahTransaksi = findViewById<Button>(R.id.btnTambahTransaksi)
        _btnTambahTransaksi.setOnClickListener {
            startActivity(Intent(this@MainActivity, new_transaction::class.java))
        }

        pickdate()

        val _tvLihatSemua = findViewById<TextView>(R.id.tvLihatSemua)
        val _mDate = findViewById<AutoCompleteTextView>(R.id.mDate)
        _tvLihatSemua.setOnClickListener {
            _mDate.setText("")
            _mDate.clearFocus()
            _tvLihatSemua.setVisibility(TextView.GONE)

            arTransaction.clear()
            db.collection("tbTransaction").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val transactionObj = transaction(document.data.get("id").toString(), document.data.get("description").toString(), document.data.get("sum_of_money").toString().toInt(), document.data.get("type").toString(), document.data.get("id_category").toString(), document.data.get("date").toString())
                        arTransaction.add(transactionObj)
                    }

                    val _tvEmptyData = findViewById<TextView>(R.id.tvEmptyData)
                    if (arTransaction.isEmpty()) {
                        _tvEmptyData.setVisibility(TextView.VISIBLE)
                    } else {
                        _tvEmptyData.setVisibility(TextView.GONE)
                    }

                    _rvTransaction.layoutManager = LinearLayoutManager(this)
                    val adapterN = adapterTransaction(arTransaction)
                    _rvTransaction.adapter = adapterN
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }
        }
    }

    var day = 0
    var month = 0
    var year = 0

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun pickdate() {
        val _mDate = findViewById<AutoCompleteTextView>(R.id.mDate)
        val _tvLihatSemua = findViewById<TextView>(R.id.tvLihatSemua)
        _mDate.setOnClickListener {
            _tvLihatSemua.setVisibility(TextView.VISIBLE)
            getDateTimeCalendar()
            DatePickerDialog(this, this, year, month, day).show()
            _mDate.clearFocus()
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val dateFormat = SimpleDateFormat("MM-dd-yyyy").parse("${p2 + 1}-$p3-$p1")
        val formattedDate = "${DateFormat.format("MMMM", dateFormat)} ${DateFormat.format("dd", dateFormat)}, ${DateFormat.format("yyyy", dateFormat)}"

        val _mDate = findViewById<AutoCompleteTextView>(R.id.mDate)
        _mDate.setText(formattedDate)

        val _rvTransaction = findViewById<RecyclerView>(R.id.rvTransaction)

        arTransaction.clear()
        db.collection("tbTransaction").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data.get("date") == formattedDate) {
                        val transactionObj = transaction(document.data.get("id").toString(), document.data.get("description").toString(), document.data.get("sum_of_money").toString().toInt(), document.data.get("type").toString(), document.data.get("id_category").toString(), document.data.get("date").toString())
                        arTransaction.add(transactionObj)
                    }
                }
                _rvTransaction.layoutManager = LinearLayoutManager(this)
                val adapterN = adapterTransaction(arTransaction)
                _rvTransaction.adapter = adapterN

                val _tvEmptyData = findViewById<TextView>(R.id.tvEmptyData)
                if (arTransaction.isEmpty()) {
                    _tvEmptyData.setVisibility(TextView.VISIBLE)
                } else {
                    _tvEmptyData.setVisibility(TextView.GONE)
                }
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }
}