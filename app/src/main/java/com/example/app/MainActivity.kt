/*
 * Copyright 2020 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.app


import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.data.FeatureTable
import com.esri.arcgisruntime.data.Geodatabase
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.loadable.LoadStatus
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.MapView
import com.example.app.Commands.Tools.LineDrawer
import com.example.app.Commands.Tools.PointDrawer
import com.example.app.Commands.Tools.PolygonDrawer
import com.example.app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var pointDrawer: PointDrawer
    private lateinit var lineDrawer: LineDrawer
    private lateinit var polygonDrawer: PolygonDrawer

    private lateinit var toolManager: ToolManager
    private val geodatabasePath = "/sdcard/DATA/data1.geodatabase"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        val linearLayout = LinearLayout(this)

        pointDrawer = PointDrawer(this@MainActivity, mapView)
        lineDrawer = LineDrawer(this@MainActivity, mapView)
        polygonDrawer = PolygonDrawer(this@MainActivity, mapView)

        activityMainBinding.layout.addView(linearLayout)
        toolManager  = ToolManager(this@MainActivity, listOf(pointDrawer,lineDrawer,polygonDrawer),linearLayout)

        toolManager.Initialize()

        setApiKeyForApp()

        setupMap()
    }


    private val activityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val mapView: MapView by lazy {
        activityMainBinding.mapView
    }

    // set up your map here. You will call this method from onCreate()
    private fun setupMap() {

        // create a map with the BasemapStyle streets
        val map = ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC)

        // set the map to be displayed in the layout's MapView
        mapView.map = map
        // set the viewpoint, Viewpoint(latitude, longitude, scale)
        mapView.setViewpoint(Viewpoint(34.0270, -118.8050, 72000.0))
    }

    private fun setApiKeyForApp(){
        // set your API key
        // Note: it is not best practice to store API keys in source code. The API key is referenced
        // here for the convenience of this tutorial.

        ArcGISRuntimeEnvironment.setApiKey("AAPK81534aabc5ae4f7d9f09428664a6755a-Nx3w3r_hxOkhKWmdW8S9dyyDIKRRMWV18c0eiOn2q-XLkGDWdwfY_M694JkOmlk")


// instantiate geodatabase with the path to the .geodatabase file
        val geodatabase = Geodatabase(geodatabasePath)

        geodatabase.loadAsync()
        geodatabase.addDoneLoadingListener {
            Log.i("HEREEE",geodatabase.loadError.toString())
            if (geodatabase.loadStatus == LoadStatus.LOADED) {
                val featureTable: FeatureTable =
                    geodatabase.getGeodatabaseFeatureTable("Trailheads")
                val featureLayer = FeatureLayer(featureTable)
                mapView.map.operationalLayers.add(featureLayer)
            }
        }
    }

    override fun onPause() {
        mapView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onDestroy() {
        mapView.dispose()
        super.onDestroy()
    }

}



