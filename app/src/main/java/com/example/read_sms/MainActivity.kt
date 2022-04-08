package com.example.read_sms

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    companion object
    {
        const val REQUEST_CODE = 1703
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            requestSMSPermission()
        }
        else
        {
            readSMS()
        }
    }

    private fun requestSMSPermission()
    {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.READ_SMS), REQUEST_CODE)
    }
    private fun readSMS()
    {
        val numberCol = Telephony.TextBasedSmsColumns.ADDRESS
        val textCol = Telephony.TextBasedSmsColumns.BODY
        val typeCol = Telephony.TextBasedSmsColumns.TYPE

        val projection = arrayOf(numberCol, textCol, typeCol)

        val cursor = contentResolver.query(Telephony.Sms.CONTENT_URI, projection, null, null)

        val numberColIndex = cursor!!.getColumnIndex(numberCol)
        val textColIndex = cursor.getColumnIndex(textCol)
        val typeColIndex = cursor.getColumnIndex(typeCol)

        while (cursor.moveToNext())
        {
            val number = cursor.getString(numberColIndex)
            val text = cursor.getString(textColIndex)
            val type = cursor.getString(typeColIndex)

            Log.d("SyAnh", "Number $number, Text: $text, Type: $type")
        }
        cursor.close()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE)
        {
            if(permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                readSMS()
            }
        }
    }
}