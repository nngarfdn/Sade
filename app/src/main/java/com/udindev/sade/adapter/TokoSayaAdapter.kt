package com.udindev.sade.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udindev.sade.R
import com.udindev.sade.activity.DetailActivity
import com.udindev.sade.activity.EditProduk
import com.udindev.sade.model.Produk
import com.udindev.sade.utils.AppUtils.getRupiahFormat
import kotlinx.android.synthetic.main.item_produk_vertical.view.*

class TokoSayaAdapter(private val list: List<Produk>?) :
        RecyclerView.Adapter<TokoSayaAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_produk_vertical, parent, false)
    )

    override fun getItemCount(): Int = list?.size!!

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
                .load(list?.get(position)?.photo)
                .resize(120, 120) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .placeholder(R.drawable.image_empty)
                .into(holder.itemView.img_item)
        holder.itemView.txt_nama_produk.text = list?.get(position)?.nama
        holder.itemView.txt_alamatproduk.text = list?.get(position)?.alamat
        holder.itemView.txt_deskripsi_produk.text = list?.get(position)?.deskripsi
        holder.itemView.txt_harga.text = list?.get(position)?.harga?.let { getRupiahFormat(it, true) }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditProduk::class.java)
            intent.putExtra(DetailActivity.EXTRA_PRODUK, list?.get(position))
            holder.itemView.context.startActivity(intent)
        }
    }


}