package com.udindev.sade.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.udindev.sade.R
import com.udindev.sade.model.Produk
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PRODUK = "extra_produk"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val produk = intent.getParcelableExtra<Produk>(EXTRA_PRODUK)

        Picasso.get()
                .load(produk?.photo)
                .fit()
                .placeholder(R.drawable.image_empty)
                .into(img_detailproduk)

        txt_alamat_detail.text = produk?.alamat
        txt_deskripsi_detail.text = produk?.deskripsi
        txt_nama_produk_detail.text = produk?.nama
        txt_harga_detail.text = "Rp ${produk?.harga}"

        btn_chat_detail.setOnClickListener {
            val nomer = produk?.wa

        }

    }
}