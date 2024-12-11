package com.example.capstone.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.capstone.R
import com.example.capstone.databinding.FragmentHomeBinding
import android.Manifest
import android.provider.MediaStore
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.result.IngredientsAdapter
import com.example.capstone.result.ProcessImageRequest
import com.example.capstone.result.ResultActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    // ViewModel
    private lateinit var homeViewModel: HomeViewModel

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted: Boolean ->
            if (granted) {
                Toast.makeText(requireContext(), R.string.permission_request_granted, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), R.string.permission_request_denied, Toast.LENGTH_LONG).show()
            }
        }

    private fun hasCameraPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Mengamati hasil analisis gambar
        homeViewModel.results.observe(viewLifecycleOwner, Observer { skincareResponse ->
            skincareResponse?.let {
                // Tampilkan hasil analisis gambar jika diperlukan
                it.results.forEach { result ->
                    Log.d("HomeFragment", "Predicted Rating: ${result.predicted_rating} for ${result.name}")
                }
            }
        })

        binding.galleryButton.setOnClickListener { openGallery() }
        binding.cameraButton.setOnClickListener { openCamera() }

        // Button untuk memulai analisis gambar
        binding.analyzeButton.setOnClickListener {
            imageUri?.let { uri ->
                val filePath = getFilePathFromUri(uri) // Fungsi untuk mengonversi Uri ke path file
                if (filePath != null) {
                    // Simulasi pemanggilan analisis gambar
                    val ingredientsList = listOf("Ingredient 1", "Ingredient 2", "Ingredient 3") // Gantikan dengan data hasil analisis
                    val categories = listOf("Category 1", "Category 2", "Category 3") // Gantikan dengan kategori yang diprediksi

                    // Kirim data ke ResultActivity
                    val intent = Intent(requireContext(), ResultActivity::class.java)
                    intent.putStringArrayListExtra("ingredients_list", ArrayList(ingredientsList))
                    intent.putStringArrayListExtra("categories", ArrayList(categories))
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Invalid image URI", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = createTemporaryFile(requireContext())
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            Log.e("getFilePathFromUri", "Error: ${e.message}")
            null
        }
    }


    // Fungsi untuk membuka galeri
    private fun openGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            displayImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    // Fungsi untuk membuka kamera
    private fun openCamera() {
        if (!hasCameraPermission()) {
            permissionLauncher.launch(CAMERA_PERMISSION)
        } else {
            val cameraIntent = Intent(requireContext(), CameraActivity::class.java)
            cameraActivityLauncher.launch(cameraIntent)
        }
    }

    private val cameraActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERAX_RESULT) {
            val uri = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            if (uri != null) {
                imageUri = uri
                displayImage()
            }
        }
    }

    private fun displayImage() {
        imageUri?.let {
            Log.d("Image URI", "displayImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        private const val CAMERAX_RESULT = 200
    }
}



