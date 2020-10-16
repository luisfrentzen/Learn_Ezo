package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.util.AttributeSet
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatRadioButton


class CustomRadio : AppCompatRadioButton {
    var onChangeListener : OnCheckedChangeListener? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setOwnOnCheckedChangeListener()
        buttonDrawable = null //lets remove the default drawable to create our own
    }


    fun setOwnOnCheckedChangeListener(onCheckedChangeListener: OnCheckedChangeListener?) {
        this.onChangeListener = onCheckedChangeListener
    }

    private fun setOwnOnCheckedChangeListener() {
        setOnCheckedChangeListener(object : OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                onChangeListener?.onCheckedChanged(buttonView, isChecked)
            }
        })
    }
}