package ru.vsu.cs.simplecinemaapp.view
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import ru.vsu.cs.simplecinemaapp.R

class ALoadingDialog(context: Context) : Dialog(context) {

    init {
        val params = window?.attributes
        params?.gravity = Gravity.CENTER
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        val view: View = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
        setContentView(view)
    }
}
