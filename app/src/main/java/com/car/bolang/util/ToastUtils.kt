package com.car.bolang.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.car.bolang.R


class ToastUtils {
    companion object {
        fun toastShort(context: Context, text: String) {

            val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            val toastView = LayoutInflater.from(context).inflate(R.layout.toast_view, null)
            val toastTv = toastView.findViewById<TextView>(R.id.toast_tv)
            toastTv.setText(text)
            toast.setView(toastView)
            toast.show()
        }


        fun toastLong(context: Context, text: String) {

            val toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            val toastView = LayoutInflater.from(context).inflate(R.layout.toast_view, null)
            val toastTv = toastView.findViewById<TextView>(R.id.toast_tv)
            toastTv.setText(text)
            toast.setView(toastView)
            toast.show()
        }
    }

}

