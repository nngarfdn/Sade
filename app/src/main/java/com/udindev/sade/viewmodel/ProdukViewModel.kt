package com.udindev.sade.viewmodel

import androidx.lifecycle.ViewModel
import com.udindev.sade.model.Produk
import com.udindev.sade.repository.ProdukRepository

class ProdukViewModel : ViewModel() {

    private var produkRepository = ProdukRepository()

    fun getResult() =  produkRepository.getResults()
    fun loadResult() = produkRepository.getData()

    fun getResultKategoriJasa() =  produkRepository.getResultsKategoriJasa()
    fun loadResultKategoriJasa() = produkRepository.getDataKetegoriJasa()

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

    fun getDataEmail() = produkRepository.getResultsEmail()
    fun loadResultDataEmail(email : String) = produkRepository.getDataEmail(email)

    fun getDataById() = produkRepository.getResultsDataById()
    fun loadResultById(idDokumen : String) = produkRepository.getDataByIdDocument(idDokumen)

    fun insertProduk(produk: Produk) { produkRepository.saveProduk(produk) }
    fun deteteProduk(idProduk: String) { produkRepository.delete(idProduk) }
    fun updateProduk(produk: Produk) { produkRepository.update(produk) }

}