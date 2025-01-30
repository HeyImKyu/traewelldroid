package de.hbch.traewelling.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.hbch.traewelling.R
import de.hbch.traewelling.api.models.status.Status
import de.hbch.traewelling.shared.FeatureFlags
import de.hbch.traewelling.shared.LoggedInUserViewModel
import de.hbch.traewelling.theme.LocalFont
import de.hbch.traewelling.theme.getBTModern
import de.hbch.traewelling.ui.composables.ButtonWithIconAndText
import de.hbch.traewelling.ui.include.cardSearchStation.CardSearch
import de.hbch.traewelling.ui.include.status.CheckInCardViewModel
import de.hbch.traewelling.ui.wrapped.WrappedTeaser
import de.hbch.traewelling.util.OnBottomReached
import de.hbch.traewelling.util.checkInList
import de.hbch.traewelling.util.openLink
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Dashboard(
    loggedInUserViewModel: LoggedInUserViewModel,
    joinConnection: (Status) -> Unit,
    searchConnectionsAction: (Int, ZonedDateTime?) -> Unit = { _, _ -> },
    userSelectedAction: (String, Boolean, Boolean) -> Unit = { _, _, _ -> },
    statusSelectedAction: (Int) -> Unit = { },
    statusDeletedAction: () -> Unit = { },
    statusEditAction: (Status) -> Unit = { }
) {
    val dashboardViewModel: DashboardFragmentViewModel = viewModel()
    val checkInCardViewModel : CheckInCardViewModel = viewModel()
    val refreshing by dashboardViewModel.isRefreshing.observeAsState(false)
    val checkIns = remember { dashboardViewModel.checkIns }
    val coroutineScope = rememberCoroutineScope()
    var currentPage by remember { mutableIntStateOf(1) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            currentPage = 1
            dashboardViewModel.refresh()
        }
    )
    val checkInListState = rememberLazyListState()

    val featureFlags = remember { FeatureFlags.getInstance() }
    val wrappedActive by featureFlags.wrappedActive.observeAsState(false)
    val trwlDown by featureFlags.trwlDown.observeAsState(false)

    checkInListState.OnBottomReached {
        if (dashboardViewModel.checkIns.size > 0) {
            dashboardViewModel.loadCheckIns(++currentPage)
        } else {
            loggedInUserViewModel.getLoggedInUser()
            loggedInUserViewModel.getLastVisitedStations {  }
            coroutineScope.launch {
                loggedInUserViewModel.updateCurrentStatus()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            userScrollEnabled = true,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = checkInListState
        ) {
            item {
                CardSearch(
                    onStationSelected = { station ->
                        searchConnectionsAction(station, null)
                    },
                    homelandStationData = loggedInUserViewModel.home,
                    recentStationsData = loggedInUserViewModel.lastVisitedStations,
                    onUserSelected = {
                        userSelectedAction(it.username, it.privateProfile, it.following)
                    }
                )
            }

            if (trwlDown) {
                item {
                    val context = LocalContext.current
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp).fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val text = stringResource(R.string.notice)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_error),
                                        contentDescription = null,
                                        tint = Color.Red
                                    )
                                    Text(
                                        text = text,
                                        fontFamily = getBTModern(text),
                                        style = LocalFont.current.headlineSmall,
                                        color = Color.Red
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.trwl_api_error),
                                    style = LocalFont.current.bodyMedium
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                ButtonWithIconAndText(
                                    stringId = R.string.traewelling_de,
                                    drawableId = R.drawable.ic_arrow_right,
                                    onClick = {
                                        context.openLink("https://traewelling.de")
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (wrappedActive) {
                item {
                    WrappedTeaser(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            checkInList(
                checkIns,
                checkInCardViewModel,
                loggedInUserViewModel,
                joinConnection,
                searchConnectionsAction,
                statusSelectedAction,
                statusEditAction,
                statusDeletedAction,
                userSelectedAction,
            )
        }

        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = refreshing,
            state = pullRefreshState
        )
    }
}
