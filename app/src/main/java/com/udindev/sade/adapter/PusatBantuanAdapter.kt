package com.udindev.sade.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udindev.sade.R
import com.udindev.sade.model.PusatBantuan
import kotlinx.android.synthetic.main.item_pusat_bantuan.view.*

class PusatBantuanAdapter (private val list: List<PusatBantuan>) : RecyclerView.Adapter<PusatBantuanAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pusat_bantuan, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.txt_nama_pusat_bantuan.text = list[position].namaPusatBantuan
        holder.itemView.txt_deskripsi_pusat_bantuan.text = list[position].deskripsiPusatBantuan
    }
}
