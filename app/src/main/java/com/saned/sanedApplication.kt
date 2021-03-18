package com.saned

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.saned.model.ApiService
import com.saned.view.error.SANEDError
import com.saned.view.utils.PreferenceAppHelper
import com.saned.view.utils.Utils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class sanedApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        instance=this
        apiService= ApiService.create()
        prefHelper = PreferenceAppHelper(this)

        val ceh = CoroutineExceptionHandler { _, error ->
            Log.e("errorEX", "" + error.message)
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            if (error is SANEDError) {

                Log.e("errorEX", "" + error.getErrorResponse())

                if (error.getResponseCode() == 401) {
                    Utils.logoutFromApp(applicationContext)
                } else if (error.getResponseCode() == 500) {
                    Toast.makeText(this, "Server error", Toast.LENGTH_LONG).show()
                }
            }
        }
        coroutineScope = CoroutineScope(Dispatchers.Main + ceh)

//         databaseHandler = DatabaseHandler(this)
    }

    companion object {
        lateinit var instance: sanedApplication
        lateinit var apiService: ApiService
        lateinit var prefHelper: PreferenceAppHelper
        lateinit var coroutineScope: CoroutineScope
//        lateinit var databaseHandler: DatabaseHandler

    }
}