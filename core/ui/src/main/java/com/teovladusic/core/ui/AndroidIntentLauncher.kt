package com.teovladusic.core.ui

import android.content.Context
import android.content.Intent
import android.net.Uri

object AndroidIntentLauncher {
    /**
     * Opens send email form with the optional content
     */
    fun openSendEmailForm(
        context: Context,
        mailTo: String? = null,
        subject: String? = null,
        body: String? = null
    ) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")

            mailTo?.let { putExtra(Intent.EXTRA_EMAIL, arrayOf(it)) }
            subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
            body?.let { putExtra(Intent.EXTRA_TEXT, it) }
        }

        context.startActivity(emailIntent)
    }
    fun openDialNumber(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(intent)
    }
}
