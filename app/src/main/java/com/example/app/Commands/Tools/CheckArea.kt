package com.example.app.Commands.Tools
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import com.esri.arcgisruntime.data.FeatureTable
import com.esri.arcgisruntime.data.Geodatabase
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable
import com.esri.arcgisruntime.data.QueryParameters
import com.esri.arcgisruntime.geometry.GeometryEngine
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.layers.Layer
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView

class CheckArea (private var context: Context, private var mapView: MapView) :ITool {
    private val graphicsOverlay = GraphicsOverlay()
    private lateinit var featureLayer: FeatureLayer
    override val id = "Check Area"
    val tolerance = 5.0 // tolerance in meters
    val layerNames = mutableListOf<String>()

    init {
        mapView.graphicsOverlays.add(graphicsOverlay)
    }

    override val onTouchListener = object : DefaultMapViewOnTouchListener(context, mapView) {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.i("aaaaaaaa", mapView.map.toJson())
            if (e != null) {
                if (e.action == MotionEvent.ACTION_UP) {
                    Log.i("bbbbbbbb", mapView.id.toString())
                    val clickPoint = mapView.screenToLocation(android.graphics.Point(e.x.toInt(), e.y.toInt()))
                    doQuery(clickPoint)
                }
            }
            return true
        }
    }

    override fun Activate() {
        mapView.onTouchListener = onTouchListener
        mapView.map.operationalLayers.iterator().forEach { layer ->
            if (layer.name != null) {
                Log.e("nameeeeeee", layer.name)
                featureLayer = layer as FeatureLayer

                layerNames.add(layer.name)
                Log.e("nameeeeeee", layerNames.toString())

                for (layer in mapView.map.operationalLayers) {
                    if (!layerNames.contains(layer.name)) {
                        Log.e("yesssssss", layer.name)
                        featureLayer = layer as FeatureLayer
                    }
                }

            }
        }
    }

    override fun Deactivate() {
        mapView.onTouchListener = DefaultMapViewOnTouchListener(context, mapView)
    }

    override fun run() {

    }


    private fun doQuery(clickPoint: Point) {
        val query = QueryParameters()
        query.geometry = GeometryEngine.buffer(clickPoint, tolerance)

        query.spatialRelationship = QueryParameters.SpatialRelationship.INTERSECTS

        var result = featureLayer.selectFeaturesAsync(query, FeatureLayer.SelectionMode.NEW)

        Log.i("ressssss", result.isDone.toString())

        result.addDoneListener {
            result.get().iterator().forEach {
                var table = (it.featureTable as GeodatabaseFeatureTable).tableName
                Log.e("ddddddddd1111", "doQuery: " + table)
                if (layerNames.contains(table)) {
                    Log.e("dddddddddddddddd", "doQuery: " + table)
                }
            /*
                if (table=="Hat") {
                    Log.e("Hattttt", "doQuery: " + table)
                }
                if (table=="Trafo") {
                    Log.e("Trafooooo", "doQuery: " + table)
                }
                if (table=="IstasyonAlani") {
                    Log.e("IstasyonAlaniiiii", "doQuery: " + table)
                }
                */
            }
        }

        /*
        if(featureLayer.name == "Hat"){
            var result = featureLayer.selectFeaturesAsync(query, FeatureLayer.SelectionMode.NEW)
            result.addDoneListener {
                result.get().iterator().forEach {
                    var table = (it.featureTable as GeodatabaseFeatureTable).tableName
                    Log.e("asd", "doQuery: " + table)
                }
            }
        }else if(featureLayer.name == "IstasyonAlani"){
            result.addDoneListener {
                result.get().iterator().forEach {
                    var table = (it.featureTable as GeodatabaseFeatureTable).tableName
                    Log.e("klm", "doQuery: " + table)
                }
            }
        }else if(featureLayer.name == "Trafo"){
            result.addDoneListener {
                result.get().iterator().forEach {
                    var table = (it.featureTable as GeodatabaseFeatureTable).tableName
                    Log.e("bmw", "doQuery: " + table)
                }
            }
        }
        */

    }
}
