package com.spectral.overlay

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.*

class MenuView(ctx: Context) : LinearLayout(ctx) {
    init {
        orientation = VERTICAL
        setBackgroundResource(R.drawable.menu_bg)
        setPadding(40, 30, 40, 30)

        val title = TextView(ctx).apply {
            text = "SPECTRAL v5"
            setTextColor(Color.parseColor("#D32F2F"))
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }
        addView(title)

        val swArrow = Switch(ctx).apply {
            text = " Hint Arrow"
            setTextColor(Color.parseColor("#F5F5F5"))
            isChecked = true
            setOnCheckedChangeListener { _, c -> OverlayService.instance?.showArrow = c }
        }
        addView(swArrow)

        val swEval = Switch(ctx).apply {
            text = " Eval Bar"
            setTextColor(Color.parseColor("#F5F5F5"))
            isChecked = true
            setOnCheckedChangeListener { _, c -> OverlayService.instance?.showEval = c }
        }
        addView(swEval)

        val promo = TextView(ctx).apply {
            text = "TV: @BubMagnat"
            setTextColor(Color.parseColor("#FF5252"))
            textSize = 14f
            setTypeface(null, Typeface.ITALIC)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 0)
        }
        addView(promo)

        title.setOnTouchListener(object : OnTouchListener {
            var initX = 0; var initY = 0; var tX = 0f; var tY = 0f
            override fun onTouch(v: View, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initX = OverlayService.menuParams.x; initY = OverlayService.menuParams.y
                        tX = e.rawX; tY = e.rawY; return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        OverlayService.menuParams.x = initX + (e.rawX - tX).toInt()
                        OverlayService.menuParams.y = initY + (e.rawY - tY).toInt()
                        OverlayService.instance?.updateMenuPosition(); return true
                    }
                }
                return false
            }
        })
    }
}
