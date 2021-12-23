package com.spamdetector.activities

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.spamdetector.R
import com.spamdetector.constants.Constants

class NewSMSActivity : AppCompatActivity(), View.OnClickListener {

    private var txtphoneNo: EditText? = null
    private var txtMessage: EditText? = null
    private var phoneNo: String? = null
    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()
    }

    private fun init() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {

    }

    override fun onPause() {
        super.onPause()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.RESULT_PICK_CONTACT) contactPicked(data)
        } else {
            Log.e("MainActivity", "Failed to pick contact")
        }
    }

    private fun contactPicked(data: Intent?) {
        val cursor: Cursor?
        try {
            val phoneNo: String?
            var name: String? = null
            val uri = data!!.data

            cursor = contentResolver.query(uri!!, null, null, null, null)
            cursor!!.moveToFirst()

            val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            phoneNo = cursor.getString(phoneIndex)
            name = cursor.getString(nameIndex)
            txtphoneNo!!.setText(phoneNo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}