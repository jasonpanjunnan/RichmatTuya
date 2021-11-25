package com.richmat.mytuya.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SpeakerPhone
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.statusBarsPadding
import com.richmat.mytuya.ui.Home.Page

@ExperimentalAnimationApi
@Composable
fun HomeMenu(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeID: Long,
) {
    var isSelectRoom by remember {
        mutableStateOf(false)
    }
    TopAppBar(
        modifier = Modifier
            .statusBarsPadding(),
        navigationIcon = {
            //TODO 房间切换

            Row(modifier = modifier.clickable { isSelectRoom = !isSelectRoom }) {
                Text(text = "Rich")
                Icon(Icons.Default.MoreVert, contentDescription = "Rich")
            }
        },
        title = {
            Text(text = "Title")
        },
        actions = {
//            NewHomeMenu()
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.SpeakerPhone, contentDescription = "Speaker")
            }

            IconButton(onClick = {
                navController.navigate("${Page.SelectDevice.route}/$homeID")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
    )

    AnimatedVisibility(visible = isSelectRoom) {
        //要想显示在外边，可以把box放在最外层
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "切换房间")
            }
        }
    }
}

@Composable
fun NewHomeMenu(modifier: Modifier = Modifier) {
    var menuExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
    ) {
        IconButton(onClick = { menuExpanded = true }) {
            Icon(
                Icons.Default.MoreHoriz,
//            tint = Color.White,
                modifier = modifier,
                contentDescription = null
            )
        }

        MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))) {
            DropdownMenu(
                modifier = Modifier.width(150.dp),
                expanded = menuExpanded,
                onDismissRequest = {
                    menuExpanded = false
                },
            ) {
                dropList.forEachIndexed { index, roomMenu ->
                    DropdownMenuItem(onClick = {}) {
                        //TODO 可以添加一个分割线
                        MenuItem(roomMenu, {})
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(room: RoomMenu, onClick: () -> Unit) {
    Row(modifier = Modifier
        .padding(5.dp)
        .clickable { onClick() }) {
        Icon(room.icon, contentDescription = room.name)
        Text(text = room.name, maxLines = 1, modifier = Modifier.padding(start = 10.dp))
    }
}


@ExperimentalAnimationApi
@Preview
@Composable
fun ShowHomeMenu() {
    HomeMenu(navController = rememberNavController(), homeID = 1000L)
}