package com.udindev.sade.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udindev.sade.R
import com.udindev.sade.activity.DetailActivity
import com.udindev.sade.model.Produk
import kotlinx.android.synthetic.main.item_produk_vertical.view.*

class JasaMenuAdapter (private val list: List<Produk>?) :
        RecyclerView.Adapter<JasaMenuAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_produk_vertical, parent, false)
    )

    override fun getItemCount(): Int = list?.size!!

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Picasso.get()
//                .load(list?.get(position)?.urlToImage)
//                .placeholder(R.drawable.ic_baseline_image_24)
//                .into(holder.itemView.image_news)
        holder.itemView.txt_nama_produk.text = list?.get(position)?.nama
        holder.itemView.txt_alamatproduk.text = list?.get(position)?.alamat
        holder.itemView.txt_harga.text = "Rp ${list?.get(position)?.harga}"

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }
}