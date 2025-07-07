package com.example.galleryapp.presentation.common.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.galleryapp.presentation.common.ui.theme.AppTheme

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    prefixIconRes: Int? = null,
    buttonColor: Color = AppTheme.colors.secondaryDefaultLight
    ) {
    Button(
        modifier = modifier.fillMaxWidth().heightIn(48.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            disabledContainerColor = buttonColor.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(
            vertical = 15.dp,
            horizontal = 24.dp
        ),
        enabled = enabled
    ) {
        if (prefixIconRes != null) {
            Icon(
                painter = painterResource(id = prefixIconRes),
                contentDescription = null,
                tint = White,
                modifier = Modifier.padding(end = 8.dp) // Padding between icon and text
            )
        }
        Text(
            text = text, fontSize = 14.sp,
            fontWeight = FontWeight.W600, color = if (enabled) AppTheme.colors.textPrimaryOnColor
            else AppTheme.colors.textPrimaryOnColor.copy(alpha = 0.5f)
        )
    }
}


@Preview
@Composable
fun PrimaryButtonPreview() {
    AppTheme {
        PrimaryButton(text = "Primary Action", onClick = {}, enabled = true)
    }
}
