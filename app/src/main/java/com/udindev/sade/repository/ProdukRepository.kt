package com.udindev.sade.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.udindev.sade.model.Produk

class ProdukRepository {

    private var resultData: MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultDataShort: MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultDataByKategory: MutableLiveData<List<Produk>> = MutableLiveData()

    companion object {
        private val TAG: String = ProdukRepository::class.java.simpleName
    }

    private val db = FirebaseFirestore.getInstance()

    fun getResults(): LiveData<List<Produk>> = resultData
    fun getResultsShort(): LiveData<List<Produk>> = resultDataShort
    fun getResultsByKategory(): LiveData<List<Produk>> = resultDataByKategory


    fun getDataSort() {
        val produkData: MutableList<Produk> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Produk>()
        db.collection("produk").orderBy("nama", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val pp = document.toObject(Produk::class.java)
                        pp.id = document.id
                        savedProdukList.add(pp)
                        produkData.add(pp)
                        Log.d(TAG, "readProduk savedProdukList size : ${savedProdukList.size}")
                        Log.d(TAG, "readProduk: $pp ")
                    }
                    resultData.value = produkData
                    Log.d(TAG, "readProduk size final savedProdukList : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }


    fun getDataByKategori(kategoriii: String) {
        val produkData: MutableList<Produk> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Produk>()
        db.collection("produk")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val kategoriDocument = document.data["kategori"] as String
                        if (kategoriDocument == kategoriii) {
                            val pp = document.toObject(Produk::class.java)
                            pp.id = document.id
                            savedProdukList.add(pp)
                            produkData.add(pp)
                            Log.d(TAG, "readProduk by kategori size : ${savedProdukList.size}")
                            Log.d(TAG, "readProduk by kategori: $pp ")
                        }
                    }
                    resultData.value = produkData
                    Log.d(TAG, "readProduk size final savedProdukList : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }


    fun getData() {
        val produkData: MutableList<Produk> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Produk>()
        db.collection("produk")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val pp = document.toObject(Produk::class.java)
                        pp.id = document.id
                        savedProdukList.add(pp)
                        produkData.add(pp)
                        Log.d(TAG, "readProduk savedProdukList size : ${savedProdukList.size}")
                        Log.d(TAG, "readProduk: $pp ")
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
        val idProduk: String? = produk.id
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
                "harga" to produk.harga,
                "wa" to produk.wa,
                "deskripsi" to produk.deskripsi,
                "photo" to produk.photo
        )
    }

}