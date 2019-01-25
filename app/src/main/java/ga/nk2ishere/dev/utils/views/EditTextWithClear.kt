package ga.nk2ishere.dev.utils.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import ga.nk2ishere.dev.fluffypatrol.R

/**
 * Created by pedromassango on 1/7/18.
 */
class EditTextWithClear : AppCompatEditText {

    // Clear buttom image
    private lateinit var mClearButtonImage: Drawable

    constructor(context: Context) : super(context) {
        setupButton()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupButton()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setupButton()
    }

    private fun setupButton() {
        mClearButtonImage = ResourcesCompat.getDrawable(resources,
                R.drawable.ic_clear_opaque, null)!!

        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, mClearButtonImage, null)

        addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s?.isEmpty()!!){
                    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
                }else{
                    mClearButtonImage = ResourcesCompat.getDrawable(resources,
                            R.drawable.ic_clear_black, null)!!
                    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, mClearButtonImage, null)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        // touch in X button to clear the current text
        setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                    this.setText("")
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

}