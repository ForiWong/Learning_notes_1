package com.wlp.myanjunote.jetpack.workmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.wlp.myanjunote.R
import androidx.work.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val dateFormat = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
        val data = Data.Builder()
            .putString("date", dateFormat.format(Date()))
            .build()

        val request = OneTimeWorkRequest
            .Builder(UploadFileWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(request)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id)
            .observe(this, Observer<WorkInfo> { workInfo ->
                if (workInfo != null && workInfo.state.isFinished) {
                    workInfo.outputData.getString("name")?.let { Log.d("MainActivity", it) }
                }
            })
    }
}