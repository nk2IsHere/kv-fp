package ga.nk2ishere.dev.fluffypatrol.ui.misc

import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.Nullable
import com.github.paolorotolo.appintro.AppIntro
import ga.nk2ishere.dev.fluffypatrol.R


class TutorialActivity : AppIntro() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        addSlide(AppIntroFragment.newInstance(SliderPage(
                title = "FluffyPatrol",
                description = "Ваш помощник в отслеживании здоровья питомца",
                bgColor = resources.getColor(R.color.colorPrimary),
                imageDrawable = R.drawable.fp_tutorial_icon
        )))

        addSlide(AppIntroFragment.newInstance(SliderPage(
                title = "Удобный список питомцев",
                description = "Качественный контроль за каждым!",
                bgColor = resources.getColor(R.color.colorPrimary),
                imageDrawable = R.drawable.fp_tutorial_pet_list
        )))

        addSlide(AppIntroFragment.newInstance(SliderPage(
                title = "Простой и понятный интерфейс",
                description = "Более 25 поддерживаемых пород!",
                bgColor = resources.getColor(R.color.colorPrimary),
                imageDrawable = R.drawable.fp_tutorial_pet_health
        )))


        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(resources.getColor(R.color.colorPrimary))
        setSeparatorColor(resources.getColor(R.color.colorPrimary))

        showSkipButton(true)
        isProgressButtonEnabled = true
    }

    override fun onDonePressed() {
        finish()
    }
}