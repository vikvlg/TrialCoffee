package ru.vik.trials.coffee.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vik.trials.coffee.R

/**
 * Поле ввода текста.
 *
 * @param labelId Строка подсказка над полем.
 * @param defTextId Отображаемая подсказка внутри поля, пока пользователь ничего не ввел.
 * @param text Хранилище вводимого текста.
 * @param password Флаг, что поле вода предназначено для пароля.
 * */
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

/**
 * Поле с горизонтальным выбором числа.
 *
 * Взято с просторов интернета.
 *
 * @param height Высота поля.
 * @param min Минимальное число для выбора.
 * @param max Максимальное число для выбора.
 * @param default Значение по умолчанию.
 * @param onValueChange Событие изменения выбранного числа.
 */
@Composable
fun HorizontalNumberPicker(
    //modifier: Modifier = Modifier,
    height: Dp = 45.dp,
    min: Int = 0,
    max: Int = 10,
    default: Int = min,
    onValueChange: (Int) -> Unit = {}
) {
    val number = remember { mutableStateOf(default) }

    Row {
        PickerButton(
            size = height,
            drawable = R.drawable.ic_minus,
            enabled = number.value > min,
            onClick = {
                if (number.value > min) number.value--
                onValueChange(number.value)
            }
        )

        Text(
            text = number.value.toString(),
            fontSize = (height.value).sp,
            modifier = Modifier
                //.padding(10.dp)
                .height(IntrinsicSize.Max)
                .align(CenterVertically)
        )

        PickerButton(
            size = height,
            drawable = R.drawable.ic_plus,
            enabled = number.value < max,
            onClick = {
                if (number.value < max) number.value++
                onValueChange(number.value)
            }
        )
    }
}

/**
 * Кнопка для [HorizontalNumberPicker].
 *
 * @param size Размер кнопки.
 * @param drawable Иконка кнопки.
 * @param enabled Флаг доступности кнопки.
 * @param onClick Событие нажатия кнопки.
 */
@Composable
fun PickerButton(
    size: Dp = 45.dp,
    @DrawableRes drawable: Int,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    val contentDesc = LocalContext.current.resources.getResourceName(drawable)
    val backgroundColor = if (enabled) MaterialTheme.colorScheme.secondary else Color.LightGray

    Image(
        painter = painterResource(id = drawable),
        contentDescription = contentDesc,
        modifier = Modifier
            .padding(4.dp)
            .background(backgroundColor, CircleShape)
            .clip(CircleShape)
            .size(size = size)
            .clickable(
                enabled = enabled,
                onClick = { onClick() }
            )
    )
}
