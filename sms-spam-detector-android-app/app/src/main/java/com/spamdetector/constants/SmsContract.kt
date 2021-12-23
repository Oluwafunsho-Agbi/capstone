package com.spamdetector.constants

import android.net.Uri


object SmsContract {
    @JvmField
    val ALL_SMS_URI = Uri.parse("content://sms/inbox")
    const val SMS_SELECTION = "address = ? "
    const val SORT_DESC = "date DESC"
}