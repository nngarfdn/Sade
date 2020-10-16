package com.udindev.sade.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.udindev.sade.model.Favorite

class FavoriteRepository {

    private val db = FirebaseFirestore.getInstance()

    private var resultDataEmail : MutableLiveData<List<Favorite>> = MutableLiveData()
    private var resultData: MutableLiveData<List<Favorite>> = MutableLiveData()
    fun getResults(): LiveData<List<Favorite>> = resultData

    fun getResultsEmail(): LiveData<List<Favorite>> = resultDataEmail

    fun getData() {
        val produkData: MutableList<Favorite> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Favorite>()
        db.collection("favorite")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val pp = document.toObject(Favorite::class.java)
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



    fun addFavorite(favorite: Favorite) {
        val item = hashMapProduk(favorite)
        db.collection("favorite")
                .add(item)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding document", e)
                }
    }

    fun deleteFavorite(favorite: Favorite) {
        val idProduk: String? = favorite.id
        hashMapProduk(favorite)
        if (idProduk != null) {
            db.collection("favorite").document(idProduk)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error deleting document", e)
                    }
        }
    }

    fun getDataEmail(email: String) {
        val produkData: MutableList<Favorite> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val savedProdukList = ArrayList<Favorite>()
        db.collection("favorite")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val namaDocument = document.data["email"] as String
                        if (namaDocument == email ) {
                            val pp = document.toObject(Favorite::class.java)
                            pp.id = document.id
                            savedProdukList.add(pp)
                            produkData.add(pp)
                            Log.d(TAG, "getDataEmail Favorite size : ${savedProdukList.size}")
                            Log.d(TAG, "getDataEmail Favorite: $pp ")
                        }
                    }
                    resultDataEmail.value = produkData
                    Log.d(TAG, "readProduk size final getDataEmail Favorite : ${savedProdukList.size}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
    }


    private fun hashMapProduk(favorite: Favorite): HashMap<String, Any?> {
        return hashMapOf(
                "idDocument" to favorite.idDocument,
                "email" to favorite.email
        )
    }

    companion object {
        private const val TAG = "FavoriteRepository"
    }
}