package com.base.app.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore.Images.ImageColumns
import android.text.TextUtils
import android.util.Base64
import androidx.fragment.app.Fragment
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.model.AspectRatio
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


//根据uri获取路径
fun uriToPath(context: Context, uri: Uri?): String {
    if (null == uri) return ""
    val scheme: String? = uri.scheme
    var data: String = ""
    if (scheme == null) data = uri.path!! else if (ContentResolver.SCHEME_FILE == scheme) {
        data = uri.path!!
    } else if (ContentResolver.SCHEME_CONTENT == scheme) {
        val cursor: Cursor? = context.contentResolver
            .query(uri, arrayOf(ImageColumns.DATA), null, null, null)
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndex(ImageColumns.DATA)
                if (index > -1) {
                    data = cursor.getString(index)
                }
            }
            cursor.close()
        }
    }
    return data
}

//bitmap转图片并获取图片地址
fun saveBitmap(path: String, mBitmap: Bitmap): String {
    val filePic: File
    try {
        filePic = File(path + Date().time + ".jpg")
        if (!filePic.exists()) {
            filePic.parentFile.mkdirs()
            filePic.createNewFile()
        }
        val fos = FileOutputStream(filePic)
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return ""
    }
    return filePic.absolutePath
}

//将本地图片转换成Base64
fun imageToBase64(path: String?): String? {
    if (TextUtils.isEmpty(path)) {
        return null
    }
    var `is`: InputStream? = null
    var data: ByteArray? = null
    var result: String? = null
    try {
        `is` = FileInputStream(path)
        //创建一个字符流大小的数组。
        data = ByteArray(`is`.available())
        //写入数组
        `is`.read(data)
        //用默认的编码格式进行编码
        result = Base64.encodeToString(data, Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (null != `is`) {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    return result
}

//将网络图片转换成Base64
fun imageChangeBase64(imagePath: String?): String? {
    val outPut = ByteArrayOutputStream()
    val data = ByteArray(1024)
    try {
        // 创建URL
        val url = URL(imagePath)
        // 创建链接
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.connectTimeout = 10 * 1000
        if (conn.responseCode !== 200) {
            return "fail" //连接失败/链接失效/图片不存在
        }
        val inStream: InputStream = conn.inputStream
        var len = -1
        while (inStream.read(data).also { len = it } != -1) {
            outPut.write(data, 0, len)
        }
        inStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    // 对字节数组Base64编码
    return Base64.encodeToString(outPut.toByteArray(), Base64.DEFAULT)
}

//固定比例裁剪
fun cropImage(path: String, activity: Activity, context: Context, fragment: Fragment, widthRatio: Float, heightRatio: Float) {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
    val imageFileName = "JPEG_$timeStamp.jpg"
    val options = UCrop.Options()
    options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
    options.setHideBottomControls(true)
    options.setCompressionQuality(90)
    options.setToolbarWidgetColor(Color.WHITE)
    options.setToolbarColor(Color.parseColor("#38393E"))
    options.setStatusBarColor(Color.parseColor("#38393E"))
    UCrop.of(Uri.fromFile(File(path)),
        Uri.fromFile(File(activity.cacheDir, imageFileName)))
        .withAspectRatio(widthRatio, heightRatio)
        .withOptions(options)
        .start(context,fragment)
}

//多比例裁剪
fun scaleCropImage(path: String, activity: Activity, context: Context, fragment: Fragment, vararg aspectRatio: AspectRatio) {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
    val imageFileName = "JPEG_$timeStamp.jpg"
    val options = UCrop.Options()
    options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
    options.setHideBottomControls(false)
    options.setAspectRatioOptions(0, *aspectRatio)
    options.setCompressionQuality(90)
    options.setToolbarWidgetColor(Color.WHITE)
    options.setToolbarColor(Color.parseColor("#38393E"))
    options.setStatusBarColor(Color.parseColor("#38393E"))
    UCrop.of(Uri.fromFile(File(path)),
        Uri.fromFile(File(activity.cacheDir, imageFileName)))
        .withOptions(options)
        .start(context,fragment)
}