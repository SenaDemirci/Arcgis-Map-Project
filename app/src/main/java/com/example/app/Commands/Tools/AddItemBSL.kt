package com.example.app.Commands.Tools

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.example.app.Commands.Database.AppDatabase
import com.example.app.Commands.Database.Entities.Items
import com.example.app.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddItemBSL(private var context: Context, private var mapView: MapView) :ITool {
    private val graphicsOverlay = GraphicsOverlay()
    override val id = "Item Button"
    private lateinit var appDB : AppDatabase

    init {
        mapView.graphicsOverlays.add(graphicsOverlay)
    }

    override val onTouchListener = object : DefaultMapViewOnTouchListener(context, mapView) {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (e != null) {
                if (e.action == MotionEvent.ACTION_UP) {
                    showDialog()
                }
            }
            return true
        }
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

    private fun showDialog() {

        appDB = AppDatabase.getDatabase(context)

        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottomsheetlayout)

        val btnSave = dialog.findViewById<Button>(R.id.save)
        val btnPrint = dialog.findViewById<Button>(R.id.print)
        val btnCamera = dialog.findViewById<Button>(R.id.camera)
        val btnDelete = dialog.findViewById<Button>(R.id.delete_btn)
        val btnUpdate = dialog.findViewById<Button>(R.id.update_btn)
        val editCode = dialog.findViewById<EditText>(R.id.code)
        val editName = dialog.findViewById<EditText>(R.id.name)

        btnUpdate?.setOnClickListener {
            val codeInfo = editCode?.text.toString()
            val nameInfo = editName?.text.toString()

            if (codeInfo.isNotEmpty() && nameInfo.isNotEmpty()) {

                GlobalScope.launch(Dispatchers.IO) {
                    appDB.itemsDao().updateItems(codeInfo, nameInfo, id.toInt())
                }

                editCode?.text?.clear()
                editName?.text?.clear()

                Toast.makeText(context, codeInfo + nameInfo + " Successfully update", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, codeInfo + nameInfo + " Cannot update", Toast.LENGTH_SHORT).show()
                val items = Items(
                    null, "no", "no", null
                )

                GlobalScope.launch(Dispatchers.IO) {
                    appDB.itemsDao().insertItems(items)
                }

                editCode?.text?.clear()
                editName?.text?.clear()
            }
        }

        btnDelete?.setOnClickListener {
            GlobalScope.launch {
                appDB.itemsDao().deleteAll()
            }
        }

        btnSave?.setOnClickListener {
            val codeInfo = editCode?.text.toString()
            val nameInfo = editName?.text.toString()
            //Toast.makeText(context, codeInfo, Toast.LENGTH_SHORT).show()
            //Toast.makeText(context, nameInfo, Toast.LENGTH_SHORT).show()

            if (codeInfo.isNotEmpty() && nameInfo.isNotEmpty()) {
                val items = Items(
                    null, codeInfo, nameInfo, null
                )

                GlobalScope.launch(Dispatchers.IO) {
                    appDB.itemsDao().insertItems(items)
                }

                editCode?.text?.clear()
                editName?.text?.clear()

                Toast.makeText(context, codeInfo + nameInfo + " Successfully written", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, codeInfo + nameInfo + " Cannot written", Toast.LENGTH_SHORT).show()
                val items = Items(
                    null, "no", "no", null
                )

                GlobalScope.launch(Dispatchers.IO) {
                    appDB.itemsDao().insertItems(items)
                }

                editCode?.text?.clear()
                editName?.text?.clear()
            }
        }

        suspend fun displayData(items: Items) {
            withContext(Dispatchers.Main) {
                var id_field = dialog.findViewById<EditText>(R.id.idText)
                var code_field = dialog.findViewById<EditText>(R.id.codeText)
                var name_field = dialog.findViewById<EditText>(R.id.nameText)
/*
                val idInfo = id_field?.text.toString()
                val codeInfo = code_field?.text.toString()
                val nameInfo = name_field?.text.toString()

 */
                Toast.makeText(context, id_field.toString() , Toast.LENGTH_SHORT).show()
                Toast.makeText(context, items.id.toString() , Toast.LENGTH_SHORT).show()
                Toast.makeText(context, items.itemCode, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, items.itemName , Toast.LENGTH_SHORT).show()

                id_field?.append(items.id.toString())
                code_field?.append(items.itemCode)
                name_field?.append(items.itemName)

                /*
                idInfo = items.id.toString()
                codeInfo = items.itemCode.toString()
                nameInfo = items.itemName.toString()

                 */
            }
        }

        btnPrint?.setOnClickListener {

            var ask_field= dialog.findViewById<EditText>(R.id.ask_button)
            val ask_button = ask_field?.text.toString()
            if (ask_button.isNotEmpty()) {
                lateinit var items: Items

                GlobalScope.launch {
                    items = appDB.itemsDao().findByCode(ask_button)
                    displayData(items)
                }
            }
        }



        btnCamera?.setOnClickListener {
            Toast.makeText(context, "cameraaaaa", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }
}