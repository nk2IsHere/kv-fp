package ga.nk2ishere.dev.fluffypatrol.ui.misc

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.otaliastudios.cameraview.*
import ga.nk2ishere.dev.fluffypatrol.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import android.R.attr.bitmap
import android.net.Uri
import timber.log.Timber
import java.io.BufferedOutputStream


class CameraActivity: AppCompatActivity() {
    companion object {
        val CAMERA_FILE_PATH = "cameraPath"
    }

    lateinit var camera: CameraView
    lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera)
        camera = findViewById(R.id.camera)
        camera.setLifecycleOwner(this)
        camera.mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        camera.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER)
        camera.mapGesture(Gesture.LONG_TAP, GestureAction.CAPTURE)

        path = intent?.extras?.getString(CAMERA_FILE_PATH)!!

        camera.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                CameraUtils.decodeBitmap(jpeg) {
                    val file = File(path)
                    file.createNewFile()

                    val os = BufferedOutputStream(FileOutputStream(file))
                    it.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    os.close()

                    setResult(Activity.RESULT_OK, Intent())
                    finish()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        camera.start()
    }

    override fun onPause() {
        camera.stop()
        super.onPause()
    }

    override fun onDestroy() {
        camera.destroy()
        super.onDestroy()
    }
}