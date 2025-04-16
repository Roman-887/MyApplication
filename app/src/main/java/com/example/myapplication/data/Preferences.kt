package com.example.myapplication.data

import android.content.Context
import java.io.File

class Preferences(private val context: Context) {

    companion object {
        private const val PREF_NAME = "MY PREFS"
        private const val PREF_LAST_PHOTO = "PREF_LAST_PHOTO"
        private const val PREF_SAVED_PHOTOS = "PREF_SAVED_PHOTOS"
    }

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveLastPhoto(savedLastPhotoPath: String) {
        pref.edit().putString(PREF_LAST_PHOTO, savedLastPhotoPath).apply()
    }

    fun getLastPhoto(): String? {
        return pref.getString(PREF_LAST_PHOTO, null)
    }

    fun savePhotoToList(savePhotoToList: String) {
        val photos = getSavedPhotos().toMutableSet()

        if (!photos.contains(savePhotoToList)) {
            photos.add(savePhotoToList)
        }

        pref.edit()
            .putStringSet(PREF_SAVED_PHOTOS, photos)
            .apply()
    }
    fun getSavedPhotos(): List<String> {
        val savedPhotos = pref.getStringSet(PREF_SAVED_PHOTOS, emptySet())
        return savedPhotos?.filter { fileExists(it) } ?: emptyList()
    }

    private fun fileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists() && file.isFile
    }

}