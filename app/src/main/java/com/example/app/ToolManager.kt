package com.example.app

import android.app.Activity
import android.content.Context
import android.widget.LinearLayout
import android.widget.Button
import com.esri.arcgisruntime.mapping.view.MapView
import com.example.app.Commands.ICommand
import com.example.app.Commands.Tools.*

class ToolManager(private val context: Context, private val mapView: MapView, private val linearLayout: LinearLayout){
    //private var commandList = list
    var activeTool : ITool? = null

    private var pointDrawer: PointDrawer
    private var lineDrawer: LineDrawer
    private var polygonDrawer: PolygonDrawer
    private var addItemBSL: AddItemBSL
    //private var checkArea: CheckArea
    //private var addItemSQL: AddItemSQL

    init {
        pointDrawer = PointDrawer(context, mapView)
        lineDrawer = LineDrawer(context, mapView)
        polygonDrawer = PolygonDrawer(context, mapView)
        addItemBSL = AddItemBSL(context, mapView)
        //checkArea = CheckArea(context, mapView)
        //addItemSQL = AddItemSQL(context, mapView)
    }

    val commandList = mutableListOf(pointDrawer,lineDrawer,polygonDrawer,addItemBSL)

    fun Initialize(){
        activeTool = null
        for ((id, command) in commandList.withIndex()) {
            val button = Button(context)
            button.id = id
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            button.text = command.id
            button.setOnClickListener {
                onClickListener(command)
            }
            linearLayout.addView(button)
        }
    }

    private fun onClickListener(command:ICommand){
        if(command is ITool){
            activeTool?.Deactivate()
            if(command == activeTool)
            {
                activeTool = null
            }
            else{
                activeTool = command
                command.Activate()
            }
        }
        else{
            command.run()
        }
    }
}