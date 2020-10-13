package com.udindev.sade.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.udindev.sade.R
import com.udindev.sade.model.Produk
import com.udindev.sade.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser = firebaseAuth.getCurrentUser()

    companion object {
        const val EXTRA_PRODUK = "extra_produk"
    }

    private fun setIconFavorite(favorite: Boolean) {
        if (favorite) {
            img_favorite.setImageResource(R.drawable.ic_baseline_favorite_24_red)
        } else {
            img_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)

        val email = firebaseUser?.email
        if (email != null) {
            favoriteViewModel.loadResultDataEmail(email)
        }

        val listFavorite = arrayListOf<Produk>()

        favoriteViewModel.getDataEmail().observe(this, Observer {

            for (a in it) {
                listFavorite.add(a)
            }

        })

        val produk = intent.getParcelableExtra<Produk>(EXTRA_PRODUK)

//        Picasso.get()
//                .load(produk?.photo)
//                .fit()
//                .placeholder(R.drawable.image_empty)
//                .into(img_detailproduk)

        txt_alamat_detail.text = produk?.alamat
        txt_deskripsi_detail.text = produk?.deskripsi
        txt_nama_produk_detail.text = produk?.nama
        txt_harga_detail.text = "Rp ${produk?.harga}"

        btn_chat_detail.setOnClickListener {
            val nomer = produk?.wa

        }

        var isFavorite: Boolean
        isFavorite = listFavorite.contains(produk)
        setIconFavorite(isFavorite)

        img_favorite.setOnClickListener {
            if (isFavorite) {
                isFavorite = false
                setIconFavorite(isFavorite)
                if (produk != null) {
                    favoriteViewModel.deleteFavorite(produk)
                    Toast.makeText(this, "Berhasil hapus favorit", Toast.LENGTH_SHORT).show()
                }
            } else {

                if (produk != null) {
                    favoriteViewModel.saveFavorite(produk)
                    Toast.makeText(this, "Berhasil Menambahkan", Toast.LENGTH_SHORT).show()
                }
                isFavorite = true
                setIconFavorite(isFavorite)
            }
        }

    }


}