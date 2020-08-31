package com.udindev.sade.viewmodel

import androidx.lifecycle.ViewModel
import com.udindev.sade.model.Produk
import com.udindev.sade.repository.ProdukRepository

class ProdukViewModel : ViewModel() {

    private var produkRepository = ProdukRepository()

    fun getResult() =  produkRepository.getResults()
    fun loadResult() = produkRepository.getData()
    fun getShortResult() = produkRepository.getResultsShort()
    fun loadResultShort() = produkRepository.getDataSort()
    fun getResultByKategori() = produkRepository.getResultsByKategory()
    fun loadResultByKategory(kategori : String) = produkRepository.getDataByKategori(kategori)

//    fun getResultSearch() = produkRepository.getResultsearchName()
//    fun loadResultSearchName(name : String) = produkRepository.getDataSearchName(name)

    fun insertProduk(produk: Produk) {
        produkRepository.saveProduk(produk)
    }
    fun deteteProduk(produk: Produk) {
        produkRepository.delete(produk)
    }
    fun updateProduk(produk: Produk) {
        produkRepository.update(produk)
    }

}