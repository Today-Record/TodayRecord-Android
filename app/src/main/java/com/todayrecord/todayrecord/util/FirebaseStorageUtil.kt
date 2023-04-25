package com.todayrecord.todayrecord.util

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume

class FirebaseStorageUtil @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) {

    suspend fun uploadImageToFirebase(imageFile: File): String = suspendCancellableCoroutine { cancelCoroutine ->
        val storage = firebaseStorage.reference
        val imagePath = storage.child(imageFile.path)

        imagePath.putFile(Uri.fromFile(imageFile))
            .addOnSuccessListener {
                cancelCoroutine.resume(it.storage.downloadUrl.toString())
            }
            .addOnFailureListener {
                cancelCoroutine.cancel(it)
            }
    }
}