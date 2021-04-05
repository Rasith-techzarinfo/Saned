package com.saned.view.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.NestedScrollView
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.android.material.button.MaterialButton
import com.saned.R
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.ui.activities.LoginActivity
import java.io.File
import java.io.FileOutputStream
import java.math.RoundingMode
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.coroutines.coroutineContext
import kotlin.math.roundToInt


class Utils {

    companion object {

        fun isValidPassword(password: String): Boolean {
            val passwordPattern =
                "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$"

            return !TextUtils.isEmpty(password) && password.matches(passwordPattern.toRegex())
        }


        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        }

        // For Checking Internet Availability....
        fun isInternetAvailable(c: Context): Boolean {

            val connectivity = c
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivity != null) {

                val info = connectivity.allNetworkInfo
                if (info != null)
                    for (i in info.indices)
                        if (info[i].state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
            }

            return false
        }

        fun backButtonOnToolbar(mActivity: AppCompatActivity) {
            if (mActivity.supportActionBar != null) {
                mActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                mActivity.supportActionBar!!.setDisplayShowHomeEnabled(true)
            }
        }

        fun hideKeyBoard(view: View, mActivity: AppCompatActivity) {
            val imm = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun doubleDigitNumber(dbDate: Int): String {

            var initialNumber = ""

            if (dbDate < 10) {
                initialNumber = "0$dbDate"
            } else {
                initialNumber = "" + dbDate
            }


            return initialNumber
        }

        fun convertCustomFileSize(file: File, maxSize: Int, imageSavedFileName: String): File {
            val image = BitmapFactory.decodeFile(file.absolutePath)
            var fileOut: File? = null


            var width = image.width.toFloat()
            var height = image.height.toFloat()

            val bitmapRatio = width / height as Float
            if (bitmapRatio > 1) {
                width = maxSize.toFloat()
                height = (width / bitmapRatio)
            } else {
                height = maxSize.toFloat()
                width = (height * bitmapRatio)
            }

            val cBitmap = Bitmap.createScaledBitmap(image, width.toInt(), height.toInt(), true)

            try {
                val file_path =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/Netwo"
                val dir = File(file_path)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                fileOut = File(dir, "" + imageSavedFileName + "" + ".png")
                val fOut = FileOutputStream(fileOut)
                cBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
                fOut.flush()
                fOut.close()

            } catch (e: Exception) {
                Log.e("Exceptione", "" + e.message)
            }
            return fileOut!!
        }

//        fun convertCustomImageSize(image: Bitmap, maxSize: Int, imageSavedFileName: String): File {
//
//            var fileOut: File? = null
//
//
//            var width = image.width.toFloat()
//            var height = image.height.toFloat()
//
//            val bitmapRatio = width / height as Float
//            if (bitmapRatio > 1) {
//                width = maxSize.toFloat()
//                height = (width / bitmapRatio)
//            } else {
//                height = maxSize.toFloat()
//                width = (height * bitmapRatio)
//            }
//
//            val cBitmap = Bitmap.createScaledBitmap(image, width.toInt(), height.toInt(), true)
//
//            try {
//                val file_path =
//                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/dems"
//                val dir = File(file_path)
//                if (!dir.exists()) {
//                    dir.mkdirs()
//                }
//                fileOut = File(dir, "" + imageSavedFileName + "" + ".png")
//                val fOut = FileOutputStream(fileOut)
//                cBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
//                fOut.flush()
//                fOut.close()
//
//            } catch (e: Exception) {
//                Log.e("Exceptione", "" + e.message)
//            }
//            return fileOut!!
//        }
//
//        fun getBitmapFromURL(urlname: String): Bitmap? {
//            var bitmap: Bitmap? = null
//            try {
//                val url = URL(urlname)
//                val connection = url.openConnection() as HttpURLConnection
//                connection.setDoInput(true)
//                connection.connect()
//                val input = connection.getInputStream()
//                bitmap = BitmapFactory.decodeStream(input)
//                return bitmap
//            } catch (e: IOException) {
//                e.printStackTrace()
//                Log.e("Exception",""+e.message)
//                return null
//            }
//            return bitmap
//        }

        fun screenSize(activity: Activity): Int {
            val dm = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(dm)
            val x = Math.pow((dm.widthPixels / dm.xdpi).toDouble(), 2.0)
            val y = Math.pow((dm.heightPixels / dm.ydpi).toDouble(), 2.0)
            return Math.sqrt(x + y).roundToInt()
        }

        fun logoutFromApp(context: Context?) {
            prefHelper.setIsLogin("0")
            prefHelper.setBearerToken("")
            prefHelper.setUserPassword("")
            prefHelper.setUserId("")
            prefHelper.setUserName("")
            prefHelper.setUserProfile("")
//            databaseHandler.deleteCartTable()
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context!!.startActivity(intent)
        }

        fun startShimmer(
            shimmerFrameLayout: ShimmerFrameLayout,
            nestedScrollView: NestedScrollView
        ) {
            nestedScrollView.visibility = View.GONE
            shimmerFrameLayout.visibility = View.VISIBLE
            shimmerFrameLayout.startShimmer()

        }

        fun stopShimmer(
            shimmerFrameLayout: ShimmerFrameLayout,
            nestedScrollView: NestedScrollView
        ) {
            nestedScrollView.visibility = View.VISIBLE
            shimmerFrameLayout.visibility = View.GONE
            shimmerFrameLayout.stopShimmer()

        }

        fun startShimmerRL(
            shimmerFrameLayout: ShimmerFrameLayout,
            relativeLayout: RelativeLayout
        ) {
            relativeLayout.visibility = View.GONE
            shimmerFrameLayout.visibility = View.VISIBLE
            shimmerFrameLayout.startShimmer()

        }

        fun stopShimmerRL(
            shimmerFrameLayout: ShimmerFrameLayout,
            nestedScrollView: RelativeLayout
        ) {
            nestedScrollView.visibility = View.VISIBLE
            shimmerFrameLayout.visibility = View.GONE
            shimmerFrameLayout.stopShimmer()

        }


        fun convertStrtoDate(inputString: String): Date {
            var formattedDate: Date = Date()
            val format =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            try {
                val date = format.parse(inputString)
                formattedDate = date
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return formattedDate
        }

        fun convertDbtoTimestamp(str_date: String):Long {
            var formattedDate: Long = 0L
            try{
                val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val date = formatter.parse(str_date) as Date
                formattedDate = date.time
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }
            return formattedDate
        }

        //time ago
        fun convertDbtoTimeAgo(str_date: String): String {
            var formattedAgo: String = ""
            try {
                val timeInMillis = convertDbtoTimestamp(str_date)
                val timeAgo: String = TimeAgo.using(timeInMillis)
                formattedAgo =  if(timeAgo == "50 years ago")  "" else timeAgo
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }

            return formattedAgo
        }

        fun convertDbtoTimeAgo2(str_date: String): String {
            var formattedAgo: String = ""
            try {
                var timeAgo2 = TimeAgo2()
                formattedAgo = timeAgo2.covertTimeToText(str_date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }

            return formattedAgo
        }

        fun isYesterday(d: Date): Boolean {
            return DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)
        }

        fun convertDbtoChatTime(str_date: String): String {
            var formattedAgo: String = ""
            try {
                val timeInMillis = convertDbtoTimestamp(str_date)

                //check today/yesterday
                if(DateUtils.isToday(timeInMillis)){
                    formattedAgo = "${meetingFancyStartDateTimeOnly(str_date)}"
                } else if(DateUtils.isToday(timeInMillis + DateUtils.DAY_IN_MILLIS)){
                    formattedAgo = "yesterday"

                } else {
                    formattedAgo = "${convertDbtoNotmalSepDate(str_date)}"
                }


            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }

            return formattedAgo
        }


        fun convertDate(inputString: String): String {

            val originalFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH)
            val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = originalFormat.parse(inputString)
            val formattedDate = targetFormat.format(date)


            return "" + formattedDate

        }



        fun convertDbtoNotmalDate(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd MMM yyyy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun convertDbtoNormalDateTime(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd MMM yyyy hh:mm aaa")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun convertDbtoNormalDateTime1(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd MMM, hh:mm aaa")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun convertNormalDbtoMonthDate(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd MMM yyyy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun convertDbtoNotmalSepDate(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd/MM/yyyy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }
            return formattedDate
        }

        //added by aravind

        fun convertDbtoNormalDate(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd MMM yyyy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun convertDbtoDate(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun convertDbtoDayName(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("EEE, MM/yy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun getDateFromSchedule(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun getMonthFromSchedule(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("MMM")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }


        fun getScheduleDateFromDB(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd-MM-yyyy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun getScheduleDateFromDBBreakdown(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd-MM-yyyy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun getScheduleTimeFromDB(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("hh:mm a")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }


        fun timeourError(activity: Activity, context: Context) {

            val builder = AlertDialog.Builder(activity)

            builder.setTitle("Error")

            builder.setMessage("Server Error!! Please Try Again Later")

            builder.setPositiveButton("Okay") { dialog, which ->

                /*    activity.finishAffinity()
                    activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
                dialog.dismiss()
            }
            /*builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }*/

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            dialog.show()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(context.resources.getColor(R.color.colorPrimary))
            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(context.resources.getColor(R.color.colorPrimary))

        }


        fun meetingTime(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("hh:mm a")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun meetingDate(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd-MM-yyyy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }


        fun meetingStartDateTime(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd MMM yyyy | hh:mm a")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun meetingFancyStartDateTime(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd | hh:mm a")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun meetingFancyStartDateOnly(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun meetingFancyStartDateTimeOnly(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("hh:mm a")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }


        fun meetingDateCheck(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd MMM yyyy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun getTodayDate(): String {

            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd-MM-yyyy")
            val formattedDate = df.format(c)


            return "" + formattedDate

        }

        fun getCurrentTime(): String {

            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("hh:mm a")
            val formattedDate = df.format(c)


            return "" + formattedDate

        }

        fun getRandomNumberString(): String {
            // It will generate 6 digit random Number.
            // from 0 to 999999
            val rnd = Random()
            val number = rnd.nextInt(999999)

            // this will convert any number sequence into 6 character.
            return String.format("%06d", number)
        }

        fun getCurrentTimsssse(inputDate: String): String {

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

            sdf.timeZone = TimeZone.getTimeZone("GMT")

            val time = sdf.parse(inputDate).time

            val now = System.currentTimeMillis()

            val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)


            return "" + ago

        }

        fun getDayOfWeek(): String {

            val c = Calendar.getInstance()
            c.get(Calendar.DAY_OF_WEEK)
            var c1 = c.get(Calendar.DAY_OF_WEEK) - 1

            return "" + c1

        }

        fun getDayOfWeekPlusOne(): String {

            val c = Calendar.getInstance()
            c.get(Calendar.DAY_OF_WEEK)
            var c1 = c.get(Calendar.DAY_OF_WEEK)

            return "" + c1

        }

        fun getFancyTodayDate(): String {

            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd MMM yyyy")
            val formattedDate = df.format(c)


            return "" + formattedDate

        }

        fun getTomorowDate(): String {

            val c = Calendar.getInstance()

            c.add(Calendar.DATE, 1)

            val c2 = c.time
            val df = SimpleDateFormat("dd-MM-yyyy")

            val formattedDate = df.format(c2)


            return "" + formattedDate

        }

        fun getFancyTomorowDate(): String {

            val c = Calendar.getInstance()

            c.add(Calendar.DATE, 1)

            val c2 = c.time
            val df = SimpleDateFormat("dd MMM yyyy")

            val formattedDate = df.format(c2)


            return "" + formattedDate

        }

        fun currentToUTC(input: String): String {
            var formattedDate: String = ""

            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                targetFormat.timeZone = TimeZone.getTimeZone("UTC")

                val date = originalFormat.parse(input)

                formattedDate = targetFormat.format(date)
                Log.e("formattedDate", "" + formattedDate)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }

            return "" + formattedDate
        }

        fun utcToNormalDate(inputString: String): String {

            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val timeZone = Calendar.getInstance().timeZone.id

                val date = originalFormat.parse(inputString)

                val local = Date(date.time + TimeZone.getTimeZone(timeZone).getOffset(date.time))

                formattedDate = targetFormat.format(local)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun getTodayTime(): String {

            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd-MM-yyyy")
            val formattedDate = df.format(c)


            return "" + formattedDate

        }


        fun displayDate(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd MMM yyyy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun displayExpiryDate(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd MMM yyyy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }


        fun displayDbtoNormalDate(inputString: String): String {
            var formattedDate: String = ""
            try {
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd-MM-yyyy")
                val date = originalFormat.parse(inputString)
                formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return "" + formattedDate

        }

        fun displayDateObj(inputString: String): Date {
            var date:Date= Date()
            try {
                val originalFormat =
                    SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                val targetFormat = SimpleDateFormat("dd MMM yyyy")
                date = originalFormat.parse(inputString)
                //   formattedDate = targetFormat.format(date)
            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }



            return date
        }


        fun setWhiteText(activity: Activity, toolBar: Toolbar, context: Context) {
            toolBar.setTitleTextColor(activity.resources.getColor(android.R.color.white))

            val drawable = toolBar.navigationIcon

            if (drawable != null) {
                drawable.mutate()
                drawable.setColorFilter(
                    ContextCompat.getColor(context, android.R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }

        //file size from uri or local
        val File.size get() = if (!exists()) 0.0 else length().toDouble()
        val File.sizeInKb get() = size / 1024
        val File.sizeInMb get() = sizeInKb / 1024
        val File.sizeInGb get() = sizeInMb / 1024
        val File.sizeInTb get() = sizeInGb / 1024

        fun Uri.asFile(): File = File(toString())

        fun String?.asUri(): Uri? {
            try {
                return Uri.parse(this)
            } catch (e: Exception) {
            }
            return null
        }

        fun File.sizeStr(): String = size.toString()
        fun File.sizeStrInKb(decimals: Int = 0): String = "%.${decimals}f".format(sizeInKb)
        fun File.sizeStrInMb(decimals: Int = 0): String = "%.${decimals}f".format(sizeInMb)
        fun File.sizeStrInGb(decimals: Int = 0): String = "%.${decimals}f".format(sizeInGb)

        fun File.sizeStrWithBytes(): String = sizeStr() + "b"
        fun File.sizeStrWithKb(decimals: Int = 0): String = sizeStrInKb(decimals) + "Kb"
        fun File.sizeStrWithMb(decimals: Int = 0): String = sizeStrInMb(decimals) + "Mb"
        fun File.sizeStrWithGb(decimals: Int = 0): String = sizeStrInGb(decimals) + "Gb"

        //alter method 1
        fun getFileSize1(path: String) : String {
            var sizeInKB: String = ""
            try {
                val file = File(path)
                var length = file.length()
                length /= 1024
                sizeInKB = length.toString()
                Log.e("FileSizeFun", "File Path : " + file.path + ", File size : " + length + " KB")
            }
            catch (e: Exception) {
                Log.e("FileSizeFun", "File not found : " + e.message + e)
            }
            return "" + sizeInKB
        }

        // extension function - drawable color change
        fun Drawable.tint(context: Context, @ColorRes color: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                DrawableCompat.setTint(this, context.resources.getColor(color, context.theme))
            }
        }

        /**
         * formats the bytes to a human readable format
         *
         * @param si true if each kilo==1000, false if kilo==1024
         */
        @SuppressLint("DefaultLocale")
        fun humanReadableByteCount(bytes: Long, si: Boolean): String? {
            val unit = if (si) 1000.0 else 1024.0
            if (bytes < unit)
                return "$bytes B"
            var result = bytes.toDouble()
            val unitsToUse = (if (si) "k" else "K") + "MGTPE"
            var i = 0
            val unitsCount = unitsToUse.length
            while (true) {
                result /= unit
                if (result < unit || i == unitsCount - 1)
                    break
                ++i
            }
            return with(StringBuilder(9)) {
                append(String.format("%.1f ", result))
                append(unitsToUse[i])
                if (si) append('B') else append("iB")
            }.toString()
        }

        //toolbar height (not use)
        open fun getToolbarHeight(context: Context): Int {
            val styledAttributes: TypedArray =
                context.theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
            val toolbarHeight = styledAttributes.getDimension(0, 0F).toInt()
            styledAttributes.recycle()
            return toolbarHeight
        }

//        fun getTabsHeight(context: Context): Int {
//            return context.resources.getDimension(R.dimen.tabsHeight).toInt()
//        }


        //multiply double
        fun multiplyDoubleValues(value1: String, value2: String): String {
            var formattedString = ""
            try{
                var formattedDouble = (java.lang.Double.parseDouble(value1) * java.lang.Double.parseDouble(
                    value2
                ))

                //round to 2 values
                val df = DecimalFormat("0.00")
                df.roundingMode = RoundingMode.CEILING
                val price2: Double = df.format(formattedDouble).toDouble()
                formattedString = price2.toString()


            }catch (e: NumberFormatException) {
                // p did not contain a valid double
                Log.e("error", "" + e.message.toString())
            }

            return formattedString
        }

        // sum doubles
        fun sumDoubleValues(value1: String, value2: String): String {
            var formattedString = ""
            try{
                var formattedDouble = (java.lang.Double.parseDouble(value1) + java.lang.Double.parseDouble(
                    value2
                ))


                //round to 2 values
                val df = DecimalFormat("0.00")
                df.roundingMode = RoundingMode.CEILING
                val price2: Double = df.format(formattedDouble).toDouble()
                formattedString = price2.toString()

            }catch (e: NumberFormatException) {
                // p did not contain a valid double
                Log.e("error", "" + e.message.toString())
            }

            return formattedString
        }

        // sub doubles
        fun subDoubleValues(value1: String, value2: String): String {
            var formattedString = ""
            try{
                var formattedDouble = (java.lang.Double.parseDouble(value1) - java.lang.Double.parseDouble(
                    value2
                ))


                //round to 2 values
                val df = DecimalFormat("0.00")
                df.roundingMode = RoundingMode.CEILING
                val price2: Double = df.format(formattedDouble).toDouble()
                formattedString = price2.toString()


            }catch (e: NumberFormatException) {
                // p did not contain a valid double
                Log.e("error", "" + e.message.toString())
            }

            return formattedString
        }

        fun roundDouble2Places(value: String): String {
            var formattedString: String = ""

            try {
                //round to 2 values
                val df = DecimalFormat("0.00")
                df.roundingMode = RoundingMode.CEILING
                val price2: Double = df.format(java.lang.Double.parseDouble(value)).toDouble()
                formattedString = price2.toString()

            } catch (e: Exception){
                Log.e("error", "" + e.message.toString())
            }

            return formattedString
        }

        //set margin for any view
        fun setViewMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
            if (v.layoutParams is ViewGroup.MarginLayoutParams)
            {
                val p = v.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(l, t, r, b)
                v.requestLayout()
            }
        }


        fun dpToPx(context: Context, dp: Float): Int {
            // Took from http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
            val scale = context.resources.displayMetrics.density
            return (dp * scale + 0.5f).toInt()
        }


//        fun themeColorSwitch(hexColor: String, activity: Activity) {
//            val CYAN_THEME = "cyan"
//            val SECONDARY_THEME = "secondary"
//
//            //primary - picked color, primaryDark - color with opacity
//            when (hexColor.toUpperCase()) {
//                "#00B0F0" -> {
//                    prefHelper.setCurrentTheme(CYAN_THEME)
//                    prefHelper.setCurrentPrimaryDarkColor("#0f8fc6")
//                    prefHelper.setCurrentAccentColor("#0f8fc6")
//                    prefHelper.setCurrentPrimaryColor(hexColor)
//                }
//                else -> {  //hex code other than #00B0F0
//                    prefHelper.setCurrentTheme(SECONDARY_THEME)
//                    prefHelper.setCurrentPrimaryDarkColor("#FF${hexColor.substring(1)}")
//                    prefHelper.setCurrentAccentColor("#FF${hexColor.substring(1)}")
//                    prefHelper.setCurrentPrimaryColor("#88${hexColor.substring(1)}")
//                }
//            }
//
//            //update global var
//            isThemeUpdated == "1"
//
//            //recreate view after selection
//            recreate(activity)
//        }


        fun isWithinRange(startDate: String, endDate: String): Boolean {

            var today = Calendar.getInstance().time
            var fromDate: Date = Date()
            var toDate: Date= Date()
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val formattedDate = formatter.format(today)
                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                today = originalFormat.parse(formattedDate)

                //from, to date
                fromDate = originalFormat.parse(startDate)
                toDate = originalFormat.parse(endDate)
//                Log.e("dateformat", "$fromDate $toDate $today ${!(today.before(fromDate) || today.after(toDate))}")

            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }

            return !(today.before(fromDate) || today.after(toDate))
        }

        fun dateDifference(date1: Date, date2: Date): HashMap<String, String>{

            var timehashmap: HashMap<String, String> = HashMap()

            try{
                val diff: Long = date1.getTime() - date2.getTime()
                val seconds = diff / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                val days = hours / 24
                timehashmap["seconds"] = seconds.toString()
                timehashmap["minutes"] = minutes.toString()
                timehashmap["hours"] = hours.toString()
                timehashmap["days"] = days.toString()

                Log.e("diff", "$days $hours $minutes $seconds")

            } catch (e: Exception) {
                Log.e("Exception", "" + e.message)
            }

            return timehashmap
        }

        //1 minute = 60 seconds
        //1 hour = 60 x 60 = 3600
        //1 day = 3600 x 24 = 86400
        fun printDateDifference(startDate: Date, endDate: Date): HashMap<String, String> {

            var timehashmap: HashMap<String, String> = HashMap()

            //milliseconds
            var different = endDate.time - startDate.time
            println("startDate : $startDate")
            println("endDate : $endDate")
            println("different : $different")
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            val elapsedDays = different / daysInMilli
            different = different % daysInMilli
            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli
            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli
            val elapsedSeconds = different / secondsInMilli
            System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
            )

            timehashmap["seconds"] = elapsedSeconds.toString()
            timehashmap["minutes"] = elapsedMinutes.toString()
            timehashmap["hours"] = elapsedHours.toString()
            timehashmap["days"] = elapsedDays.toString()

            return timehashmap
        }

        fun tintMyDrawable(drawable: Drawable?, color: Int): Drawable? {
            var drawable = drawable
            drawable = DrawableCompat.wrap(drawable!!)
            DrawableCompat.setTint(drawable, color)
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
            return drawable
        }

        fun <T> Context.openActivity(it: Class<T>, activity: Activity, extras: Bundle.() -> Unit = {}) {
            val intent = Intent(this, it)
            intent.putExtras(Bundle().apply(extras))
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
//            activity.overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
        }

        fun <T> Context.openActivityWithResult(it: Class<T>, activity: Activity, reqCode: Int, extras: Bundle.() -> Unit = {}, ) {
            val intent = Intent(this, it)
            intent.putExtras(Bundle().apply(extras))
            startActivityForResult(activity, intent, reqCode, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
        }


        fun <T> Context.openActivityWithFlag(it: Class<T>, activity: Activity, flags: Int,  extras: Bundle.() -> Unit = {}) {
            val intent = Intent(this, it)
            intent.flags = flags
            intent.putExtras(Bundle().apply(extras))
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
//            activity.overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
        }

        fun checkNetworkDialog(context: Context, activity: Activity) {
            if (!isInternetAvailable(context)) {
                var networkDialog = Dialog(context)
                networkDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                networkDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                networkDialog?.setContentView(R.layout.no_internet_layout)
                networkDialog?.setCancelable(false)
                val okayButton = networkDialog!!.findViewById(R.id.okayButton) as MaterialButton
                okayButton.setOnClickListener {
                    if (isInternetAvailable(context)) {
                        networkDialog?.dismiss()
                    }
                }
                if (!activity.isFinishing) {
                    networkDialog?.show()
                }
            }
        }

    }
}

class TimeAgo2 {
    fun covertTimeToText(dataDate:String):String {
        var convTime:String = ""
        var prefix = ""
        var suffix = "Ago"
        try
        {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val pasTime = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.getTime() - pasTime.getTime()
            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60)
            {
                convTime = "$second s"
            }
            else if (minute < 60)
            {
                convTime = "$minute m"
            }
            else if (hour < 24)
            {
                convTime = "$hour h"
            }
            else if (day >= 7)
            {
                if (day > 360)
                {
                    convTime = "${(day / 360)} y"
                }
                else if (day > 30)
                {
                    convTime =   "${(day / 30)} m"
                }
                else
                {
                    convTime =  "${(day / 7)} w"
                }
            }
            else if (day < 7)
            {
                convTime = "$day d"
            }
        }
        catch (e:ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message!!)
        }
        return convTime
    }
}