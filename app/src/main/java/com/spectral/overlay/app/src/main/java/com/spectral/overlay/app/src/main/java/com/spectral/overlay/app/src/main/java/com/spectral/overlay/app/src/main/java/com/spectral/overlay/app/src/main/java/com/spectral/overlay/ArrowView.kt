package com.spectral.overlay

import android.content.Context
import android.graphics.*
import android.view.View

class ArrowView(ctx: Context) : View(ctx) {
    private val boardLeft = 40f
    private val boardTop = 700f
    private val boardSize = 1080f
    private val cell = boardSize / 8f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 18f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    init { setLayerType(LAYER_TYPE_SOFTWARE, null) }

    override fun onDraw(canvas: Canvas) {
        val a = OverlayService.instance?.arrow ?: return
        val p1 = center(a.first)
        val p2 = center(a.second)

        paint.setShadowLayer(25f, 0f, 0f, Color.parseColor("#FF5252"))
        paint.shader = LinearGradient(p1.x, p1.y, p2.x, p2.y,
            intArrayOf(Color.TRANSPARENT, Color.parseColor("#D32F2F"), Color.WHITE),
            floatArrayOf(0f, 0.7f, 1f), Shader.TileMode.CLAMP)

        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint)

        val ang = Math.atan2((p2.y - p1.y).toDouble(), (p2.x - p1.x).toDouble())
        val len = 70f
        val head = Path().apply {
            moveTo(p2.x, p2.y)
            lineTo(p2.x - len * Math.cos(ang + Math.PI / 5).toFloat(), p2.y - len * Math.sin(ang + Math.PI / 5).toFloat())
            moveTo(p2.x, p2.y)
            lineTo(p2.x - len * Math.cos(ang - Math.PI / 5).toFloat(), p2.y - len * Math.sin(ang - Math.PI / 5).toFloat())
        }
        paint.shader = null
        paint.color = Color.WHITE
        canvas.drawPath(head, paint)
    }

    private fun center(sq: String): PointF {
        val f = sq[0] - 'a'
        val r = sq[1] - '1'
        return PointF(boardLeft + (f + 0.5f) * cell, boardTop + (7 - r + 0.5f) * cell)
    }
}
