package com.udindev.sade.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.udindev.sade.model.Produk

class ProdukRepository {

    private var resultData: MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultDataByKategory: MutableLiveData<List<Produk>> = MutableLiveData()

    companion object {
        private val TAG: String = ProdukRepository::class.java.simpleName
    }

    private val db = FirebaseFirestore.getInstance()

    fun getResults(): LiveData<List<Produk>> = resultData
    fun getResultsByKategory(): LiveData<List<Produk>> = resultDataByKategory

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
                            // todo add ke list
                            val id = document.id
                            val nama = document.data["nama"] as String
                            val kategori = document.data["kategori"] as String
                            val alamat = document.data["alamat"] as String
                            val kecamatan = document.data["kecamatan"] as String
                            val kabupaten = document.data["kecamatan"] as String
                            val provinsi = document.data["provinsi"] as String
                            val wa = document.data["wa"] as String
                            val deskripso = document.data["deskripsi"] as String
                            val photo = document.data["photo"] as String
                            val produkItem = Produk(id, nama, kategori, alamat, kecamatan, kabupaten, provinsi, wa, deskripso, photo)
                            savedProdukList.add(produkItem)
                            produkData.add(produkItem)
                            Log.d(TAG, "Jumlah data $kategoriDocument : ${savedProdukList.size}")
                            Log.d(TAG, "readProdukByKategori $kategoriDocument: $produkItem ")
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
                        val id = document.id
                        val nama = document.data["nama"] as String
                        val kategori = document.data["kategori"] as String
                        val alamat = document.data["alamat"] as String
                        val kecamatan = document.data["kecamatan"] as String
                        val kabupaten = document.data["kecamatan"] as String
                        val provinsi = document.data["provinsi"] as String
                        val wa = document.data["wa"] as String
                        val deskripso = document.data["deskripsi"] as String
                        val photo = document.data["photo"] as String
                        val produkItem = Produk(id, nama, kategori, alamat, kecamatan, kabupaten, provinsi, wa, deskripso, photo)
                        savedProdukList.add(produkItem)
                        produkData.add(produkItem)
                        Log.d(TAG, "readProduk savedProdukList size : ${savedProdukList.size}")
                        Log.d(TAG, "readProduk: $produkItem ")
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
                "wa" to produk.wa,
                "deskripsi" to produk.deskripsi,
                "photo" to produk.photo
        )
    }

}