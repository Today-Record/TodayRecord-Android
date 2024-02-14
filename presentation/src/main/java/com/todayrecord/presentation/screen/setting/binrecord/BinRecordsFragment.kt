package com.todayrecord.presentation.screen.setting.binrecord

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.todayrecord.designsystem.component.TodayRecordTopAppBar
import com.todayrecord.designsystem.modifier.singleClickable
import com.todayrecord.designsystem.theme.TodayRecordColor
import com.todayrecord.designsystem.theme.TodayRecordTextStyle
import com.todayrecord.presentation.R
import com.todayrecord.presentation.screen.base.ComposableFragment
import com.todayrecord.presentation.util.TimeUtil
import com.todayrecord.presentation.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.presentation.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BinRecordsFragment : ComposableFragment() {

    private val binRecordsViewModel: BinRecordsViewModel by viewModels()

    @Composable
    override fun RootContent() = BinRecordScreen(binRecordsViewModel) {
        if (!findNavController().navigateUp()) requireActivity().finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                binRecordsViewModel.navigateToBinClearPopup.collect {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.bin_record_clear_popup_title))
                        .setMessage(resources.getString(R.string.bin_record_clear_popup_description))
                        .setNegativeButton(resources.getString(R.string.all_cancel)) { dialog, _: Int ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(resources.getString(R.string.all_confirm)) { dialog, _: Int ->
                            binRecordsViewModel.clearBinRecords()
                            dialog.dismiss()
                        }.show()
                }
            }

            launch {
                binRecordsViewModel.navigateToDetailRecord.collect {
                    findNavController().safeNavigate(BinRecordsFragmentDirections.actionBinRecordsFragmentToNavRecordDetail(it))
                }
            }
        }
    }
}

@Composable
fun BinRecordScreen(
    viewModel: BinRecordsViewModel = viewModel(),
    navigateToBack: () -> Unit
) {
    val records = viewModel.binRecords.collectAsLazyPagingItems(LocalLifecycleOwner.current.lifecycleScope.coroutineContext)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TodayRecordTopAppBar(
            title = stringResource(id = R.string.bin_record_title),
            navigationIcon = {
                IconButton(onClick = navigateToBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_back),
                        contentDescription = "back",
                        tint = Color.Unspecified
                    )
                }
            }
        )

        Box {
            BinRecordsContainer(
                records = records,
                navigateToRecordDetail = viewModel::navigateToDetailRecord
            )

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                shape = CircleShape,
                onClick = viewModel::navigateToBinClearPopup,
                containerColor = TodayRecordColor.color_c14d4d
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_bin),
                    contentDescription = "Bin Clear Button.",
                    tint = TodayRecordColor.color_ffffff
                )
            }
        }
    }
}

@Composable
fun BinRecordsContainer(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    records: LazyPagingItems<BinRecordsUiModel>,
    navigateToRecordDetail: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState
    ) {
        items(
            count = records.itemCount,
            key = records.itemKey(),
            contentType = records.itemContentType()
        ) { index ->
            when (val uiModel = records[index]) {
                is BinRecordsUiModel.BinRecordItem -> BinRecordContent(
                    recordUiModel = uiModel,
                    navigateToRecordDetail = navigateToRecordDetail
                )

                is BinRecordsUiModel.EmptyItem -> BinRecordsEmptyScreen(
                    modifier = Modifier.fillParentMaxSize()
                )

                else -> return@items
            }
        }
    }
}

@Composable
fun BinRecordContent(
    modifier: Modifier = Modifier,
    recordUiModel: BinRecordsUiModel.BinRecordItem,
    navigateToRecordDetail: (String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .singleClickable(onClick = { navigateToRecordDetail(recordUiModel.record.id) }),
        border = BorderStroke(1.dp, TodayRecordColor.color_d9d9d9),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = TodayRecordColor.color_ffffff)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = TimeUtil.getDateTimeFormatString(recordUiModel.record.date, stringResource(id = R.string.time_regex_year_month_day)),
                color = TodayRecordColor.color_000000,
                style = TodayRecordTextStyle.Body3.asComposeStyle()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = recordUiModel.record.content ?: "",
                color = TodayRecordColor.color_474a5a,
                style = TodayRecordTextStyle.Body2.asComposeStyle(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                recordUiModel.record.images.map {
                    item(key = it) { BinRecordContentForImage(imageUrl = it) }
                }
            }
        }
    }
}

@Composable
fun BinRecordContentForImage(imageUrl: String) {
    Card(
        border = BorderStroke(1.dp, TodayRecordColor.color_d9d9d9),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = TodayRecordColor.color_eeeeee)
    ) {
        AsyncImage(
            modifier = Modifier.size(60.dp),
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(imageUrl)
                .build(),
            contentDescription = "bin record image",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun BinRecordsEmptyScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_bin),
            contentDescription = "bin empty record"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.bin_record_list_empty),
            textAlign = TextAlign.Center,
            color = TodayRecordColor.color_474a5a,
            style = TodayRecordTextStyle.Body2.asComposeStyle()
        )
    }
}