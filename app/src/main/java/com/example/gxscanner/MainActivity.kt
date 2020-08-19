package com.example.gxscanner

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*


const val ZXING="com.google.zxing.client.android"
const val ZXINGINTENT="com.google.zxing.client.android.SCAN"
const val TAG="GX"
class MainActivity : AppCompatActivity() {
    private val mDocumentGenerator:DocumentGenerator= DocumentGenerator(this)
    private val results = arrayListOf<String>()
    private var adapter:ArrayAdapter<String>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab_scan.setOnClickListener { view ->
            /*val intent = Intent("miui.intent.action.scanbarcode")
            intent.putExtra("isBackToThirdApp",true)
            startActivityForResult(intent, 1);*/

            val intent = Intent(ZXINGINTENT)
            intent.putExtra("SCAN_MODE", "ONE_D_MODE")
            intent.putExtra("SCAN_FORMATS","CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE"
            )
            startActivityForResult(intent, 1)

        }
        fab_print.setOnClickListener { view ->
           mDocumentGenerator.doWebViewPrint(DocumentTemplate.getHTML(results, getString(R.string.template_1)))
        }
        // используем адаптер данных
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, results
        )
        listResults.setAdapter(adapter)
    }

    override fun onResume() {
        super.onResume()
        checkZxingInstalled()
    }

    fun addResult(result:String){
        var builder=AlertDialog.Builder(this)
        builder.setTitle("Сохранить код?")
        builder.setPositiveButton("Сохранить",  DialogInterface.OnClickListener  { _, i ->
            val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
            val currentDate = sdf.format(Date())
            results.add(StringBuilder().append(result).append(" ").append(currentDate).toString())
            adapter?.notifyDataSetChanged()
        })
        builder.setMessage(result)
        builder.setNegativeButton("Отмена", DialogInterface.OnClickListener  { _, i ->  })
        builder.setCancelable(false)
        builder.show()
    }

    fun checkZxingInstalled(){
        var packageManager=this.packageManager
        try {
            packageManager.getPackageInfo(ZXING, 0)
            unhideButtons()
        } catch (e: PackageManager.NameNotFoundException) {
            var builder=AlertDialog.Builder(this)
            builder.setMessage("Необходимо установить приложение Сканер-штрихкодов")
            builder.setPositiveButton("Установить", DialogInterface.OnClickListener { _, i ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+ZXING)))
            })
            builder.setCancelable(false)
            builder.show()
            hideButtons()
        }
    }

    fun hideButtons(){
        (fab_scan as View).visibility=View.GONE
        (fab_print as View).visibility=View.GONE
    }
    fun unhideButtons(){
        (fab_scan as View).visibility=View.VISIBLE
        (fab_print as View).visibility=View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK){
            Log.d("intent URI", data?.toUri(0));
            if (data!=null) {
                var result:String?=null
                if (data.hasExtra("SCAN_RESULT")) result=data.extras.getString("SCAN_RESULT")
                if (data.hasExtra("result")) result=data.extras.getString("result")
                if(result!=null){
                    addResult(result)
                }


            }
        }
        /* Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()*/
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
