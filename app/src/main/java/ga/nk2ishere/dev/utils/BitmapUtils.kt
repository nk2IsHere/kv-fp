package ga.nk2ishere.dev.utils

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.File


fun File.getPreview(): Bitmap {
    val options = BitmapFactory.Options()
    options.inTempStorage = ByteArray(24 * 1024)
    options.inJustDecodeBounds = false
    options.inSampleSize = 32
    val bitmap = BitmapFactory.decodeFile(this.absolutePath, options)
    val previewBitmap = ThumbnailUtils.extractThumbnail(bitmap, 96, 96)
    bitmap?.recycle()
    return previewBitmap
}