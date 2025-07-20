package ru.vik.trials.coffee.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp

@Composable
fun InputText(@StringRes labelId: Int, @StringRes defTextId: Int, text: MutableState<TextFieldValue>, password: Boolean = false) {
    val keyboardType = if (password) KeyboardType.Password else KeyboardType.Text
    val visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None
    val label = stringResource(labelId)
    Text(
        text = label,
        modifier = Modifier
            .fillMaxWidth(fraction = 0.9f)
    )
    BasicTextField(
        value = text.value,
        onValueChange = { text.value = it },
        modifier = Modifier
            .fillMaxWidth(fraction = 0.9f)
            .height(48.dp)
            .border(2.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(start = 16.dp, top = 14.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Send,
        ),
        visualTransformation = visualTransformation,
        singleLine = true,
        //cursorBrush = SolidColor(LocalContentColor.current),
        textStyle = LocalTextStyle.current.copy(
            color = LocalContentColor.current,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Bottom,
                trim = LineHeightStyle.Trim.FirstLineTop,
            )
        ),
        decorationBox = { innerTextField ->
            if (text.value.text.isEmpty())
                Text(
                    text = stringResource(defTextId),
                    color = Color.Gray
                )
            innerTextField()
        },
    )
    Spacer(modifier = Modifier.height(8.dp))
}

