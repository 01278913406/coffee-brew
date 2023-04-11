@file:OptIn(ExperimentalAnimationApi::class)

package org.chicha.cofi.share.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import org.chicha.cofi.share.R
import org.chicha.cofi.share.Step
import org.chicha.cofi.utils.roundToDecimals
import org.chicha.cofi.utils.toStringDuration
import org.chicha.cofi.utils.toStringShort
import kotlin.math.roundToInt

@Composable
fun ColumnScope.TimerValue(
    modifier: Modifier = Modifier,
    currentStep: Step,
    animatedProgressValue: Float,
    weightMultiplier: Float = 1f,
    alreadyDoneWeight: Float = 0f,
    color: Color,
    maxLines: Int,
    style: TextStyle,
) {
    Box(modifier = modifier.align(Alignment.CenterHorizontally)) {
        AnimatedContent(
            targetState = currentStep,
            contentAlignment = Alignment.Center,
            transitionSpec = slideUpDown { target, initial ->
                (target.orderInRecipe ?: 0) > (initial.orderInRecipe ?: 0)
            },
        ) {
            val currentStepValue = it.value ?: return@AnimatedContent
            val currentValueFromProgress = remember(currentStepValue, animatedProgressValue) {
                (currentStepValue * animatedProgressValue)
            }
            val currentValueWithMultiplier = remember(currentValueFromProgress, weightMultiplier) {
                (currentValueFromProgress * weightMultiplier) + alreadyDoneWeight
            }
            val currentTargetValue by remember(currentStepValue, weightMultiplier) {
                derivedStateOf {
                    (currentStepValue * weightMultiplier) + alreadyDoneWeight
                }
            }
            val targetString = currentTargetValue.toStringShort()
            val shouldShowDecimals by remember {
                derivedStateOf {
                    targetString.contains(".")
                }
            }
            val currentValueString: Number = if (shouldShowDecimals) {
                currentValueWithMultiplier.roundToDecimals()
            } else {
                currentValueWithMultiplier.roundToInt()
            }
            Text(
                text = stringResource(
                    id = R.string.timer_progress_weight,
                    currentValueString,
                    targetString,
                ),
                color = color,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("timer_value"),
                style = style,
            )
        }
    }
}

@Composable
fun StepNameText(
    currentStep: Step,
    color: Color,
    style: TextStyle,
    maxLines: Int,
    paddingHorizontal: Dp,
) {
    AnimatedContent(
        targetState = currentStep,
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = paddingHorizontal),
        transitionSpec = slideUpDown { target, initial ->
            (target.orderInRecipe ?: 0) > (initial.orderInRecipe ?: 0)
        },
    ) {
        Text(
            text = if (it.time != null) {
                stringResource(
                    id = R.string.timer_step_name_time,
                    it.name,
                    it.time / 1000,
                )
            } else {
                it.name
            },
            color = color,
            style = style,
            textAlign = TextAlign.Center,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.testTag("timer_name"),
        )
    }

}

@Composable
fun ColumnScope.TimeText(
    modifier: Modifier = Modifier,
    currentStep: Step, animatedProgressValue: Float,
    color: Color, maxLines: Int, style: TextStyle,
    paddingHorizontal: Dp,
    showMillis: Boolean,
) {
    val durationInString = if (currentStep.time == null) {
        stringResource(id = R.string.recipe_details_noTime)
    } else {
        val duration = (currentStep.time * animatedProgressValue).toInt()
        duration.toStringDuration(
            padMillis = true,
            padMinutes = true,
            padSeconds = true,
            showMillis = showMillis,
        )
    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .align(Alignment.CenterHorizontally)
            .padding(horizontal = paddingHorizontal),
    ) {
        Text(
            text = durationInString,
            style = style,
            color = color,
            textAlign = TextAlign.Center,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .testTag("timer_duration"),
        )
    }
}
