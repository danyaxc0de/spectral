package com.spectral.overlay

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager

class OverlayService : Service() {
    private lateinit var wm: WindowManager
    private lateinit var arrowView: ArrowView
    private lateinit var evalView: EvalBarView
    private lateinit var menuView: MenuView

    var arrow: Pair<String, String>? = null
    var showArrow = true
    var showEval = true

    override fun onCreate() {
        super.onCreate()
        instance = this
        wm = getSystemService(WINDOW_SERVICE) as WindowManager

        arrowView = ArrowView(this)
        wm.addView(arrowView, WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ))

        evalView = EvalBarView(this)
        wm.addView(evalView, WindowManager.LayoutParams(
            60, 800, 40, 400,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        ))

        menuView = MenuView(this)
        menuParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
            800, 200,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        menuParams.gravity = Gravity.TOP or Gravity.START
        wm.addView(menuView, menuParams)
    }

    fun updateMenuPosition() { wm.updateViewLayout(menuView, menuParams) }

    override fun onStartCommand(i: Intent?, f: Int, s: Int): Int = START_STICKY
    override fun onBind(i: Intent?): IBinder? = null

    companion object {
        @Volatile var instance: OverlayService? = null
        lateinit var menuParams: WindowManager.LayoutParams

        fun requestHint(fen: String) {
            val svc = instance ?: return
            Thread {
                val res = EngineBridge.analyze(fen)
                svc.arrow = res.move.substring(0, 2) to res.move.substring(2, 4)
                
                svc.arrowView.post {
                    svc.arrowView.visibility = if (svc.showArrow) View.VISIBLE else View.GONE
                    svc.arrowView.invalidate()
                }
                svc.evalView.post {
                    svc.evalView.visibility = if (svc.showEval) View.VISIBLE else View.GONE
                    svc.evalView.setScore(res.score)
                }
            }.start()
        }
    }
}
