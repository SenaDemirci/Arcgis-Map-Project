package com.example.app.Commands.Tools

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.Toast
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.example.app.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class AddItemSQL(private var context: Context, private var mapView: MapView): ITool {
    private val graphicsOverlay = GraphicsOverlay()
    override val id = "Add Item"

    init {
        mapView.graphicsOverlays.add(graphicsOverlay)
    }

    override val onTouchListener = object : DefaultMapViewOnTouchListener(context, mapView) {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            showDialog();
            return true
        }
    }

    private fun showDialog() {
        Log.e("girdiiiiiiii", "girdi")
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottomsheetlayout)
        val btnEdit= dialog.findViewById<RelativeLayout>(R.id.rl_edit)
        val btnDelete= dialog.findViewById<RelativeLayout>(R.id.rl_delete)
        val btnAdd= dialog.findViewById<RelativeLayout>(R.id.rl_add)

        btnEdit?.setOnClickListener {
            Toast.makeText(context, "Clicked on Edit", Toast.LENGTH_SHORT).show()
        }
        btnDelete?.setOnClickListener {
            Toast.makeText(context, "Clicked on Delete", Toast.LENGTH_SHORT).show()
        }
        btnAdd?.setOnClickListener {
            Toast.makeText(context, "Clicked on Add", Toast.LENGTH_SHORT).show()
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