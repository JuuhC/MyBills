package com.carrati.mybills.appCompose.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.carrati.mybills.appCompose.R.drawable
import com.carrati.mybills.appCompose.extensions.navigateSingleTopTo
import com.carrati.mybills.appCompose.extensions.nextMonth
import com.carrati.mybills.appCompose.extensions.previousMonth
import com.carrati.mybills.appCompose.extensions.startDay
import com.carrati.mybills.appCompose.ui.home.HomeScreen
import com.carrati.mybills.appCompose.ui.main.MainDestination.Home
import com.carrati.mybills.appCompose.ui.main.MainDestination.Transactions
import com.carrati.mybills.appCompose.ui.main.home.HomeViewModel
import com.carrati.mybills.appCompose.ui.main.transactions.TransactionsScreen
import com.carrati.mybills.appCompose.ui.main.transactions.TransactionsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Preview
@Composable
private fun MainScreenPreview() {
    val selectedDate = remember {
        mutableStateOf(
            Calendar.getInstance().startDay()
        )
    }
    MainScreen(selectedDate)
}

@Composable
fun MainScreen(
    selectedDate: MutableState<Calendar>
) {
    val navController = rememberNavController()
    val isFabMenuVisible = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Olá, userName", color = Color.White) },
                    backgroundColor = Color(0xFF33B5E5),
                    elevation = 0.dp,
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                        }
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { isFabMenuVisible.value = isFabMenuVisible.value.not() },
                    modifier = Modifier.padding(4.dp),
                    backgroundColor = Color(0xFFFAC853)
                ) {
                    Icon(
                        imageVector = Filled.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            bottomBar = {
                BottomAppBar(
                    backgroundColor = Color.White,
                    cutoutShape = CircleShape,
                    elevation = 2.dp
                ) {
                    val currentBackStack by navController.currentBackStackEntryAsState()
                    val currentDestinations = currentBackStack?.destination?.hierarchy

                    listOf(Home, Transactions).forEach { item ->
                        BottomNavigationItem(
                            icon = {
                                Icon(imageVector = item.icon, contentDescription = item.title)
                            },
                            label = { Text(text = item.title, fontSize = 10.sp) },
                            selectedContentColor = Color(0xFF33B5E5),
                            unselectedContentColor = Color.Black.copy(0.4f),
                            alwaysShowLabel = true,
                            selected = currentDestinations?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigateSingleTopTo(item.route)
                            }
                        )
                    }
                }
            },
            backgroundColor = Color(0xFFEDF3FB)
        ) { innerPadding ->
            Column {
                MonthYearNavigationView(selectedDate = selectedDate)
                MainNavHost(
                    navController = navController,
                    selectedDate = selectedDate,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        MainFABMenu(isFabMenuVisible)
    }
}

@Composable
fun MonthYearNavigationView(
    selectedDate: MutableState<Calendar>
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color(0xFF33B5E5)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.padding(horizontal = 32.dp),
            onClick = { selectedDate.value = selectedDate.value.previousMonth() }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                tint = Color.White,
                contentDescription = null
            )
        }
        TextButton(
            onClick = {}
        ) {
            val locale = Locale.getDefault()
            val formatter = SimpleDateFormat("MMMM yyyy", locale)
            Text(
                text = formatter.format(selectedDate.value.time),
                color = Color.White,
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        }
        IconButton(
            modifier = Modifier.padding(horizontal = 32.dp),
            onClick = { selectedDate.value = selectedDate.value.nextMonth() }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                tint = Color.White,
                contentDescription = null
            )
        }
    }
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    selectedDate: MutableState<Calendar>,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            val viewModel = viewModel<HomeViewModel>()
            viewModel.loadData(selectedDate.value)
            HomeScreen(state = viewModel.state.value) { viewModel.onAddConta() }
        }
        composable(route = Transactions.route) {
            val viewModel = viewModel<TransactionsViewModel>()
            viewModel.loadData(selectedDate.value)
            TransactionsScreen(state = viewModel.state.value)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainFABMenu(
    isFabMenuVisible: MutableState<Boolean>
) {
    AnimatedVisibility(
        visible = isFabMenuVisible.value,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .animateEnterExit(
                        enter = fadeIn(),
                        exit = fadeOut()
                    ),
                color = Color.Black.copy(alpha = 0.7f)
            ) {}

            FloatingActionButton(
                onClick = { isFabMenuVisible.value = isFabMenuVisible.value.not() },
                modifier = Modifier
                    .padding(28.dp)
                    .animateEnterExit(
                        enter = fadeIn(),
                        exit = fadeOut()
                    ),
                backgroundColor = Color(0xFFFAC853),
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(
                    imageVector = Filled.Close,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                MainFABMenuItem(
                    animScope = this@AnimatedVisibility,
                    enterAnim = fadeIn() + slideIn { IntOffset(it.width, 100) },
                    exitAnim = fadeOut() + slideOut { IntOffset(it.width, 100) },
                    bottomPadding = 50.dp,
                    text = "Despesa",
                    fabBackgroudColor = Color(0xFFF866A1),
                    iconRes = drawable.ic_despesa_24dp
                ) {}
                MainFABMenuItem(
                    animScope = this@AnimatedVisibility,
                    enterAnim = fadeIn() + slideIn { IntOffset(0, 100) },
                    exitAnim = fadeOut() + slideOut { IntOffset(0, 100) },
                    bottomPadding = 110.dp,
                    text = "Transferência",
                    fabBackgroudColor = Color(0xFF33B5E5),
                    iconRes = drawable.ic_transferencia_24dp
                ) {}
                MainFABMenuItem(
                    animScope = this@AnimatedVisibility,
                    enterAnim = fadeIn() + slideIn { IntOffset(-it.width, 100) },
                    exitAnim = fadeOut() + slideOut { IntOffset(-it.width, 100) },
                    bottomPadding = 50.dp,
                    text = "Receita",
                    fabBackgroudColor = Color(0xFF2AA653),
                    iconRes = drawable.ic_receita_24dp
                ) {}
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainFABMenuItem(
    animScope: AnimatedVisibilityScope,
    enterAnim: EnterTransition,
    exitAnim: ExitTransition,
    bottomPadding: Dp,
    text: String,
    fabBackgroudColor: Color,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    with(animScope) {
        Column(
            Modifier
                .padding(bottom = bottomPadding).padding(horizontal = 8.dp)
                .animateEnterExit(
                    enter = enterAnim,
                    exit = exitAnim
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                fontSize = 11.sp,
                color = Color.White
            )
            FloatingActionButton(
                onClick = { onClick() },
                modifier = Modifier.scale(0.7f),
                backgroundColor = fabBackgroudColor
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}
