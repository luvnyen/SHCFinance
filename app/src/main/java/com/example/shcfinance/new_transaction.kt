package com.example.shcfinance

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import android.text.InputType
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.widget.*

import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

private val categoryName: ArrayList<String> = ArrayList()

class new_transaction : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        categoryName.clear()
        db.collection("tbCategory").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    categoryName.add(document.data.get("name").toString())
                }
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
                Toast.makeText(this, "Gagal mengambil data!", Toast.LENGTH_LONG).show()
            }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categoryName
        )
        val editTextFilledExposedDropdown =
            findViewById<AutoCompleteTextView>(R.id.mKategori)
        editTextFilledExposedDropdown.setAdapter(adapter)

        val _btnPengeluaran = findViewById<Button>(R.id.btnPengeluaran)
        val _btnPemasukkan = findViewById<Button>(R.id.btnPemasukkan)
        val _tvRP = findViewById<TextView>(R.id.tvRP)
        val _labelNominal = findViewById<TextInputLayout>(R.id.outlinedTextField)
        var type = "outcome"
        _btnPengeluaran.setOnClickListener {
            _btnPengeluaran.setTextColor(Color.parseColor("#ffffff"))
            _btnPengeluaran.setBackgroundColor(Color.parseColor("#eb283b"))

            _btnPemasukkan.setTextColor(Color.parseColor("#9c9b9c"))
            _btnPemasukkan.setBackgroundColor(Color.parseColor("#e0dfe1"))

            _tvRP.setTextColor(Color.parseColor("#eb283b"))
            _labelNominal.setHint("Nominal pengeluaran")
            type = "outcome"
        }
        _btnPemasukkan.setOnClickListener {
            _btnPemasukkan.setTextColor(Color.parseColor("#ffffff"))
            _btnPemasukkan.setBackgroundColor(Color.parseColor("#27e831"))

            _btnPengeluaran.setTextColor(Color.parseColor("#9c9b9c"))
            _btnPengeluaran.setBackgroundColor(Color.parseColor("#e0dfe1"))

            _tvRP.setTextColor(Color.parseColor("#27e831"))
            _labelNominal.setHint("Nominal pemasukkan")
            type = "income"
        }

        val _btnBackTransaksi = findViewById<ImageButton>(R.id.btnBackTransaksi)
        _btnBackTransaksi.setOnClickListener {
            startActivity(Intent(this@new_transaction, MainActivity::class.java))
        }

        val _tvKategoriBaru = findViewById<TextView>(R.id.tvKategoriBaru)
        _tvKategoriBaru.setOnClickListener {
            showAlertWithTextInputLayout(this, db)
        }

        val _eNominal = findViewById<EditText>(R.id.eNominal)
        val _eDeskripsi = findViewById<EditText>(R.id.eDeskripsi)
        val _mKategori = findViewById<AutoCompleteTextView>(R.id.mKategori)
        val _mDateTransaction = findViewById<AutoCompleteTextView>(R.id.mDateTransaction)
        val _btnSimpanTransaksi = findViewById<Button>(R.id.btnSimpanTransaksi)
        _btnSimpanTransaksi.setOnClickListener {
            if (TextUtils.isEmpty(_eNominal.getText()) || TextUtils.isEmpty(_eDeskripsi.getText()) || TextUtils.isEmpty(_mKategori.getText()) || TextUtils.isEmpty(_mDateTransaction.getText())) {
                if (TextUtils.isEmpty(_eNominal.getText())) {
                    _eNominal.setError("Nominal tidak boleh kosong!")
                }
                if (TextUtils.isEmpty(_eDeskripsi.getText())) {
                    _eDeskripsi.setError("Keterangan tidak boleh kosong!")
                }
                if (TextUtils.isEmpty(_mKategori.getText())) {
                    _mKategori.setError("Kategori tidak boleh kosong!")
                }
                if (TextUtils.isEmpty(_mDateTransaction.getText())) {
                    _mDateTransaction.setError("Tanggal tidak boleh kosong!")
                }
            } else {
                if (_eNominal.text.toString().toInt() <= 0) {
                    _eNominal.setError("Nominal tidak boleh kosong!")
                } else {
                    _btnSimpanTransaksi.isEnabled = false
                    _btnSimpanTransaksi.isClickable = false

                    db.collection("tbCategory").get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                if (document.data.get("name") == _mKategori.text.toString()) {
                                    Toast.makeText(this, "Menyimpan data...", Toast.LENGTH_LONG).show()
                                    val data = transaction(generateID(), _eDeskripsi.text.toString(), _eNominal.text.toString().toInt(), type, document.data.get("id").toString(), _mDateTransaction.text.toString())
                                    val intent = Intent(this@new_transaction, MainActivity::class.java)
                                    db.collection("tbTransaction").document(data.id)
                                        .set(data)
                                        .addOnSuccessListener {
                                            Log.d("Firebase", "Successfully added!")
                                            intent.putExtra(
                                                "msg",
                                                "Berhasil menyimpan data!"
                                            )
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener {
                                            Log.d("Firebase", it.message.toString())
                                            intent.putExtra(
                                                "msg",
                                                "Gagal menyimpan data!"
                                            )
                                            startActivity(intent)
                                        }
                                    break
                                }
                            }
                        }
                }
            }
        }

        pickdate()
    }

    private fun generateID(): String {
        return UUID.randomUUID().toString()
    }

    private fun showAlertWithTextInputLayout(context: Context, db: FirebaseFirestore) {
        val textInputLayout = TextInputLayout(context)
        textInputLayout.setPadding(
            20,0,0,0
        )
        val input = EditText(context)
        textInputLayout.addView(input)
        val alert = AlertDialog.Builder(context)
            .setTitle("Tambah kategori baru")
            .setView(textInputLayout)
            .setMessage("Silakan masukkan nama kategori baru")
            .setPositiveButton("Simpan") { dialog, _ ->
                if (TextUtils.isEmpty(input.text)) {
                    Toast.makeText(this, "Nama kategori tidak boleh kosong!", Toast.LENGTH_LONG).show();
                    showAlertWithTextInputLayout(this, db)
                } else {
                    Toast.makeText(this, "Menyimpan data...", Toast.LENGTH_LONG).show()
                    val data = category(generateID(), input.text.toString())
                    db.collection("tbCategory").document(data.id)
                        .set(data)
                        .addOnSuccessListener {
                            Log.d("Firebase", "Successfully added!")
                            Toast.makeText(this, "Berhasil menyimpan data!", Toast.LENGTH_LONG).show()

                            categoryName.clear()
                            db.collection("tbCategory").get()
                                .addOnSuccessListener { result ->
                                    for (document in result) {
                                        categoryName.add(document.data.get("name").toString())
                                    }
                                }
                                .addOnFailureListener {
                                    Log.d("Firebase", it.message.toString())
                                    Toast.makeText(this, "Gagal mengambil data!", Toast.LENGTH_LONG).show()
                                }

                            val adapter = ArrayAdapter(
                                this,
                                android.R.layout.simple_spinner_dropdown_item,
                                categoryName
                            )
                            val editTextFilledExposedDropdown =
                                findViewById<AutoCompleteTextView>(R.id.mKategori)
                            editTextFilledExposedDropdown.setAdapter(adapter)

                            val _mKategori = findViewById<AutoCompleteTextView>(R.id.mKategori)
                            _mKategori.setText(input.text, false)
                        }
                        .addOnFailureListener {
                            Log.d("Firebase", it.message.toString())
                            Toast.makeText(this, "Gagal menyimpan data!", Toast.LENGTH_LONG).show()
                        }
                    dialog.cancel()
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.cancel()
            }.create()
        input.setInputType(InputType.TYPE_CLASS_TEXT)
        input.requestFocus()

        alert.show()
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
        val _mDateTransaction = findViewById<AutoCompleteTextView>(R.id.mDateTransaction)
        _mDateTransaction.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val dateFormat = SimpleDateFormat("MM-dd-yyyy").parse("${p2 + 1}-$p3-$p1")
        val formattedDate = "${DateFormat.format("MMMM", dateFormat)} ${DateFormat.format("dd", dateFormat)}, ${DateFormat.format("yyyy", dateFormat)}"

        val _mDateTransaction = findViewById<AutoCompleteTextView>(R.id.mDateTransaction)
        _mDateTransaction.setText(formattedDate)
    }
}