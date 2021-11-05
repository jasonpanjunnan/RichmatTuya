package com.richmat.mytuya.ui.newHome

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.compose.jetsurvey.theme.Gray100
import com.example.compose.jetsurvey.theme.Purple700
import com.richmat.mytuya.R

@Composable
fun PersonalScreen(navController: NavHostController) {
    Column(modifier = Modifier.padding(10.dp)) {
        TopButton(Modifier.fillMaxWidth(), navController)
        Spacer(modifier = Modifier.height(10.dp))
        PersonalMessage(
            Message(
                "点击设置昵称",
                "1234567890",
                painterResource(id = R.drawable.profile_picture)
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        ThirdService()
        Spacer(modifier = Modifier.height(10.dp))
        CommonlySetting()
    }
}

@Composable
fun CommonlySetting() {
    Card(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
            .padding(10.dp),
        backgroundColor = Purple700
    ) {

    }
}

@Composable
fun ThirdService(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(150.dp)
            .padding(10.dp)
            .fillMaxWidth(),
        backgroundColor = Gray100
    ) {
        Column() {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "语音等第三方")
            }
        }
    }
}

@Composable
fun PersonalMessage(message: Message) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        var isExpanded by remember {
            mutableStateOf(false)
        }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface
        )
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = message.name,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = message.phone,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun TopButton(modifier: Modifier = Modifier, navController: NavHostController) {
    Row(modifier = modifier.padding(5.dp), horizontalArrangement = Arrangement.End) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Scanner, contentDescription = "Scan")
        }
        IconButton(onClick = { navController.navigate(Page.Setting.route) }) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Scan")
        }
    }
}

data class Message(var name: String, var phone: String, var image: Painter)
