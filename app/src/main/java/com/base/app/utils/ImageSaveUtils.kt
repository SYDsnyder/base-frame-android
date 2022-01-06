package com.base.app.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.*

object ImageSaveUtil {
    /**
     * @param context 上下文
     * @param bitmap  需要保存的bitmap
     * @param format  图片格式
     * @param quality 压缩的图片质量
     * @param recycle 完成以后，是否回收Bitmap，建议为true
     * @return 文件的 uri
     */
    fun saveAlbum(
        context: Context,
        bitmap: Bitmap,
        format: CompressFormat,
        quality: Int,
        recycle: Boolean
    ): Uri? {
        val suffix: String = if (CompressFormat.JPEG == format) "JPG" else format.name
        val fileName =
            System.currentTimeMillis().toString() + "_" + quality + "." + suffix
        return if (Build.VERSION.SDK_INT < 29) {
            if (!isGranted(context)) {
                Toast.makeText(context, "未开启存储权限", Toast.LENGTH_LONG).show()
                return null
            }
            val picDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val destFile = File(picDir, fileName)
            if (!save(bitmap, destFile, format, quality, recycle)) return null
            var uri: Uri? = null
            if (destFile.exists()) {
                uri = Uri.parse("file://" + destFile.absolutePath)
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                intent.data = uri
                context.sendBroadcast(intent)
            }
            uri
        } else {
            // Android 10 使用
            val contentUri: Uri = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else MediaStore.Images.Media.INTERNAL_CONTENT_URI
            val contentValues = ContentValues()
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
            contentValues.put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_DCIM + "/"
            )
            // 告诉系统，文件还未准备好，暂时不对外暴露
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1)
            val uri =
                context.contentResolver.insert(contentUri, contentValues) ?: return null
            var os: OutputStream? = null
            try {
                os = context.contentResolver.openOutputStream(uri)
                bitmap.compress(format, quality, os)
                // 告诉系统，文件准备好了，可以提供给外部了
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                context.contentResolver.update(uri, contentValues, null, null)
                uri
            } catch (e: Exception) {
                e.printStackTrace()
                // 失败的时候，删除此 uri 记录
                context.contentResolver.delete(uri, null, null)
                null
            } finally {
                try {
                    os?.close()
                } catch (e: IOException) {
                    // ignore
                }
            }
        }
    }

    private fun save(
        bitmap: Bitmap,
        file: File,
        format: CompressFormat,
        quality: Int,
        recycle: Boolean
    ): Boolean {
        if (isEmptyBitmap(bitmap)) {
            Logger.e("ImageUtils", "bitmap is empty.")
            return false
        }
        if (bitmap.isRecycled) {
            Logger.e("ImageUtils", "bitmap is recycled.")
            return false
        }
        if (!createFile(file, true)) {
            Logger.e("ImageUtils", "create or delete file <\$file> failed.")
            return false
        }
        var os: OutputStream? = null
        var ret = false
        try {
            os = BufferedOutputStream(FileOutputStream(file))
            ret = bitmap.compress(format, quality, os)
            if (recycle && !bitmap.isRecycled) bitmap.recycle()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
                // ignore
            }
        }
        return ret
    }

    private fun isEmptyBitmap(bitmap: Bitmap?): Boolean {
        return bitmap == null || bitmap.isRecycled || bitmap.width == 0 || bitmap.height == 0
    }

    private fun createFile(file: File?, isDeleteOldFile: Boolean): Boolean {
        if (file == null) return false
        if (file.exists()) {
            if (isDeleteOldFile) {
                if (!file.delete()) return false
            } else return file.isFile
        }
        return if (!createDir(file.parentFile)) false else try {
            file.createNewFile()
        } catch (e: IOException) {
            false
        }
    }

    private fun createDir(file: File?): Boolean {
        if (file == null) return false
        return if (file.exists()) file.isDirectory else file.mkdirs()
    }

    private fun isGranted(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}