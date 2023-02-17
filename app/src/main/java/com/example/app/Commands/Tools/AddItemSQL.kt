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
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivityForResult

class AddItemSQL(private var context: Context, private var mapView: MapView, private var activity: Activity): ITool {
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
            db.addName(codeInfo, nameInfo)
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
            //takePicture()
            /*
            val permissionGranted = requestCameraPermission()
            if (permissionGranted) {
                // Open the camera interface
                openCameraInterface()
            }
            */
        }

        dialog.show()
    }

    /*
    val CAMERA_PERMISSION_CODE = 1000
    val IMAGE_CAPTURE_CODE = 1001
    var imageUri: Uri? = null
    var imageView: ImageView? = null

    private fun requestCameraPermission(): Boolean {
        var permissionGranted = false

        // If system os is Marshmallow or Above, we need to request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val cameraPermissionNotGranted = checkSelfPermission(activity as Context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            if (cameraPermissionNotGranted){
                val permission = arrayOf(Manifest.permission.CAMERA)

                // Display permission dialog
                requestPermissions(activity, permission, CAMERA_PERMISSION_CODE)
                //requestPermissions(permission, CAMERA_PERMISSION_CODE)
            }
            else{
                // Permission already granted
                permissionGranted = true
            }
        }
        else{
            // Android version earlier than M -> no need to request permission
            permissionGranted = true
        }

        return permissionGranted
    }

    // Handle Allow or Deny response from the permission dialog
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode === CAMERA_PERMISSION_CODE) {
            if (grantResults.size === 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Permission was granted
                openCameraInterface()
            }
            else{
                // Permission was denied
                Toast.makeText(context, "Camera permission was denied.", Toast.LENGTH_SHORT).show()
                //showAlert("Camera permission was denied. Unable to take a picture.");
            }
        }
    }

    private fun openCameraInterface() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, R.string.take_picture)
        values.put(MediaStore.Images.Media.DESCRIPTION, R.string.take_picture_description)
        imageUri = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        // Create camera intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        // Launch intent
        startActivityForResult(activity, intent, IMAGE_CAPTURE_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Callback from camera intent
        if (resultCode == Activity.RESULT_OK){
            // Set image captured to image view
            imageView?.setImageURI(imageUri)
        }
        else {
            // Failed to take picture
            Toast.makeText(context, "Failed to take picture.", Toast.LENGTH_SHORT).show()
            //showAlert("Failed to take camera picture")
        }
    }

*/

    /*
    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(activity as Context)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.ok_button_title, null)
        val dialog = builder.create()
        dialog.show()
    }
    */




    private fun takePicture() {

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