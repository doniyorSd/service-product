package com.example.sero_service_admin.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.example.sero_service_admin.BuildConfig
import com.example.sero_service_admin.R
import com.example.sero_service_admin.databinding.BottomPermissionBinding
import com.example.sero_service_admin.databinding.FragmentAddNewProductBinding
import com.example.sero_service_admin.model.Product
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddNewProductFragment : Fragment() {

    private lateinit var binding: FragmentAddNewProductBinding
    lateinit var photoUri: Uri

    lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase
    var imgUrl: String? = null
    lateinit var storageRef: StorageReference

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),    // contract for requesting 1 permission
        ::onGotCameraPermissionResult
    )

    private val requestReadPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),    // contract for requesting 1 permission
        ::onGotReadExternalStoragePermissionResult
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_product, container, false)
        binding = FragmentAddNewProductBinding.bind(view)

        val storage = FirebaseStorage.getInstance()

        storageRef = storage.getReference("images")

        database = Firebase.database
        myRef = database.getReference("products")


        binding.ivProduct.setOnClickListener {
            val bottomSheet = BottomSheetDialog(requireContext())
            val bottomView = layoutInflater.inflate(R.layout.bottom_permission, binding.root, false)
            bottomSheet.setContentView(bottomView)

            bottomSheet.show()
            val bind = BottomPermissionBinding.bind(bottomView)

            bind.tvCancel.setOnClickListener {
                bottomSheet.dismiss()
            }
            bind.ivCamera.setOnClickListener {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                bottomSheet.dismiss()
            }
            bind.ivGallery.setOnClickListener {
                requestReadPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                bottomSheet.dismiss()
            }
        }

        binding.addButton.setOnClickListener {
            val productName = binding.inEtProductName.text.toString()
            val productCount = binding.inEtProductCount.text.toString()

            if (productName.isNotBlank() && productCount.isNotBlank() && imgUrl != null) {
                val key = myRef.push().key

                val product = Product(
                    key, productName, imgUrl, 0, productCount.toInt()
                )
                myRef.child(key!!).setValue(product)
                Toast.makeText(requireContext(), "Qoshildi", Toast.LENGTH_SHORT).show()
                binding.inEtProductName.text?.clear()
                binding.inEtProductCount.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Bo'sh maydonlarni to'ldiring", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            binding.ivProduct.setImageURI(uri)

            addToFireStorage(uri)
        }

    private fun addToFireStorage(uri: Uri) {
        val time = System.currentTimeMillis()
        val uploadTask = storageRef.child(time.toString()).putFile(uri)

        uploadTask.addOnSuccessListener {
            if (it.task.isSuccessful) {
                it.metadata?.reference?.downloadUrl?.addOnSuccessListener { url ->
                    imgUrl = url.toString()
                }
            }
        }
    }

    private fun getImageFromGallery() {
        getImageContent.launch("image/*")
    }

    private fun onGotReadExternalStoragePermissionResult(
        granted: Boolean
    ) {
        if (granted) {
            getImageFromGallery()
        } else {
            // example of handling 'Deny & don't ask again' user choice
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                askUserForOpeningAppSettings()
            } else {
                Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun onGotCameraPermissionResult(
        granted: Boolean
    ) {
        if (granted) {
            getImageFromCamera()
        } else {
            // example of handling 'Deny & don't ask again' user choice
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                askUserForOpeningAppSettings()
            } else {
                Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun askUserForOpeningAppSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        if (requireContext().packageManager.resolveActivity(
                appSettingsIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            Toast.makeText(
                requireContext(),
                R.string.permissions_denied_forever,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.permission_denied)
                .setMessage(R.string.permission_denied_forever_message)
                .setPositiveButton(R.string.open) { _, _ ->
                    startActivity(appSettingsIntent)
                }
                .create()
                .show()
        }
    }

    private fun getImageFromCamera() {
        val imageFile = createImageFile()
        photoUri =
            FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, imageFile)
        getTakeImageContent.launch(photoUri)
    }

    private var getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                binding.ivProduct.setImageURI(photoUri)
                addToFireStorage(photoUri)
            }
        }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        //...../JPEG_13.08.2022.jpg
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
}