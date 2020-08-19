package com.example.gxscanner

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient



class DocumentGenerator{
    private val mContext:Context
    private val mPrinterWorker:PrinterWorker
    private var mWebView: WebView? = null
    constructor(context:Context){
        this.mContext=context
        this.mPrinterWorker= PrinterWorker(context)
    }

    public fun doWebViewPrint(html:String) {
        // Create a WebView object specifically for printing
        val webView = WebView(mContext)
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false

            override fun onPageFinished(view: WebView, url: String) {
                Log.i(TAG, "page finished loading $url")
                if (mWebView!=null) {
                    mPrinterWorker.createWebPrintJob(mWebView!!)
                }
                mWebView = null
            }
        }
        webView.loadDataWithBaseURL(null, html, "text/HTML", "UTF-8", null)
        mWebView = webView
    }


}