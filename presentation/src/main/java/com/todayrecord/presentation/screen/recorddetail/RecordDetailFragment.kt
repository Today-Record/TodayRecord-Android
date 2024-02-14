package com.todayrecord.presentation.screen.recorddetail

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.todayrecord.designsystem.component.TodayRecordTopAppBar
import com.todayrecord.designsystem.theme.TodayRecordColor
import com.todayrecord.designsystem.theme.TodayRecordTextStyle
import com.todayrecord.presentation.R
import com.todayrecord.presentation.screen.base.ComposableFragment
import com.todayrecord.presentation.util.Const.KEY_IS_RECORD_UPDATE
import com.todayrecord.presentation.util.TimeUtil
import com.todayrecord.presentation.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.presentation.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordDetailFragment : ComposableFragment() {

    private val recordDetailViewModel: RecordDetailViewModel by hiltNavGraphViewModels(R.id.nav_record_detail)

    @Composable
    override fun RootContent() = RecordDetailScreen(recordDetailViewModel) {
        if (!findNavController().navigateUp()) requireActivity().finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initNavBackstackObserve()
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                recordDetailViewModel.navigateToEditDialog.collect {
                    findNavController().safeNavigate(RecordDetailFragmentDirections.actionRecordDetailFragmentToRecordDetailEditBottomSheetDialogFragment())
                }
            }

            launch {
                recordDetailViewModel.navigateToBinEditDialog.collect {
                    findNavController().safeNavigate(RecordDetailFragmentDirections.actionRecordDetailFragmentToBinRecordDetailEditBottomSheetDialogFragment())
                }
            }

            launch {
                recordDetailViewModel.navigateToRecordEdit.collect {
                    if (findNavController().currentDestination?.id == R.id.recordDetailEditBottomSheetDialogFragment) {
                        findNavController().popBackStack()
                    }

                    findNavController().safeNavigate(RecordDetailFragmentDirections.actionRecordDetailFragmentToNavWriteRecord(it))
                }
            }

            launch {
                recordDetailViewModel.navigateToBack.collect {
                    with(findNavController()) {
                        if (currentDestination?.id == R.id.recordDetailEditBottomSheetDialogFragment
                            || currentDestination?.id == R.id.binRecordDetailEditBottomSheetDialogFragment
                        ) {
                            popBackStack()
                        }

                        if (!navigateUp()) {
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    private fun initNavBackstackObserve() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.recordDetailFragment)
        val resultObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!navBackStackEntry.savedStateHandle.contains(KEY_IS_RECORD_UPDATE)) return@LifecycleEventObserver
                val isUpdated: Boolean = navBackStackEntry.savedStateHandle[KEY_IS_RECORD_UPDATE] ?: false

                if (isUpdated) recordDetailViewModel.onRefresh()
                navBackStackEntry.savedStateHandle.remove<Boolean>(KEY_IS_RECORD_UPDATE)
            }
        }

        navBackStackEntry.lifecycle.addObserver(resultObserver)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(resultObserver)
            }
        })
    }
}

@Composable
fun RecordDetailScreen(
    viewModel: RecordDetailViewModel,
    navigateToBack: () -> Unit
) {
    val record by viewModel.record.collectAsStateWithLifecycle()
    val recordContentItem by viewModel.recordContentItem.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TodayRecordColor.color_ffffff)
    ) {
        RecordDetailTopAppbar(
            title = record?.date,
            navigateToBack = navigateToBack,
            navigateToEdit = viewModel::navigateToRecordDetailEditDialog
        )
        RecordDetailContents(recordContentItem)
    }
}

@Composable
fun RecordDetailTopAppbar(
    title: String?,
    navigateToBack: () -> Unit,
    navigateToEdit: () -> Unit
) {
    TodayRecordTopAppBar(
        title = TimeUtil.getDateTimeFormatString(title, stringResource(id = R.string.time_regex_year_month_day)),
        navigationIcon = {
            IconButton(navigateToBack) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_back),
                    contentDescription = "back",
                    tint = Color.Unspecified
                )
            }
        },
        actions = {
            TextButton(navigateToEdit) {
                Text(
                    text = stringResource(id = R.string.all_edit),
                    color = TodayRecordColor.color_474a5a,
                    style = TodayRecordTextStyle.Body3.asComposeStyle()
                )
            }
        }
    )
}

@Composable
fun RecordDetailContents(contents: List<RecordDetailItemUiModel>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TodayRecordColor.color_ffffff)
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 18.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        contents.map {
            item(key = it.hashCode()) {
                when (it) {
                    is RecordDetailItemUiModel.RecordDetailImageItem -> RecordDetailImageContent(it)
                    is RecordDetailItemUiModel.RecordDetailTextItem -> RecordDetailTextContent(it)
                }
            }
        }
    }
}

@Composable
private fun RecordDetailImageContent(content: RecordDetailItemUiModel.RecordDetailImageItem) {
    Surface(shape = MaterialTheme.shapes.extraSmall) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(content.imageUrl)
                .build(),
            contentDescription = "record image",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun RecordDetailTextContent(content: RecordDetailItemUiModel.RecordDetailTextItem) {
    Text(
        text = content.text ?: "",
        color = TodayRecordColor.color_474a5a,
        style = TodayRecordTextStyle.Body2.asComposeStyle(),
    )
    Spacer(modifier = Modifier.height(120.dp))
}