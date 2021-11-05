/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.richmat.mytuya.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Support wide screen by making the content width max 840dp, centered horizontally.
 */
fun Modifier.supportWideScreen() =
    fillMaxWidth()
        .wrapContentWidth(align = Alignment.CenterHorizontally)
        .widthIn(max = 740.dp)//反转的时候用的

fun Modifier.confirmDone(pointSize: Dp) = drawWithContent {
    drawContent()
    //两种方式
//    drawIntoCanvas {
//        it.drawCircle()
//    }
    //实际上就是用的canvas的方法，封了一层
    drawCircle(color = Color(0xFFFF3D00),
        (pointSize / 2).toPx(),
        center = Offset(drawContext.size.width, 0f))
}