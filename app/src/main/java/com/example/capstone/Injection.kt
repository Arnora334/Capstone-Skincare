package com.example.capstone

import android.content.Context
import com.example.capstone.User.UserPreference
import com.example.capstone.User.dataStore
import com.example.capstone.api.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }

        // Menyediakan kedua API service: satu untuk autentikasi dan satu lagi untuk gambar
        val authApiService = ApiConfig.getDefaultApiService(user.token)  // API untuk autentikasi
        val imageApiService = ApiConfig.getProcessImageApiService(user.token)  // API untuk analisis gambar

        return Repository.getInstance(pref, authApiService, imageApiService)
    }
}
