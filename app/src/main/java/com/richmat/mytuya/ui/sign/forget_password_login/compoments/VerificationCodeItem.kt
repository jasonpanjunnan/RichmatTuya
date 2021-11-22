package com.richmat.mytuya.ui.sign.forget_password_login.compoments

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerificationCodeItem(
    text: String,
    modifier: Modifier = Modifier,
    focused: Boolean,
) {
    Surface(
        modifier = modifier.size(30.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
        color = Color.LightGray
    ) {

    }
}

/**
 * @param text 文本内容
 * @param focused 是否高亮当前输入框
 */
@Composable
fun SimpleVerificationCodeItem(text: String, focused: Boolean) {
    val borderColor = if (focused) Color.Gray else Color(0xeeeeeeee)

    Box(
        modifier = Modifier
            .border(1.dp, borderColor)
            .size(45.dp, 45.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }

}