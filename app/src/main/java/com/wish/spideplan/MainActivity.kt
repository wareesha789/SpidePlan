package com.wish.spideplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.wish.spideplan.data.database.SpidePlanDatabase
import com.wish.spideplan.data.repository.TaskRepository
import com.wish.spideplan.data.repository.QuoteRepository
import com.wish.spideplan.data.repository.SleepRepository
import com.wish.spideplan.data.repository.BrainDumpRepository
import com.wish.spideplan.navigation.SpidePlanNavigation
import com.wish.spideplan.ui.theme.SpidePlanTheme
import com.wish.spideplan.ui.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {
    
    private lateinit var database: SpidePlanDatabase
    private lateinit var taskRepository: TaskRepository
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var sleepRepository: SleepRepository
    private lateinit var brainDumpRepository: BrainDumpRepository
    
    private lateinit var homeViewModel: HomeViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // Initialize database and repositories
        setupDependencies()
        
        enableEdgeToEdge()
        setContent {
            SpidePlanTheme {
                SpidePlanApp()
            }
        }
    }
    
    private fun setupDependencies() {
        // Initialize database
        database = SpidePlanDatabase.getDatabase(this)
        
        // Initialize repositories
        taskRepository = TaskRepository(database.taskDao())
        quoteRepository = QuoteRepository(database.quoteDao())
        sleepRepository = SleepRepository(database.sleepDao())
        brainDumpRepository = BrainDumpRepository(database.brainDumpDao())
        
        // Initialize ViewModels
        homeViewModel = HomeViewModel(taskRepository, quoteRepository)
    }
    
    @Composable
    private fun SpidePlanApp() {
        val navController = rememberNavController()
        
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            SpidePlanNavigation(
                navController = navController,
                homeViewModel = homeViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}