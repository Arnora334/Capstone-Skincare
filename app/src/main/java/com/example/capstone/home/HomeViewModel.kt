package com.example.capstone.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.capstone.api.ApiConfig
import com.example.capstone.result.ProcessImageRequest
import com.example.capstone.result.SkincareResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _results = MutableLiveData<SkincareResponse>()
    val results: LiveData<SkincareResponse> get() = _results

    // Fungsi ini dipanggil untuk menganalisis gambar
    fun analyzeImage(request: ProcessImageRequest) {
        // Menggunakan viewModelScope untuk meluncurkan coroutine
        viewModelScope.launch {
            try {
                // Mendapatkan API service untuk proses image
                val apiService = ApiConfig.getProcessImageApiService("your_token_here")

                // Memanggil processImage sebagai fungsi suspend
                val response = apiService.processImage(request)

                // Jika berhasil, update LiveData dengan hasil response
                if (response.isSuccessful) {
                    _results.value = response.body()
                } else {
                    Log.e("HomeViewModel", "Failed to analyze image: ${response.errorBody()?.string()}")
                }
            } catch (exception: Exception) {
                // Tangani kegagalan jaringan atau kesalahan lainnya
                Log.e("HomeViewModel", "Error analyzing image: ${exception.message}")
            }
        }
    }
}


