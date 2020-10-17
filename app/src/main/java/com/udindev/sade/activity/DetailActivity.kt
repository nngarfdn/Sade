package com.udindev.sade.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.udindev.sade.R
import com.udindev.sade.model.Favorite
import com.udindev.sade.model.Produk
import com.udindev.sade.viewmodel.FavoriteViewModel
import com.udindev.sade.viewmodel.ProdukViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var produkViewModel: ProdukViewModel
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser = firebaseAuth.getCurrentUser()
    var isFavorite: Boolean = false
    var allFavorite: List<Favorite>? = arrayListOf()

    companion object {
        const val EXTRA_PRODUK = "extra_produk"
        private const val TAG = "DetailActivity"
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
        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel::class.java)


        var myFavorite = arrayListOf<Produk>()


        //kumpulan dukument id produk favorite saya
        var listIdMyFavorite = arrayListOf<String>()

        val produk = intent.getParcelableExtra<Produk>(EXTRA_PRODUK)

        // ra gelem metu
        favoriteViewModel.loadResult()
        favoriteViewModel.getResult().observe(this, Observer {

            allFavorite = it

        })

        allFavorite?.forEach { fav ->

            if (fav.email == firebaseUser?.email) {
                fav.idDocument?.let { it1 -> listIdMyFavorite.add(it1) }
                Log.d(TAG, "onCreate: list id my favorite ada = ${listIdMyFavorite.size}")

                for (id in listIdMyFavorite) {
                    Log.d(TAG, "onCreate daftar idDokument Myfavorite: $id ")
                    produkViewModel.loadResultById(id)
                    myFavorite.clear()
                    produkViewModel.getDataById().observe(this, Observer {
                        if (produk in it) {
                            isFavorite = true
                            setIconFavorite(isFavorite)
                        } else {
                            isFavorite = false
                            setIconFavorite(isFavorite)
                        }

                    })
                }
            }
            img_favorite.setOnClickListener {
                if (isFavorite) {
                    setIconFavorite(false)
                    favoriteViewModel.deleteFavorite(fav)
                } else {
                    
                    

                    setIconFavorite(true)
                    val favoriteAdd = Favorite("", produk?.id, firebaseUser?.email)
                    favoriteViewModel.saveFavorite(favoriteAdd)
                }
            }
        }


        Picasso.get()
                .load(produk?.photo)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.image_empty)
                .into(img_detailproduk)

        txt_alamat_detail.text = produk?.alamat
        txt_deskripsi_detail.text = produk?.deskripsi
        txt_nama_produk_detail.text = produk?.nama
        txt_harga_detail.text = "Rp ${produk?.harga}"

        btn_chat_detail.setText( "Chat ${produk?.wa}")

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


}