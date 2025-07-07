package com.example.galleryapp.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.galleryapp.R
import com.example.galleryapp.presentation.common.ui.theme.AppTheme
import com.example.galleryapp.presentation.common.ui.theme.White

@Composable
fun PermissionCard(title: String, description: String, icon: Painter) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.secondaryDefaultLight, shape = RoundedCornerShape(10.dp))
            .padding(vertical = 12.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                color = AppTheme.colors.textPrimaryOnColor,
            )
            Text(
                text = description,
                color = AppTheme.colors.textSecondaryOnSecondaryHover,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W400,
                    lineHeight = 12.sp,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
                )
        }
    }
}

@Preview
@Composable
fun PermissionCardPreview() {
    AppTheme {
        PermissionCard(title = "Location", description = "Location Description", icon = painterResource(id = R.drawable.ic_launcher_background))
    }
}
