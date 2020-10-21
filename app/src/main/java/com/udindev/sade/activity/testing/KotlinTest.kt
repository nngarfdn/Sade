package com.udindev.sade.activity.testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.udindev.sade.R
import com.udindev.sade.model.Produk
import com.udindev.sade.viewmodel.ProdukViewModel
import kotlinx.android.synthetic.main.activity_kotlin_test.*

class KotlinTest : AppCompatActivity() {
    private val TAG = KotlinTest::class.java.simpleName
    private lateinit var produkViewModel: ProdukViewModel
    private var dataProduk: ArrayList<Produk> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)

        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel::class.java)

        // coba kategorinya bukan produk

        val produk1 = Produk(nama = "Kase", kategori = "jasa", harga = 1000, alamat = "bantul")
        val produk2 = Produk(nama = "Dompet", kategori = "jasa", harga = 2000)
        val produk3 = Produk(nama = "Jagung", kategori = "jasa", harga = 3000)
        val produk4 = Produk(nama = "Telur", harga = 2500, alamat = "bantul")
        val produk5 = Produk(nama = "Apel", harga = 1500)

        val list = arrayListOf(produk1, produk2, produk3, produk4, produk5)

        btnAddAll.setOnClickListener {
//            for (produk in list) { insertData(produk) }
        }

        produkViewModel.loadResultRendahKeTinggi()
        produkViewModel.getHargaRendahKeTinggi().observe(this, Observer<List<Produk>> { result ->
           result.forEach {
               Log.d(TAG, "onCreate rendah - tinggi : ${it.nama} harganya ${it.harga}")
           }

        })

//        produkViewModel.loadResult()
//        produkViewModel.getResult().observe(this, Observer<List<Produk>> { result ->
//            Log.d(TAG, "onCreate: $result")
//            // urut data berdasar harga tertinggi
//            val short = result.sortedWith(compareByDescending { it.harga })
//            short.forEach {
//                Log.d(TAG, "onCreate tinggi - rendah : ${it.nama} harganya ${it.harga}")
//            }
//
//            //urut data harga terenda
//            val terendahKeTinggi = result.sortedWith(compareBy{ it.harga })
//            terendahKeTinggi.forEach {
//                Log.d(TAG, "onCreate rendah - tinggi : ${it.nama} harganya ${it.harga}")
//            }
//
//            val list = arrayListOf<Produk>()
//
//            terendahKeTinggi.forEach {
//                if (it.alamat == "bantul" || it.kategori == "jasa") { list.add(it) }
//            }
//
//            list.forEach {
//                 Log.d(TAG, "onCreate list jasa bantul : ${it.nama} kategori ${it.kategori} alamat ${it.alamat}")
//            }
//
////            deleteAll(result)
//            btnDeleteAll.setOnClickListener {
//            }
//
//            btnShowAll.setOnClickListener {
//            }
////            val a = result.sortedBy {  it.harga}
////            Log.d(TAG, "shortedBy : $a")
//
//        })
//        produkViewModel.loadResultByKategory("jasa")
//        getDataByKategory()

//        produkViewModel.loadResultSearchName("Dompet")
//        produkViewModel.getResultSearch()
//
//        shortData()

    }


//    private fun getDataByKategory() {
//        produkViewModel.getResultByKategori().observe(this, Observer<List<Produk>> { result ->
//            Log.d(TAG, "By Kategori : $result")
//        })
//    }
//
//    private fun insertData(produk: Produk) {
//        produkViewModel.insertProduk(produk)
//    }
//
//    private fun deleteData(produk: Produk) {
//        produkViewModel.deteteProduk(produk)
//    }
//
//    private fun deleteAll(list: List<Produk>) {
//        for (data in list) {
//            deleteData(data)
//        }
//    }
}