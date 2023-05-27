package com.carrati.mybills.appCompose.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.carrati.mybills.appCompose.extensions.navigateSingleTopTo
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
    val selectedDate by remember {
        mutableStateOf(
            Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
            }
        )
    }
    MainScreen(selectedDate)
}

@Composable
fun MainScreen(
    selectedDate: Calendar
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination?.route
    val mainNavDestinations = listOf(Home, Transactions)
    val currentScreen = mainNavDestinations.find { it.route == currentDestination } ?: Home

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Olá, userName", color = Color.White) },
                backgroundColor = Color(0xFF33B5E5),
                elevation = 0.dp
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                modifier = Modifier.padding(4.dp),
                backgroundColor = Color(0xFFFAC853)
            ) {
                Icon(
                    imageVector = Filled.Check,
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
                mainNavDestinations.forEach { item ->
                    BottomNavigationItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                        label = { Text(text = item.title, fontSize = 9.sp) },
                        selectedContentColor = Color(0xFF33B5E5),
                        unselectedContentColor = Color.Black.copy(0.4f),
                        alwaysShowLabel = true,
                        selected = item == currentScreen,
                        onClick = { navController.navigateSingleTopTo(item.route) }
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        MonthYearNavigationView(modifier = modifier, selectedDate = selectedDate)
        MainNavHost(navController = navController, selectedDate = selectedDate, modifier = modifier)
    }
}

@Composable
fun MonthYearNavigationView(
    modifier: Modifier,
    selectedDate: Calendar
) {
    Row(
        modifier = modifier.fillMaxWidth().background(Color(0xFF33B5E5)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.padding(horizontal = 32.dp),
            onClick = { selectedDate.add(Calendar.MONTH, 1) }
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
                text = formatter.format(selectedDate.time),
                color = Color.White,
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        }
        IconButton(
            modifier = Modifier.padding(horizontal = 32.dp),
            onClick = { selectedDate.add(Calendar.MONTH, -1) }
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
    selectedDate: Calendar,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            val viewModel = viewModel<HomeViewModel>()
            viewModel.loadData(selectedDate)
            HomeScreen(state = viewModel.state.value) { viewModel.onAddConta() }
        }
        composable(route = Transactions.route) {
            val viewModel = viewModel<TransactionsViewModel>()
            viewModel.loadData(selectedDate)
            TransactionsScreen(state = viewModel.state.value)
        }
    }
}
