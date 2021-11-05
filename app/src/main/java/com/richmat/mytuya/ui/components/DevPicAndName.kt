package com.richmat.mytuya.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Segment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DevPicAndName(
    name: String,
    showDetail: () -> Unit,
    onWidgetClick: () -> Unit,
    painter: Painter,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier
//        .padding(vertical = 10.dp, horizontal = 10.dp)
        .clickable(onClick = onWidgetClick)) {
        Row(modifier = modifier) {
            Image(painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .sizeIn(maxHeight = 80.dp,
                        maxWidth = 80.dp,
                        minWidth = 40.dp,
                        minHeight = 40.dp))
            Column(modifier = Modifier.padding(start = 5.dp)) {
                Row {
                    Text(text = name,
                        fontSize = 27.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f))
                    IconButton(onClick = showDetail) {
                        Icon(imageVector = Icons.Default.Segment,
                            contentDescription = null,
                            modifier = Modifier
                                .weight(1f)
//                                .alignByBaseline()
                                .align(Bottom)
                                .wrapContentWidth(Alignment.End)
                                .padding(horizontal = 5.dp))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = subtitle,
//                        modifier = Modifier.wrapContentHeight(Bottom),/////
                    fontSize = 16.sp)
            }
        }
    }
}