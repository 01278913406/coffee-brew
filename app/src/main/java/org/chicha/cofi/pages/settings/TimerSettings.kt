@file:OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class,
)

package org.chicha.cofi.pages.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.chicha.cofi.R
import org.chicha.cofi.components.Material3Dialog
import org.chicha.cofi.components.PiPAwareAppBar
import org.chicha.cofi.components.createAppBarBehavior
import org.chicha.cofi.model.DataStore
import org.chicha.cofi.model.NEXT_STEP_ENABLED_DEFAULT_VALUE
import org.chicha.cofi.model.PIP_DEFAULT_VALUE
import org.chicha.cofi.share.*
import org.chicha.cofi.ui.CofiTheme
import org.chicha.cofi.utils.checkPiPPermission
import org.chicha.cofi.utils.getDefaultPadding
import kotlinx.coroutines.launch

@Composable
fun TimerSettings(goBack: () -> Unit) {
    val context = LocalContext.current

    val dataStore = DataStore(context)
    val isStepSoundEnabled by dataStore.getStepChangeSoundSetting()
        .collectAsState(STEP_SOUND_DEFAULT_VALUE)
    val isStepVibrationEnabled by dataStore.getStepChangeVibrationSetting()
        .collectAsState(STEP_VIBRATION_DEFAULT_VALUE)
    val isPiPEnabled by dataStore.getPiPSetting().collectAsState(PIP_DEFAULT_VALUE)
    val isNextStepEnabled by dataStore.getNextStepSetting().collectAsState(
        NEXT_STEP_ENABLED_DEFAULT_VALUE,
    )
    val combineWeightState by dataStore.getWeightSetting()
        .collectAsState(COMBINE_WEIGHT_DEFAULT_VALUE)
    var showCombineWeightDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val appBarBehavior = createAppBarBehavior()

    val hasPiPPermission = checkPiPPermission(context)
    val togglePiP: () -> Unit = {
        coroutineScope.launch { dataStore.togglePipSetting() }
    }
    val toggleNextStep: () -> Unit = {
        coroutineScope.launch { dataStore.toggleNextStepEnabled() }
    }
    val toggleStepChangeSound: () -> Unit = {
        coroutineScope.launch { dataStore.toggleStepChangeSound() }
    }
    val toggleStepChangeVibration: () -> Unit = {
        coroutineScope.launch { dataStore.toggleStepChangeVibration() }
    }

    Scaffold(
        topBar = {
            PiPAwareAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_timer_item),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                scrollBehavior = appBarBehavior,
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .nestedScroll(appBarBehavior.nestedScrollConnection)
                .fillMaxSize(),
            contentPadding = getDefaultPadding(
                paddingValues = it,
                additionalStartPadding = 0.dp,
                additionalEndPadding = 0.dp,
            ),
        ) {
            item {
                ListItem(
                    text = {
                        Text(text = stringResource(id = R.string.settings_pip_item))
                    },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_picture_in_picture),
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier
                        .settingsItemModifier(
                            onClick = togglePiP,
                            enabled = hasPiPPermission,
                        )
                        .testTag("settings_timer_list_item_pip"),
                    trailing = {
                        Switch(
                            modifier = Modifier.testTag("settings_timer_switch_pip"),
                            checked = if (!hasPiPPermission) false else isPiPEnabled,
                            onCheckedChange = { togglePiP() },
                            enabled = hasPiPPermission,
                        )
                    },
                )
            }
            item {
                ListItem(
                    text = {
                        Text(text = stringResource(id = R.string.settings_step_sound_item))
                    },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_sound),
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier
                        .settingsItemModifier(onClick = toggleStepChangeSound)
                        .testTag("settings_timer_list_item_sound"),
                    trailing = {
                        Switch(
                            modifier = Modifier.testTag("settings_timer_switch_sound"),
                            checked = isStepSoundEnabled,
                            onCheckedChange = { toggleStepChangeSound() },
                        )
                    },
                )
            }
            item {
                ListItem(
                    text = {
                        Text(text = stringResource(id = R.string.settings_step_vibrate_item))
                    },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_vibration),
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier
                        .settingsItemModifier(onClick = toggleStepChangeVibration)
                        .testTag("settings_timer_list_item_vibration"),
                    trailing = {
                        Switch(
                            modifier = Modifier.testTag("settings_timer_switch_vibration"),
                            checked = isStepVibrationEnabled,
                            onCheckedChange = { toggleStepChangeVibration() },
                        )
                    },
                )
            }
            item {
                ListItem(
                    text = {
                        Text(text = stringResource(id = R.string.settings_step_next_step_item))
                    },
                    icon = {
                        Icon(
                            Icons.Rounded.ArrowForward,
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier
                        .settingsItemModifier(onClick = toggleStepChangeVibration)
                        .testTag("settings_timer_list_item_nextStep"),
                    trailing = {
                        Switch(
                            modifier = Modifier.testTag("settings_timer_switch_nextStep"),
                            checked = isNextStepEnabled,
                            onCheckedChange = { toggleNextStep() },
                        )
                    },
                )
            }
            item {
                ListItem(
                    overlineText = {
                        Text(text = stringResource(id = R.string.settings_combine_weight_item))
                    },
                    text = {
                        Text(
                            text = stringResource(
                                stringToCombineWeight(combineWeightState).settingsStringId,
                            ),
                            fontWeight = FontWeight.Light,
                        )
                    },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_list_add),
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier
                        .settingsItemModifier(onClick = { showCombineWeightDialog = true })
                        .testTag("settings_timer_list_item_weight"),
                )
                if (showCombineWeightDialog) {
                    CombineWeightDialog(
                        dismiss = { showCombineWeightDialog = false },
                        selectCombineMethod = {
                            coroutineScope.launch {
                                dataStore.selectCombineMethod(it)
                                showCombineWeightDialog = false
                            }
                        },
                        combineWeightState = combineWeightState,
                    )
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CombineWeightDialog(
    dismiss: () -> Unit,
    selectCombineMethod: (CombineWeight) -> Unit,
    combineWeightState: String,
) {
    Material3Dialog(onDismissRequest = dismiss) {
        CombineWeight.values().forEach {
            ListItem(
                text = { Text(stringResource(id = it.settingsStringId)) },
                modifier = Modifier
                    .selectable(
                        selected = combineWeightState == it.name,
                        onClick = { selectCombineMethod(it) },
                    )
                    .testTag("settings_timer_list_item_weight_dialog"),
                icon = {
                    RadioButton(
                        selected = combineWeightState == it.name,
                        onClick = { selectCombineMethod(it) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.secondary,
                        ),
                    )
                },
            )
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Preview
@Composable
fun SettingsTimerPreview() {
    CofiTheme() {
        TimerSettings(goBack = { })
    }
}