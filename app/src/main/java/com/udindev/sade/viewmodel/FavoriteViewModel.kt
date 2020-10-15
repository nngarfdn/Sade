package com.udindev.sade.viewmodel

import androidx.lifecycle.ViewModel
import com.udindev.sade.model.Produk
import com.udindev.sade.repository.FavoriteRepository

class FavoriteViewModel : ViewModel() {

    private var favoriteRepository = FavoriteRepository()

    fun saveFavorite(produk: Produk) {favoriteRepository.addFavorite(produk)}
    fun deleteFavorite(produk: Produk) {favoriteRepository.deleteFavorite(produk)}

    fun getDataEmail() = favoriteRepository.getResultsEmail()
    fun loadResultDataEmail(email : String) = favoriteRepository.getDataEmail(email)

}