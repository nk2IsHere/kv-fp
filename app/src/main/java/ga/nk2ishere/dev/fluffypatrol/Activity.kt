package ga.nk2ishere.dev.fluffypatrol

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import ga.nk2ishere.dev.fluffypatrol.ui.home.HomeController
import kotlinx.android.synthetic.main.activity.*
import timber.log.Timber

class Activity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        router = Conductor.attachRouter(this, main_container, savedInstanceState)
        if (!router.hasRootController()) router.setRoot(RouterTransaction.with(HomeController.create()))
    }

    override fun onBackPressed() {
        if (!router.handleBack()) super.onBackPressed()
    }
}
