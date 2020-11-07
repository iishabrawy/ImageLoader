package com.weventer.app.imageloader

import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.weventer.app.imageloader.`class`.DownloadImage
import com.weventer.app.imageloader.`class`.ImageStorageManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity() {
    private var pd: ProgressDialog? = null
    private var di: DownloadImage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setup()



    }

    private fun setup() {
        loadSaveImg.setOnClickListener {
            loadSaveImg.isEnabled = false
            iv.setImageBitmap(null)
            loadImageFromInternet()
            iv.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_image_24))
        }

        getImgShow.setOnClickListener {
            iv.setImageBitmap(null)
            val bitmap = ImageStorageManager.getImageFromInternalStorage(applicationContext, "a-computer-engineer.jpg")

            if (bitmap == null) {
                iv.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_image_24))
            } else {
                iv.setImageBitmap(bitmap)
            }
        }

        deleteImg.setOnClickListener {
            iv.setImageBitmap(null)
            ImageStorageManager.deleteImageFromInternalStorage(applicationContext, "a-computer-engineer.jpg")
            iv.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_image_24))
        }
    }

    private fun loadImageFromInternet() {
        pd = ProgressDialog(this);
        pd!!.setMessage("Downloading image, please wait ...");
        pd!!.isIndeterminate = true;
        pd!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd!!.setCancelable(false);
        pd!!.setProgressNumberFormat("%1d KB/%2d KB");

        di = DownloadImage(applicationContext, pd!!, loadSaveImg)
        di!!.execute("https://images.unsplash.com/photo-1585247226801-bc613c441316?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1600&q=80")

        pd!!.setOnCancelListener {
            di!!.cancel(true)
            loadSaveImg.isEnabled = true
        }
    }
}

