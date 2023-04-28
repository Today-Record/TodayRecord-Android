package com.todayrecord.todayrecord.util.file

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
        val imagePath = storage.child(imageFile.absolutePath)

        imagePath.putFile(Uri.fromFile(imageFile))
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    cancelCoroutine.resume(uri.toString())
                }.addOnFailureListener { exception ->
                    cancelCoroutine.cancel(exception)
                }
            }
            .addOnFailureListener {
                cancelCoroutine.cancel(it)
            }
    }
}