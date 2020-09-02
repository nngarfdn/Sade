package com.udindev.sade.viewmodel

import androidx.lifecycle.ViewModel
import com.udindev.sade.model.Produk
import com.udindev.sade.repository.ProdukRepository

class ProdukViewModel : ViewModel() {

    private var produkRepository = ProdukRepository()

    fun getResult() =  produkRepository.getResults()
    fun loadResult() = produkRepository.getData()

    fun getResultByKategori() = produkRepository.getResultsByKategory()
    fun loadResultByKategory(kategori : String) = produkRepository.getDataByKategori(kategori)

    fun getResultProvinsi() = produkRepository.getResultsProvinsi()
    fun loadResultProvinsi(provinsi : String) = produkRepository.getDataProvinsi(provinsi)

    fun getResultKabupaten() = produkRepository.getResultsKabupaten()
    fun loadResultKabupaten(kabupaten : String) = produkRepository.getDataKabupaten(kabupaten)

    fun getResultKecamatan() = produkRepository.getResultsKecamatan()
    fun loadResultKecamatan(kecamatan : String) = produkRepository.getDataKecamatan(kecamatan)

    fun getResultSearch() = produkRepository.getResultsSearch()
    fun loadResultSearch(nama : String) = produkRepository.getDataSearch(nama)

    fun getHargaRendahKeTinggi() = produkRepository.getResultHargaRendahKeTinggi()
    fun loadResultRendahKeTinggi() = produkRepository.getRendahKeTinggi()

    fun getHargaTinggiKeRendah() = produkRepository.getResultHargaTinggiKeRendah()
    fun loadResultTinggiKeRendah() = produkRepository.getTinggiKeRendah()

    fun insertProduk(produk: Produk) { produkRepository.saveProduk(produk) }
    fun deteteProduk(produk: Produk) { produkRepository.delete(produk) }
    fun updateProduk(produk: Produk) { produkRepository.update(produk) }

}