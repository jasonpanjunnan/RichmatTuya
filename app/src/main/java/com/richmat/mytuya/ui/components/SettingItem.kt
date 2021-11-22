package com.richmat.mytuya.ui.components

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.richmat.mytuya.R

@Composable
fun SettingItem() {
    Surface() {

    }
    Box {
        Text(text = "", fontWeight = FontWeight.Bold)
        val resource = LocalContext.current.resources
        val water = resource.getQuantityString(
            R.plurals.test0,1
        )
        AndroidView(factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        }, update = { tv ->
            tv.text = HtmlCompat.fromHtml("", HtmlCompat.FROM_HTML_MODE_COMPACT)
        })
    }
}
