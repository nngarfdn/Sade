package com.udindev.sade.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.udindev.sade.model.Produk

class FavoriteRepository {

    private val db = FirebaseFirestore.getInstance()

    private var resultDataEmail : MutableLiveData<List<Produk>> = MutableLiveData()

    fun getResultsEmail(): LiveData<List<Produk>> = resultDataEmail
    

    fun saveFavorite(produk: Produk) {
        val item = hashMapProduk(produk)
        db.collection("favorite")
                .add(item)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding document", e)
                }
    }

    fun addFavorite(produk: Produk) {
        val idProduk = produk.id
        val item = hashMapProduk(produk)

        if (idProduk != null) {
            db.collection("produk").document(idProduk)
                    .set(item)
                    .addOnSuccessListener {
                        Log.d(TAG, "Succes tambah favorite")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error adding document", e)
                    }
        }
    }

    fun deleteFavorite(produk: Produk) {
        val idProduk: String? = produk.id
        hashMapProduk(produk)
        if (idProduk != null) {
            db.collection("favorite").document(idProduk)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error deleting document", e)
                    }
        }
    }

    fun getDataEmail(email: String) {
        val produkData: MutableList<Produk> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Produk>()
        val isFavorite = 1
        db.collection("produk")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val namaDocument = document.data["email"] as String
//                        val fav = document.data["isFavorite"] as String
                        if (namaDocument == email ) {
                            val pp = document.toObject(Produk::class.java)
                            pp.id = document.id
                            savedProdukList.add(pp)
                            produkData.add(pp)
                            Log.d(TAG, "getDataEmail size : ${savedProdukList.size}")
                            Log.d(TAG, "getDataEmail: $pp ")
                        }
                    }
                    resultDataEmail.value = produkData
                    Log.d(TAG, "readProduk size final getDataEmail : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }


    private fun hashMapProduk(produk: Produk): HashMap<String, Any?> {
        return hashMapOf(
                "nama" to produk.nama,
                "email" to produk.email,
                "kategori" to produk.kategori,
                "alamat" to produk.alamat,
                "kecamatan" to produk.kecamatan,
                "kabupaten" to produk.kabupaten,
                "provinsi" to produk.provinsi,
                "harga" to produk.harga,
                "wa" to produk.wa,
                "deskripsi" to produk.deskripsi,
                "photo" to produk.photo

        )
    }

    companion object {
        private const val TAG = "FavoriteRepository"
    }
}