package com.example.app.Commands.Tools

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.graphics.Point as androidPoint
import android.view.MotionEvent
import android.widget.*
import androidx.core.content.ContextCompat.checkSelfPermission
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.example.app.Commands.Database.DBHelper
import com.example.app.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivityForResult

class AddItemSQL(private var context: Context, private var mapView: MapView): ITool {
    private val graphicsOverlay = GraphicsOverlay()
    override val id = "Add Item"

    init {
        mapView.graphicsOverlays.add(graphicsOverlay)
    }

    override val onTouchListener = object : DefaultMapViewOnTouchListener(context, mapView) {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            if (e != null) {
                val x = e.x.toInt()
                val y = e.y.toInt()
                val newPoint = mapView.screenToLocation(androidPoint(x,y))
                showDialog(newPoint)
            }
            return true
        }
    }

    @SuppressLint("Range")
    private fun showDialog(newPoint: Point) {

        val db = DBHelper(context, null)

        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottomsheetlayout)
        val btnEdit= dialog.findViewById<RelativeLayout>(R.id.rl_edit)
        val btnDelete= dialog.findViewById<RelativeLayout>(R.id.rl_delete)
        val btnAdd= dialog.findViewById<RelativeLayout>(R.id.rl_add)

        val btnSave = dialog.findViewById<Button>(R.id.save)
        val btnPrint = dialog.findViewById<Button>(R.id.print)
        val btnCamera = dialog.findViewById<Button>(R.id.camera)
        val editCode = dialog.findViewById<EditText>(R.id.code)
        val editName = dialog.findViewById<EditText>(R.id.name)

        btnEdit?.setOnClickListener {
            Toast.makeText(context, "Clicked on Edit", Toast.LENGTH_SHORT).show()
        }
        btnDelete?.setOnClickListener {
            Toast.makeText(context, "Clicked on Delete", Toast.LENGTH_SHORT).show()
        }
        btnAdd?.setOnClickListener {
            Toast.makeText(context, "Clicked on Add", Toast.LENGTH_SHORT).show()
        }

        btnSave?.setOnClickListener {
            val codeInfo = editCode?.text.toString()
            val nameInfo = editName?.text.toString()
            //db.addName(codeInfo, nameInfo)
            Toast.makeText(context, codeInfo, Toast.LENGTH_SHORT).show()
            Toast.makeText(context, nameInfo, Toast.LENGTH_SHORT).show()
            editCode?.text?.clear()
            editName?.text?.clear()
        }

        btnPrint?.setOnClickListener {
            val db = DBHelper(context, null)
            val cursor = db.getName()

            // moving the cursor to first position and appending value in the text view
            cursor!!.moveToFirst()
            editCode?.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
            editName?.append(cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")

            // moving our cursor to next
            // position and appending values
            while(cursor.moveToNext()){
                editCode?.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
                editName?.append(cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")
            }
            cursor.close()
        }

        btnCamera?.setOnClickListener {
            Toast.makeText(context, "cameraaaaa", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    override fun Activate() {
        mapView.onTouchListener = onTouchListener
    }

    override fun Deactivate() {
        mapView.onTouchListener = DefaultMapViewOnTouchListener(context, mapView)
    }

    override fun run() {
        TODO("Not yet implemented")
    }

}