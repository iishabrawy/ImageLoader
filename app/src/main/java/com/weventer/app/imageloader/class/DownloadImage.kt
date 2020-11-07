package com.weventer.app.imageloader.`class`

import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.weventer.app.imageloader.R
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class DownloadImage(
    private val c: Context,
    private val pd: ProgressDialog,
    private val btn: Button): AsyncTask<String, Int, String>() {

    override fun doInBackground(vararg p0: String?): String? {
        var `is`: InputStream? = null
        var os: OutputStream? = null
        var con: HttpURLConnection? = null
        val length: Int
        try {
            val url = URL(p0[0])
            con = url.openConnection() as HttpURLConnection
            con.connect()
            if (con.responseCode !== HttpURLConnection.HTTP_OK) {
                return "HTTP CODE: " + con.responseCode
                    .toString() + " " + con.responseMessage
            }
            length = con.contentLength
            pd.max = length / 1000
            `is` = con.inputStream
            os =
                FileOutputStream(
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator.toString() + "a-computer-engineer.jpg"
                )
            val data = ByteArray(4096)
            var total: Long = 0
            var count: Int
            while (`is`.read(data).also { count = it } != -1) {
                if (isCancelled) {
                    `is`.close()
                    return null
                }
                total += count.toLong()
                if (length > 0) {
                    publishProgress(total.toInt())
                }
                os.write(data, 0, count)
            }
        } catch (e: Exception) {
            btn.isEnabled = true
            return e.toString()
        } finally {
            try {
                os?.close()
                `is`?.close()
            } catch (ioe: IOException) {
            }
            con?.disconnect()
        }
        return null

    }

    override fun onPreExecute() {
        super.onPreExecute()
        pd.show()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        pd.isIndeterminate = false
        pd.progress = ((values[0]) ?: 0) / 1000
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        pd.dismiss()
        btn.isEnabled = true
        if (result != null) {
            Toast.makeText(c, "Download error: $result", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(c, c.getString(R.string.downloaded_successfully), Toast.LENGTH_SHORT).show()
            val b =
                BitmapFactory.decodeFile(
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator.toString() + "a-computer-engineer.jpg"
                )
            ImageStorageManager.saveToInternalStorage(c, b, "a-computer-engineer.jpg")
        }
    }
}