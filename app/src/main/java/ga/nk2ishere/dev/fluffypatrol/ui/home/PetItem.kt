package ga.nk2ishere.dev.fluffypatrol.ui.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Environment
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.nk2ishere.dev.fluffypatrol.R
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.utils.getPreview
import kotlinx.android.synthetic.main.pet_item.view.*
import java.io.File

data class PetItem(private val context: Context, val dog: Dog) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.name.text = dog.name
        viewHolder.itemView.age.text = dog.age.toString()
        viewHolder.itemView.photo.setImageBitmap(File(context.filesDir, "${dog.photo}.jpg").getPreview())
    }

    override fun getLayout() = R.layout.pet_item
}