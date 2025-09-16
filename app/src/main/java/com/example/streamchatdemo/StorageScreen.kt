package com.example.streamchatdemo

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.exifinterface.media.ExifInterface
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun StorageScreen() {
    val context = LocalContext.current

    var pickedUri by remember { mutableStateOf<Uri?>(null) }
    var preview by remember { mutableStateOf<Bitmap?>(null) }
    var savedPath by remember { mutableStateOf<String?>(null) }
    var status by remember { mutableStateOf<String?>(null) }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        pickedUri = uri
    }

    // When a new image is picked: decode -> fix rotation -> save -> preview
    LaunchedEffect(pickedUri) {
        val uri = pickedUri ?: return@LaunchedEffect
        status = "Decoding…"
        runCatching {
            val bmp = withContext(Dispatchers.IO) { decodeBitmap(context, uri) }
            val degrees = withContext(Dispatchers.IO) { exifRotationDegrees(context, uri) }
            val fixed = withContext(Dispatchers.IO) { rotateIfNeeded(bmp, degrees) }

            status = "Saving to cache…"
            val path = withContext(Dispatchers.IO) {
                saveToCache(context, fixed, "picked_image.jpg")
            }

            preview = fixed
            savedPath = path
            status = "Saved ✓"
        }.onFailure { e ->
            status = "Error: ${e.message}"
            preview = null
            savedPath = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Storage & EXIF demo", style = MaterialTheme.typography.titleMedium)

        Button(onClick = {
            picker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text("Pick image")
        }

        status?.let { Text(it) }

        Divider()

        preview?.let { bmp ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "Picked image (rotated per EXIF)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        savedPath?.let { path ->
            Text("Saved to cache: $path")
            Text(
                "Check in Android Studio: View > Tool Windows > Device File Explorer → " +
                        "/data/data/${context.packageName}/cache"
            )
        }
    }
}

/* ---------- helpers (IO) ---------- */

private fun decodeBitmap(context: android.content.Context, uri: Uri): Bitmap {
    return if (Build.VERSION.SDK_INT >= 28) {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
            // avoid gigantic hardware bitmaps for simple preview
            decoder.isMutableRequired = true
        }
    } else {
        @Suppress("DEPRECATION")
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }
}

/** Read EXIF orientation from the image stream and return rotation in degrees. */
private fun exifRotationDegrees(context: android.content.Context, uri: Uri): Int {
    val ins: InputStream = context.contentResolver.openInputStream(uri) ?: return 0
    ins.use {
        val exif = ExifInterface(it)
        return when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }
}

private fun rotateIfNeeded(bmp: Bitmap, degrees: Int): Bitmap {
    if (degrees == 0) return bmp
    val m = Matrix().apply { postRotate(degrees.toFloat()) }
    return Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, m, true)
}

/** Save as JPEG(90) in cache dir; returns absolute path. */
private fun saveToCache(
    context: android.content.Context,
    bitmap: Bitmap,
    fileName: String
): String {
    val file = File(context.cacheDir, fileName)
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return file.absolutePath
}
