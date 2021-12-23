package com.spamdetector.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spamdetector.R
import com.spamdetector.adapters.SingleGroupAdapter
import com.spamdetector.constants.Constants
import com.spamdetector.constants.SmsContract
import com.spamdetector.services.UpdateSMSService

class SmsDetailedView : AppCompatActivity(),
        LoaderManager.LoaderCallbacks<Cursor?>,
        View.OnClickListener {

    private var contact: String? = null
    private var savedContactName: String? = null
    private var singleGroupAdapter: SingleGroupAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var etMessage: EditText? = null
    private var btSend: ImageView? = null
    private var message: String? = null
    private var from_reciever = false

    private var _Id: Long = 0
    private var color = 0
    private var read: String? = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_detailed_view)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()
    }

    private fun init() {
        val intent = intent
        contact = intent.getStringExtra(Constants.CONTACT_NAME)
        savedContactName = intent.getStringExtra(Constants.SAVED_CONTACT_NAME)
        _Id = intent.getLongExtra(Constants.SMS_ID, -123)
        color = intent.getIntExtra(Constants.COLOR, 0)
        read = intent.getStringExtra(Constants.READ)
        from_reciever = intent.getBooleanExtra(Constants.FROM_SMS_RECIEVER, false)
        if (supportActionBar != null) {
            if (savedContactName == null)
                supportActionBar!!.setTitle(contact)
            else
                supportActionBar!!.setTitle(savedContactName)
        }
        recyclerView = findViewById(R.id.recyclerview)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        recyclerView?.layoutManager = linearLayoutManager
        setRecyclerView(null)
        if (read != null && read == "0") setReadSMS()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (from_reciever) startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setRecyclerView(cursor: Cursor?) {
        singleGroupAdapter = SingleGroupAdapter(this, cursor, color, savedContactName)
        recyclerView!!.adapter = singleGroupAdapter
    }

    override fun onResume() {
        super.onResume()
        LoaderManager.getInstance(this).initLoader(Constants.CONVERSATION_LOADER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor?> {
        val selectionArgs = arrayOf(contact)
        return CursorLoader(this,
                SmsContract.ALL_SMS_URI,
                null,
                SmsContract.SMS_SELECTION,
                selectionArgs,
                null)
    }

    override fun onLoadFinished(loader: Loader<Cursor?>, cursor: Cursor?) {
        if (cursor != null && cursor.count > 0) {
            singleGroupAdapter!!.swapCursor(cursor)
        } //no sms
    }

    private fun setReadSMS() {
        val intent = Intent(this, UpdateSMSService::class.java)
        intent.putExtra("id", _Id)
        startService(intent)
    }

    override fun onLoaderReset(loader: Loader<Cursor?>) {
        singleGroupAdapter!!.swapCursor(null)
    }


    private fun requestPermissions() = if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SEND_SMS)) {
        } else {

        }
    } else {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(applicationContext,
                        "SMS failed, please try again.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        if (from_reciever) {
            startActivity(Intent(this, MainActivity::class.java))
        } else super.onBackPressed()
    }

    override fun onClick(p0: View?) {

    }
}