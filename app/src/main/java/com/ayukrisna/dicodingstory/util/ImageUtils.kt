package com.ayukrisna.dicodingstory.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FileHelper(private val context: Context) {

    private val MAXIMAL_SIZE = 1000000 //1 MB
    private val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

    private fun createCustomTempFile(): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

    fun uriToFile(imageUri: Uri): File {
        val myFile = createCustomTempFile()
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(
            buffer,
            0,
            length
        )
        outputStream.close()
        inputStream.close()
        return myFile
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }


    private fun Bitmap.getRotatedBitmap(file: File): Bitmap {
        val orientation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ExifInterface(file).getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
            )
        } else {
            ExifInterface(file.absolutePath).getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
            )
        }
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
            ExifInterface.ORIENTATION_NORMAL -> this
            else -> this
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix, true
        )
    }
}