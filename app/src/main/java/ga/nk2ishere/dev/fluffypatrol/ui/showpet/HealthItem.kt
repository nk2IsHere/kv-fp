package ga.nk2ishere.dev.fluffypatrol.ui.showpet

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Environment
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.nk2ishere.dev.fluffypatrol.R
import ga.nk2ishere.dev.fluffypatrol.common.data.Dog
import ga.nk2ishere.dev.fluffypatrol.common.data.Health
import ga.nk2ishere.dev.utils.round
import kotlinx.android.synthetic.main.health_item.view.*
import org.joda.time.DateTime
import org.koin.core.Koin
import java.io.File
import java.util.*

data class HealthItem(private val context: Context, val health: Health) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.weight.text = health.weight.round(2).toString()
        viewHolder.itemView.growth.text = health.growth.round(2).toString()
        viewHolder.itemView.coefficient.text = health.coefficient.times(100).round(2).toString()
        viewHolder.itemView.date.text = DateTime(health.date).toString("dd.MM.YY")
    }

    override fun getLayout() = R.layout.health_item
}