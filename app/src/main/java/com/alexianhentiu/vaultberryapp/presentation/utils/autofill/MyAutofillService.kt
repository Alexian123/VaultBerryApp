package com.alexianhentiu.vaultberryapp.presentation.utils.autofill

import android.app.PendingIntent
import android.app.assist.AssistStructure
import android.content.Intent
import android.os.Build
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.Presentations
import android.service.autofill.SaveCallback
import android.service.autofill.SaveRequest
import android.util.Log
import android.view.autofill.AutofillId
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.alexianhentiu.vaultberryapp.presentation.activity.AutofillActivity
import kotlin.jvm.java

class MyAutofillService : AutofillService() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {
        Log.d("MyAutofillService", "onFillRequest")
        val structure = request.fillContexts.last().structure
        val component = structure.activityComponent

        val usernameId = findNode(structure, "username") ?: findNode(structure, "email")
        val passwordId = findNode(structure, "password")

        if (usernameId == null || passwordId == null) {
            Log.e("MyAutofillService", "usernameId: $usernameId, passwordId: $passwordId")
            callback.onSuccess(null)
            return
        }

        // Keywords for the AutofillActivity
        val keywords = arrayListOf(component.packageName)
        try {
            val label = packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(component.packageName, 0)
            ).toString()
            keywords.add(label)
        } catch (e: Exception) {
            Log.w("MyAutofillService", "Label not found for ${component.packageName}", e)
        }
        Log.d("MyAutofillService", "keywords: $keywords")

        val intent = Intent(this, AutofillActivity::class.java).apply {
            putStringArrayListExtra("keywords", keywords)
            putExtra("usernameId", usernameId)
            putExtra("passwordId", passwordId)
        }

        val sender = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        ).intentSender

        val presentation = RemoteViews(packageName, android.R.layout.simple_list_item_1).apply {
            setTextViewText(android.R.id.text1, "Unlock to autofill")
        }

        val presentations = Presentations.Builder()
            .setDialogPresentation(presentation)
            .setMenuPresentation(presentation)
            .build()

        val response = FillResponse.Builder()
            .setAuthentication(arrayOf(usernameId, passwordId), sender, presentations)
            .build()

        Log.d("MyAutofillService", "response: $response")
        callback.onSuccess(response)
    }


    override fun onSaveRequest(
        request: SaveRequest,
        callback: SaveCallback
    ) {
        // TODO: Save credentials entered by user
    }

    fun findNode(structure: AssistStructure, hint: String): AutofillId? {
        val nodes = structure.windowNodeCount
        for (i in 0 until nodes) {
            val node = structure.getWindowNodeAt(i).rootViewNode
            val result = traverseNode(node, hint)
            if (result != null) return result
        }
        return null
    }

    private fun traverseNode(node: AssistStructure.ViewNode, hint: String): AutofillId? {
        val hints = node.autofillHints
        if (hints != null) {
            for (h in hints) {
                if (h.contains(hint, ignoreCase = true)) {
                    return node.autofillId
                }
            }
        }
        if (hints != null && hints.contains(hint)) {
            return node.autofillId
        }

        val resourceName = node.contentDescription
        if (resourceName != null && resourceName.contains(hint, ignoreCase = true)) {
            return node.autofillId
        }

        for (i in 0 until node.childCount) {
            val child = node.getChildAt(i)
            val result = traverseNode(child, hint)
            if (result != null) return result
        }
        return null
    }

}
