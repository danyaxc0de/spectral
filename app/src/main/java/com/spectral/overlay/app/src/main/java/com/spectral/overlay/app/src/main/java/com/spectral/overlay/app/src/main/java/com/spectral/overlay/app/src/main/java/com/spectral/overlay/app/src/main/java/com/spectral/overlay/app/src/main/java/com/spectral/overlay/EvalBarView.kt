package com.spectral.overlay

import android.content.Context
import android.graphics.*
import android.view.View

class EvalBarView(ctx: Context) : View(ctx) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var evalScore = 0f

    fun setScore(s: Float) { evalScore = s.coerceIn(-10f, 10f); invalidate() }

    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        paint.color = Color.parseColor("#0A0A0A")
        canvas.drawRoundRect(0f, 0f, w, h, 8f, 8f, paint)

        val whiteH = h * ((evalScore + 10f) / 20f)
        paint.color = Color.parseColor("#F5F5F5")
        canvas.drawRoundRect(0f, h - whiteH, w, h, 8f, 8f, paint)

        paint.color = Color.parseColor("#D32F2F")
        paint.strokeWidth = 4f
        canvas.drawLine(0f, h - whiteH, w, h - whiteH, paint)

        paint.textSize = 30f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(if (evalScore > 0) "+%.1f".format(evalScore) else "%.1f".format(evalScore), w / 2, h / 2, paint)
    }
}
