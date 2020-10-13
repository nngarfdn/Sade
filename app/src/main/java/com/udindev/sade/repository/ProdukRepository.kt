package com.udindev.sade.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.udindev.sade.model.Produk

class ProdukRepository {

    private var resultData: MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultDataHargaRendahKeTinggi: MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultDataHargaTinggiKeRendah: MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultDataByKategory: MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultDataSearch: MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultDataProvinsi: MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultDataKabupaten: MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultDataKecamatan: MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultKategoriJasa : MutableLiveData<List<Produk>> = MutableLiveData()
    private var resultDataEmail : MutableLiveData<List<Produk>> = MutableLiveData()

    companion object {
        private val TAG: String = ProdukRepository::class.java.simpleName
    }

    private val db = FirebaseFirestore.getInstance()

    fun getResults(): LiveData<List<Produk>> = resultData
    fun getResultsKategoriJasa(): LiveData<List<Produk>> = resultKategoriJasa
    fun getResultsByKategory(): LiveData<List<Produk>> = resultDataByKategory
    fun getResultsSearch(): LiveData<List<Produk>> = resultDataSearch
    fun getResultsProvinsi(): LiveData<List<Produk>> = resultDataProvinsi
    fun getResultsKabupaten(): LiveData<List<Produk>> = resultDataKabupaten
    fun getResultsKecamatan(): LiveData<List<Produk>> = resultDataKecamatan
    fun getResultHargaRendahKeTinggi(): LiveData<List<Produk>> = resultDataHargaRendahKeTinggi
    fun getResultHargaTinggiKeRendah(): LiveData<List<Produk>> = resultDataHargaTinggiKeRendah
    fun getResultsEmail(): LiveData<List<Produk>> = resultDataEmail


    fun getDataKetegoriJasa() {
        val produkData: MutableList<Produk> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Produk>()
        db.collection("produk")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val kategoriDocument = document.data["kategori"] as String
                        if (kategoriDocument == "produk") {
                            val pp = document.toObject(Produk::class.java)
                            pp.id = document.id
                            savedProdukList.add(pp)
                            produkData.add(pp)
                            Log.d(TAG, "getDataByKategori size : ${savedProdukList.size}")
                            Log.d(TAG, "getDataByKategori: $pp ")
                        }
                    }
                    resultDataByKategory.value = produkData
                    Log.d(TAG, "readProduk size final getDataByKategori : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }

    fun getDataEmail(email: String) {
        val produkData: MutableList<Produk> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Produk>()
        db.collection("produk")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val namaDocument = document.data["email"] as String
                        if (namaDocument == email) {
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


    fun getDataProvinsi(provinsi: String) {
        val produkData: MutableList<Produk> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Produk>()
        db.collection("produk")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val namaDocument = document.data["provinsi"] as String
                        if (namaDocument == provinsi) {
                            val pp = document.toObject(Produk::class.java)
                            pp.id = document.id
                            savedProdukList.add(pp)
                            produkData.add(pp)
                            Log.d(TAG, "getDataProvinsi size : ${savedProdukList.size}")
                            Log.d(TAG, "getDataProvinsi: $pp ")
                        }
                    }
                    resultDataProvinsi.value = produkData
                    Log.d(TAG, "readProduk size final getDataByKategori : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }

    fun getDataKabupaten(kabupaten : String) {
        val produkData: MutableList<Produk> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Produk>()
        db.collection("produk")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val namaDocument = document.data["kabupaten"] as String
                        if (namaDocument == kabupaten) {
                            val pp = document.toObject(Produk::class.java)
                            pp.id = document.id
                            savedProdukList.add(pp)
                            produkData.add(pp)
                            Log.d(TAG, "getDataSearch size : ${savedProdukList.size}")
                            Log.d(TAG, "getDataSearch: $pp ")
                        }
                    }
                    resultDataKabupaten.value = produkData
                    Log.d(TAG, "readProduk size final getDataByKategori : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }

    fun getDataKecamatan(kecamatan: String) {
        val produkData: MutableList<Produk> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Produk>()
        db.collection("produk")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val namaDocument = document.data["kecamatan"] as String
                        if (namaDocument == kecamatan) {
                            val pp = document.toObject(Produk::class.java)
                            pp.id = document.id
                            savedProdukList.add(pp)
                            produkData.add(pp)
                            Log.d(TAG, "getDataSearch size : ${savedProdukList.size}")
                            Log.d(TAG, "getDataSearch: $pp ")
                        }
                    }
                    resultDataKecamatan.value = produkData
                    Log.d(TAG, "readProduk size final getDataByKategori : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }

    fun getDataSearch(namaProduk: String) {
        val produkData: MutableList<Produk> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Produk>()
        db.collection("produk")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val namaDocument = document.data["nama"] as String
                        if (namaDocument == namaProduk) {
                            val pp = document.toObject(Produk::class.java)
                            pp.id = document.id
                            savedProdukList.add(pp)
                            produkData.add(pp)
                            Log.d(TAG, "getDataSearch size : ${savedProdukList.size}")
                            Log.d(TAG, "getDataSearch: $pp ")
                        }
                    }
                    resultDataByKategory.value = produkData
                    Log.d(TAG, "readProduk size final getDataByKategori : ${savedProdukList.size}")
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
                            Log.d(TAG, "getDataByKategori size : ${savedProdukList.size}")
                            Log.d(TAG, "getDataByKategori: $pp ")
                        }
                    }
                    resultDataByKategory.value = produkData
                    Log.d(TAG, "readProduk size final getDataByKategori : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }

    fun getTinggiKeRendah() {
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
                        Log.d(TAG, "getTinggiKeRendah size: ${savedProdukList.size}")
                        Log.d(TAG, "getTinggiKeRendah: $pp ")
                    }
                    resultDataHargaTinggiKeRendah.value = produkData.sortedWith(compareByDescending { it.harga })
                    Log.d(TAG, "readProduk size final getTinggiKeRendah : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }

    fun getRendahKeTinggi() {
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
                        Log.d(TAG, "getRendahKeTinggi size : ${savedProdukList.size}")
                        Log.d(TAG, "getRendahKeTinggi: $pp ")
                    }
                    resultDataHargaRendahKeTinggi.value = produkData.sortedWith(compareBy { it.harga })
                    Log.d(TAG, "readProduk size final getRendahKeTinggi : ${savedProdukList.size}")
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
                        Log.d(TAG, "getData size : ${savedProdukList.size}")
                        Log.d(TAG, "getData: $pp ")
                    }
                    resultData.value = produkData
                    Log.d(TAG, "readProduk size final getData : ${savedProdukList.size}")
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

}