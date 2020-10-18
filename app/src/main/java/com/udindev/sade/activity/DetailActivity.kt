package com.udindev.sade.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.udindev.sade.R
import com.udindev.sade.model.Produk
import com.udindev.sade.utils.AppUtils.getRupiahFormat
import com.udindev.sade.viewmodel.ProdukViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var favoriteViewModel: com.udindev.sade.cobacoba.FavoriteViewModel
    private lateinit var produkViewModel: ProdukViewModel
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser = firebaseAuth.getCurrentUser()
    var isFavorite: Boolean = false


    companion object {
        const val EXTRA_PRODUK = "extra_produk"
        private const val TAG = "DetailActivity"
    }


    @SuppressLint("SetTextI18n", "QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favoriteViewModel = ViewModelProviders.of(this).get(com.udindev.sade.cobacoba.FavoriteViewModel::class.java)
        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel::class.java)

        val produk = intent.getParcelableExtra<Produk>(EXTRA_PRODUK)

        favoriteViewModel.data.observe(this, Observer { favorite ->
            isFavorite = favorite.listProductId.contains(produk!!.id)
            if (isFavorite) img_favorite.setImageResource(R.drawable.ic_baseline_favorite_24_red) else img_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        })


            img_favorite.setOnClickListener {
                val userId = firebaseUser!!.uid
                if (isFavorite) {
                    favoriteViewModel.remove(userId, produk!!.id)
                    img_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    Toast.makeText(this, "Berhasil Hapus Favorite", Toast.LENGTH_SHORT).show()
                } else {
                    favoriteViewModel.add(userId, produk!!.id)
                    img_favorite.setImageResource(R.drawable.ic_baseline_favorite_24_red)
                    Toast.makeText(this, "Berhasil Tambah Favorite", Toast.LENGTH_SHORT).show()
                }
                isFavorite = !isFavorite
            }


        Picasso.get()
                .load(produk?.photo)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.image_empty)
                .into(img_detailproduk)

        txt_alamat_detail.text = "${produk?.alamat}, ${produk?.kecamatan}, ${produk?.kabupaten}, ${produk?.provinsi}"
        txt_deskripsi_detail.text = produk?.deskripsi
        txt_nama_produk_detail.text = produk?.nama
        txt_harga_detail.text = produk?.harga?.let { getRupiahFormat(it, true) }

        btn_chat_detail.setText("Chat ${produk?.wa}")

        btn_chat_detail.setOnClickListener {
            val nomer = produk?.wa
            val url = "http://wa.me/$nomer"
            val webpage = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else{
                Toast.makeText(this, "TIdak bisa membuka", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        favoriteViewModel.loadData(firebaseUser!!.uid)
    }


}