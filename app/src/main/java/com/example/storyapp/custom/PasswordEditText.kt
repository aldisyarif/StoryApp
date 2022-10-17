package com.example.storyapp.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R

class PasswordEditText: AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
    private fun init() {
        hint = context.getString(R.string.insert_your_password)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (s.toString().isEmpty()) {
                    context.getString(R.string.insert_your_password)
                } else if (s.toString().trim { it <= ' ' }.length < 6) {
                    context.getString(R.string.min_password)
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable) {
                //
            }
        })
    }


}