package ir.javagym.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import ir.javagym.readerapp.screens.ReaderSplashScreen
import ir.javagym.readerapp.screens.details.BookDetailsScreen
import ir.javagym.readerapp.screens.home.Home
import ir.javagym.readerapp.screens.home.HomeScreenViewModel
import ir.javagym.readerapp.screens.login.ReaderLoginScreen
import ir.javagym.readerapp.screens.search.BooksSearchViewModel
import ir.javagym.readerapp.screens.search.SearchScreen
import ir.javagym.readerapp.screens.stats.ReaderStatsScreen
import ir.javagym.readerapp.screens.update.BookUpdateScreen


@ExperimentalComposeUiApi
@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = ReaderScreens.SplashScreen.name ){

         composable(ReaderScreens.SplashScreen.name) {
             ReaderSplashScreen(navController = navController)
         }
        composable(ReaderScreens.LoginScreen.name) {
            ReaderLoginScreen(navController = navController)
        }

        composable(ReaderScreens.ReaderStatsScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderStatsScreen(navController = navController, viewModel = homeViewModel)
        }

        composable(ReaderScreens.ReaderHomeScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            Home(navController = navController, viewModel = homeViewModel)
        }

        composable(ReaderScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<BooksSearchViewModel>()

            SearchScreen(navController = navController, viewModel = searchViewModel)
        }



        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })) { backStackEntry ->
              backStackEntry.arguments?.getString("bookId").let {

                  BookDetailsScreen(navController = navController, bookId = it.toString())
              }
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}",
                  arguments = listOf(navArgument("bookItemId") {
                      type = NavType.StringType
                  })) { navBackStackEntry ->

            navBackStackEntry.arguments?.getString("bookItemId").let {
                BookUpdateScreen(navController = navController, bookItemId = it.toString())
            }

        }

    }

}