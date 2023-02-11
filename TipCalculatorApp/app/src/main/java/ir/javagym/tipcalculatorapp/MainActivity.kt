package ir.javagym.tipcalculatorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.javagym.tipcalculatorapp.components.InputField
import ir.javagym.tipcalculatorapp.ui.theme.TipCalculatorAppTheme
import ir.javagym.tipcalculatorapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    TipCalculatorAppTheme {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun MainContent() {
    Column() {
        val totalTipPerPerson = remember {
            mutableStateOf(0.0)
        }
        TopHeader(totalTipPerPerson.value)
        BillForm(totalTipPerPerson = totalTipPerPerson) { billAmt ->
            Log.d("billAmt", "MainContent: $billAmt")
        }
    }
}

@Composable
fun TopHeader(totalPerPerson: Double = 134.0) {
    Surface(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(24.dp))),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    totalTipPerPerson: MutableState<Double>,
    onValChange: (String) -> Unit
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validateBillState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enable = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validateBillState)
                        return@KeyboardActions
                    onValChange(totalBillState.value)
                    keyboardController?.hide()
                }
            )
            if (validateBillState) {
            val personCount = remember {
                mutableStateOf(1)
            }
            SplitPerPerson(personCount)

            val tipSliderValue = remember {
                mutableStateOf(0f)
            }
            val tipAmountState = remember {
                mutableStateOf(0.0)
            }

            TipsRow(tipSliderValue, totalBillState, tipAmountState)

            totalTipPerPerson.value = (totalBillState.value.toDouble() + tipAmountState.value).div(personCount.value)

            } else {
                totalTipPerPerson.value = 0.0
                Box() {

                }
            }
        }
    }
}

@Composable
fun SplitPerPerson(personCount: MutableState<Int>) {
    val personRange = IntRange(start = 1, endInclusive = 100)
    Row(
        modifier = Modifier
            .padding(start = 15.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Split",
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.padding(end = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            RoundIconButton(
                imageVector = Icons.Default.Remove,
                onClick = {
                    if (personCount.value > personRange.first)
                        personCount.value = personCount.value - 1
                }
            )
            Text(
                text = "${personCount.value}",
                modifier = Modifier
                    .padding(start = 9.dp, end = 9.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
            RoundIconButton(
                imageVector = Icons.Default.Add,
                onClick = {
                    if (personCount.value < personRange.last)
                        personCount.value = personCount.value + 1
                }
            )
        }
    }
}

@Composable
fun TipsRow(
    tipSliderValue: MutableState<Float>,
    totalBillState: MutableState<String>,
    tipAmountState: MutableState<Double>
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Tip",
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$${tipAmountState.value}",
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .padding(end = 40.dp)
        )
    }

    // Tip Percentage
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val tipPercentage = tipSliderValue.value.times(100).toInt()
        Text(text = "$tipPercentage%")
        Spacer(modifier = Modifier.height(10.dp))
        Slider(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            value = tipSliderValue.value,
            onValueChange = { newVal ->
                tipSliderValue.value = newVal
                tipAmountState.value = calculateTipAmount(totalBillState.value.toDouble(), tipPercentage)
            },
            steps = 5
        )
    }
}

fun calculateTipAmount(totalBill: Double, tipPercentage: Int): Double {
    return totalBill.times(tipPercentage).div(100)
}
