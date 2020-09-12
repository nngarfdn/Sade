package com.udindev.sade.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udindev.sade.R
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
//        Picasso.get()
//                .load(list?.get(position)?.urlToImage)
//                .placeholder(R.drawable.ic_baseline_image_24)
//                .into(holder.itemView.image_news)
        holder.itemView.txt_nama_produk.text = list?.get(position)?.nama
        holder.itemView.txt_alamatproduk.text = list?.get(position)?.alamat
        holder.itemView.txt_hargaproduk.text = "Rp ${list?.get(position)?.harga}"

//        holder.itemView.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString(DetailNewsFragment.EXTRA_URL, list.get(position)?.url)
//            val fragment = DetailNewsFragment()
//            fragment.arguments = bundle
//
//            setFragment(fragment, holder.itemView.context)
        }
    }

//    private fun setFragment(fragment: DetailNewsFragment, context: Context?) {
//        (context as AppCompatActivity).supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.framelayout, fragment, "Adapter")
//                .addToBackStack(null)
//                .commit()
//    }

//}