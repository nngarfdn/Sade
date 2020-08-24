package com.udindev.sade.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.udindev.sade.model.Produk

class ProdukRepository {

    private var resultData: MutableLiveData<List<Produk>> = MutableLiveData()
    companion object {
        private val TAG: String = ProdukRepository::class.java.simpleName
    }
    private val db = FirebaseFirestore.getInstance()

    fun getResults(): LiveData<List<Produk>> = resultData

    fun getData() {
        val produkData: MutableList<Produk> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Produk>()
        db.collection("produk")
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {
                        val id = document.id
                        val nama = document.data["nama"] as String
                        val produkItem = Produk(id, nama)
                        savedProdukList.add(produkItem)
                        produkData.add(produkItem)
                        Log.d(TAG, "readProduk savedProdukList size : ${savedProdukList.size}")
                        Log.d(TAG, "readProduk: id = $id nama = $nama ")
                    }
                    resultData.value = produkData
                    Log.d(TAG, "readProduk size final savedProdukList : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }

    fun saveProduk(produk: Produk) {
        val item = hashMapProduk(produk)
        db.collection("produk")
                .add(item)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding document", e)
                }
    }

    fun update(produk: Produk) {
        val idProduk = produk.id
        val item = hashMapProduk(produk)

        if (idProduk != null) {
            db.collection("produk").document(idProduk)
                    .set(item)
                    .addOnSuccessListener {
                        Log.d(TAG, "Succes update")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error adding document", e)
                    }
        }
    }

    fun delete(produk: Produk) {
        val idProduk : String? = produk.id
        hashMapProduk(produk)
        if (idProduk != null) {
            db.collection("produk").document(idProduk)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error deleting document", e)
                    }
        }
    }

    private fun hashMapProduk(produk: Produk): HashMap<String, Any?> {
        return hashMapOf(
                "nama" to produk.nama,
                "kategori" to produk.kategori,
                "alamat" to produk.alamat,
                "kecamatan" to produk.kecamatan,
                "kabupaten" to produk.kabupaten,
                "provinsi" to produk.provinsi,
                "wa" to produk.wa,
                "deskripsi" to produk.deskripsi,
                "photo" to produk.photo
        )
    }

}