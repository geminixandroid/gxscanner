package com.example.gxscanner

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView

class PrinterWorker{
    private val mContext:Context
    constructor(context:Context){
        this.mContext=context
    }
    fun createWebPrintJob(webView: WebView) {
        // Get a PrintManager instance
        (this.mContext.getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

            val jobName = "${this.mContext.getString(R.string.app_name)} Document"

            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)
            // Create a print job with name and adapter instance
            printManager.print(
                jobName,
                printAdapter,
                PrintAttributes.Builder().build())
        }
    }
}