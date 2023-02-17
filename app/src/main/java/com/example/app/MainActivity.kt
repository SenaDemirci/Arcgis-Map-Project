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

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.data.Geodatabase
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.loadable.LoadStatus
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.MapView
import com.example.app.Commands.Database.DBHelper
import com.example.app.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class MainActivity : AppCompatActivity() {
    //private lateinit var imageView: ImageView


    private lateinit var toolManager: ToolManager
    private val geodatabasePath = "/storage/emulated/0/DATA/Demo.geodatabase"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        activityMainBinding.layout.addView(linearLayout)

        toolManager  = ToolManager(this@MainActivity, mapView, linearLayout)

        toolManager.Initialize()

        setApiKeyForApp()

        setupMap()

        val btnCamera = findViewById<Button>(R.id.btnTakePicture)
        val imageView = findViewById<ImageView>(R.id.imageView)

         val takePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val takenImage = result.data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(takenImage)

            }
        }


        btnCamera?.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            Log.e("butonnnn", takePictureIntent.data.toString())
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                Log.e("evetttt", takePictureIntent.resolveActivity(this.packageManager).packageName)
                takePicture.launch(takePictureIntent)
            } else {
                Log.e("hayirrrr", "hayirrrrr")
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }


 /*
        val btnCamera = findViewById<Button>(R.id.btnTakePicture)
        btnCamera?.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                Log.e("olduu", takePictureIntent.data.toString())
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Log.e("yok", takePictureIntent.data.toString())
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
*/
    }

/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    Log.e("sonnnnnnn", requestCode.toString())
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.e("basarili", REQUEST_CODE.toString())
            val takenImage = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(takenImage)
        } else {
            Log.e("basarisiz", REQUEST_CODE.toString())
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
 */

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
        mapView.setViewpoint(Viewpoint(39.142, 34.502, 72000.0))
    }

    private fun setApiKeyForApp(){
        // set your API key
        // Note: it is not best practice to store API keys in source code. The API key is referenced
        // here for the convenience of this tutorial.

        ArcGISRuntimeEnvironment.setApiKey("AAPK81534aabc5ae4f7d9f09428664a6755a-Nx3w3r_hxOkhKWmdW8S9dyyDIKRRMWV18c0eiOn2q-XLkGDWdwfY_M694JkOmlk")

        //val serviceFeatureTable = ServiceFeatureTable("/storage/emulated/0/DATA/Demo.geodatabase")

        // instantiate geodatabase with the path to the .geodatabase file
        val geodatabase = Geodatabase(geodatabasePath)

        geodatabase.loadAsync()
        geodatabase.addDoneLoadingListener {
            if (geodatabase.loadStatus == LoadStatus.LOADED) {
                for (featureTable in geodatabase.geodatabaseFeatureTables) {
                    //featureTable.queryFeaturesAsync()
                    val featureLayer = FeatureLayer(featureTable)
                    mapView.map.operationalLayers.add(featureLayer)
                }
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



