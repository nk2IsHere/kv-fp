package ga.nk2ishere.dev.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface MviView<in VA> : MvpView {
    @StateStrategyType(value = AddToEndStrategy::class)
    fun applyAction(action: VA)

    @StateStrategyType(value = SkipStrategy::class)
    fun applyActionWithSkip(action: VA)
}