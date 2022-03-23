package com.richmat.mytuya.ui.sign.forget_password_login.compoments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerificationCodeField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (value: String) -> Unit,
    digits: Int = 6,
    horizontalMargin: Dp = 16.dp,
    inputCallBack: (content: String) -> Unit,
    itemScope: @Composable (focused: Boolean) -> Unit,
) {
    //不行了哥，抄吧

//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//    ) {
//        BasicTextField(value = value, onValueChange = onValueChange, textStyle = TextStyle(
//            textIndent = TextIndent(), letterSpacing = TextUnit(30f, TextUnitType.Unspecified)
//
//        ))
//    }
}

@Composable
fun VerificationCodeField(
    digits: Int,
    horizontalMargin: Dp = 10.dp,
    inputCallback: (content: String) -> Unit = {},
    itemScope: @Composable (text: String, focused: Boolean) -> Unit,
) {
    var content by remember { mutableStateOf("") }
    Box {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //绘制框
            repeat(digits) {
                if (it != 0) {
                    //添加间距
                    Spacer(modifier = Modifier.width(horizontalMargin))
                }
                //获取当前框的文本
                val text = content.getOrNull(it)?.toString() ?: ""
                //是否正在输入的框
                val focused = it == content.length
                //绘制文本
                itemScope(text, focused)
            }
        }
        BasicTextField(value = content, onValueChange = {
            if (it.length >= 6) inputCallback(it) else content = it
        }, modifier = Modifier
            .drawWithContent { }//清楚绘制内容
            .matchParentSize())//填充至父布局大小
    }

}