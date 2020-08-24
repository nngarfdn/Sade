package com.udindev.sade.viewmodel

import androidx.lifecycle.ViewModel
import com.udindev.sade.model.Produk
import com.udindev.sade.repository.ProdukRepository

class ProdukViewModel : ViewModel() {

    private var produkRepository = ProdukRepository()

    fun getResult() =  produkRepository.getResults()
    fun loadResult() = produkRepository.getData()

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