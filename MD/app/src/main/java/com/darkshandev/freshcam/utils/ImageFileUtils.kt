package com.darkshandev.freshcam.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import com.darkshandev.freshcam.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val timeStamp = System.currentTimeMillis().toString()
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "${System.currentTimeMillis()}.jpg")
}

fun File.asTensorInput(): ByteBuffer {
    val imagebitmap = BitmapFactory.decodeFile(this.path)

    //input shape is (100, 100, 3)
    val inputShape = intArrayOf(100, 100, 3)
    val bitmap = Bitmap.createScaledBitmap(imagebitmap, inputShape[0], inputShape[1], true)

    val input = ByteBuffer.allocateDirect(inputShape[0] * inputShape[1] * inputShape[2] * 4).order(ByteOrder.nativeOrder())
//    val input = ByteBuffer.allocateDirect(100 * 100 * 3 * 4).order(ByteOrder.nativeOrder())

        for (x in 0 until inputShape[0]) {
            for (y in 0 until inputShape[1]) {

                val px = bitmap.getPixel(x, y)

                // Get channel values from the pixel value.
                val r = Color.red(px)
                val g = Color.green(px)
                val b = Color.blue(px)

                // Normalize channel values to [-1.0, 1.0]. This requirement depends on the model.
                // For example, some models might require values to be normalized to the range
                // [0.0, 1.0] instead.
                val rf = (r - 127) / 255f
                val gf = (g - 127) / 255f
                val bf = (b - 127) / 255f

                input.putFloat(rf)
                input.putFloat(gf)
                input.putFloat(bf)

        }
    }
    return input
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)
    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}