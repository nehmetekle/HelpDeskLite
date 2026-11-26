package fr.estiam.helpdesklite.ui

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

internal fun View.applySystemPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val padding = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout() or
                WindowInsetsCompat.Type.ime()
        )
        view.setPadding(padding.left, padding.top, padding.right, padding.bottom)
        insets
    }
    ViewCompat.requestApplyInsets(this)
}
