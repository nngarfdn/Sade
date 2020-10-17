package com.udindev.sade.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udindev.sade.R
import com.udindev.sade.activity.DetailActivity
import com.udindev.sade.model.Produk
import kotlinx.android.synthetic.main.item_produk_horizontal.view.*

class ProdukMenuAdapter (private val list: List<Produk>?) :
        RecyclerView.Adapter<ProdukMenuAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_produk_horizontal, parent, false)
    )

    override fun getItemCount(): Int = list?.size!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
                .load(list?.get(position)?.photo)
                .resize(120, 120) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .placeholder(R.drawable.image_empty)
                .into(holder.itemView.img_item)
        holder.itemView.txt_nama_produk.text = list?.get(position)?.nama
        holder.itemView.txt_alamatproduk.text = list?.get(position)?.alamat
        holder.itemView.txt_hargaproduk.text = "Rp ${list?.get(position)?.harga}"

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }
}
