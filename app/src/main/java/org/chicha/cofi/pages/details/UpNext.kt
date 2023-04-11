@file:OptIn(ExperimentalAnimationApi::class)

package org.chicha.cofi.pages.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.chicha.cofi.R
import org.chicha.cofi.components.StepListItem
import org.chicha.cofi.components.StepProgress
import org.chicha.cofi.share.Step
import org.chicha.cofi.share.components.slideUpDown
import org.chicha.cofi.ui.Spacing
import org.chicha.cofi.ui.card
import org.chicha.cofi.ui.shapes

@Composable
fun UpNext(modifier: Modifier = Modifier, step: Step) {
    Surface(modifier = modifier.animateContentSize(), shape = shapes.card, tonalElevation = 2.dp) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Spacing.big),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(Spacing.medium),
        ) {
            Text(
                text = stringResource(id = R.string.recipe_details_upNext),
                style = MaterialTheme.typography.titleMedium,
            )
            AnimatedContent(
                targetState = step,
                transitionSpec = slideUpDown { target, initial ->
                    (target.orderInRecipe ?: 0) > (initial.orderInRecipe ?: 0)
                },
            ) {
                StepListItem(step = it, stepProgress = StepProgress.Upcoming)
            }
        }
    }
}
