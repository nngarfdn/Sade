package com.udindev.sade.viewmodel

import androidx.lifecycle.ViewModel
import com.udindev.sade.model.Favorite
import com.udindev.sade.repository.FavoriteRepository

class FavoriteViewModel : ViewModel() {

    private var favoriteRepository = FavoriteRepository()

    fun getResult() =  favoriteRepository.getResults()
    fun loadResult() = favoriteRepository.getData()

    fun saveFavorite(favorite: Favorite) {favoriteRepository.addFavorite(favorite)}
    fun deleteFavorite(favorite: Favorite) {favoriteRepository.deleteFavorite(favorite)}

    fun getDataMyFavorite() = favoriteRepository.getResultsEmail()
    fun loadResultMyFavorite(email : String) = favoriteRepository.getDataEmail(email)

}