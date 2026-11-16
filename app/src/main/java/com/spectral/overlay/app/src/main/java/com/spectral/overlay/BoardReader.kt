package com.spectral.overlay

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class BoardReader : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val root = rootInActiveWindow ?: return
        val labels = mutableListOf<String>()
        collect(root, labels)
        if (labels.isNotEmpty()) BoardState.update(labels)
    }

    private fun collect(node: AccessibilityNodeInfo?, out: MutableList<String>) {
        if (node == null) return
        node.contentDescription?.toString()?.let { out.add(it) }
        node.text?.toString()?.let { out.add(it) }
        for (i in 0 until node.childCount) collect(node.getChild(i), out)
    }

    override fun onInterrupt() {}
}
