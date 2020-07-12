package com.dev_app.texttopdf

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val STORAGE_CODE: Int = 100
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        deleteBtn.findViewById<Button>(R.id.deleteBtn)

       saveBtn.setOnClickListener(){
           //we need to handle runtime permission for devices with marshmallow and above
           if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
           == PackageManager.PERMISSION_DENIED){
               //permission was not granted, request it
               val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
               requestPermissions(permissions, STORAGE_CODE)
           } else {
                savePdfFile() //... permission granted call this method..
           }
       }
        //...Delete all text
        deleteBtn.setOnClickListener(){
            deleteAll()
        }
    }

    private fun mFolder(){

    }

    //...save the PDF...
    private fun savePdfFile(){
        val pdfDoc = Document()
        val pdfFilename = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val pdfFiepath = Environment.getExternalStorageDirectory().toString() + "/" + pdfFilename+".pdf"
        try {
            PdfWriter.getInstance(pdfDoc, FileOutputStream(pdfFiepath))
            pdfDoc.open() //open the document for writing...

            val textPdf = textEt.text.toString()
            pdfDoc.addAuthor("TEXT TO PDF")
            pdfDoc.add(Paragraph(textPdf))

            pdfDoc.close()
            Toast.makeText(this, "$pdfFilename.pdf\nis saved to\n$pdfFiepath", Toast.LENGTH_LONG).show()
        } catch (e: Exception){
            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted, call savePdf() method
                    savePdfFile()
                } else {
                    //permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //..
   private fun deleteAll(){
        alertDialog()
    }

       private  fun alertDialog(){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("Delete all data at once")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            builder.setPositiveButton("Yes"){
                dialogInterface, i ->
                val msg: String = textEt.text.toString()

                if (msg.trim().isEmpty()){
                    Toast.makeText(applicationContext,"Field is Empty..",Toast.LENGTH_SHORT).show()
                }
                else{
                    textEt.text.clear()
                    Toast.makeText(applicationContext,"Successful..",Toast.LENGTH_SHORT).show()
                }
            }
           //..Negative dialog..
           builder.setNegativeButton("No"){
               dialogInterface: DialogInterface?, i: Int ->
           }
           val alertDialog: AlertDialog = builder.create()
           alertDialog.show()
            }
        }