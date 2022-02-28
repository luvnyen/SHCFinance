package com.example.shcfinance

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

data class adapterTransaction(private val listTransactions: ArrayList<transaction>) : RecyclerView.Adapter<adapterTransaction.ListViewHolder>() {
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _itemDescription : TextView = itemView.findViewById(R.id.tvItemDescription)
        var _itemSumofMoney : TextView = itemView.findViewById(R.id.tvItemSumofMoney)
        var _itemCategory : TextView = itemView.findViewById(R.id.tvItemCategory)
        var _itemDate : TextView = itemView.findViewById(R.id.tvItemDate)
        var _iconItemDelete : ImageButton = itemView.findViewById(R.id.iconItemDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterTransaction.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterTransaction.ListViewHolder, position: Int) {
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        val transaction = listTransactions[position]

        holder._itemDescription.setText(transaction.description)
        holder._itemDate.setText(transaction.date)

        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.setMaximumFractionDigits(0)
        format.setCurrency(Currency.getInstance("IDR"))
        holder._itemSumofMoney.setText(format.format(transaction.sum_of_money.toString().toInt()))

        if (transaction.type == "income") {
            holder._itemSumofMoney.setTextColor(Color.parseColor("#27e831"))
        } else {
            holder._itemSumofMoney.setTextColor(Color.parseColor("#eb283b"))
        }

        db.collection("tbCategory").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data.get("id") == transaction.id_category) {
                        holder._itemCategory.setText("Kategori: ${document.data.get("name").toString()}")
                        break
                    }
                }
            }

        holder._iconItemDelete.setOnClickListener {
            db.collection("tbTransaction").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data.get("id").toString() == transaction.id) {
                            db.collection("tbTransaction").document(transaction.id)
                                .delete()
                                .addOnSuccessListener {
                                    Log.d("Firebase", "Successfully deleted!")
                                }
                                .addOnFailureListener {
                                    Log.d("Firebase", it.message.toString())
                                }
                            break
                        }
                    }
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }

            listTransactions.removeAt(position)
            Toast.makeText(holder.itemView.context, "Data berhasil dihapus!", Toast.LENGTH_LONG).show()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return listTransactions.size
    }
}