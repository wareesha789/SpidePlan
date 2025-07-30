package com.wish.spideplan.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wish.spideplan.data.model.Task
import com.wish.spideplan.data.model.TaskCategory
import com.wish.spideplan.ui.theme.*
import com.wish.spideplan.ui.viewmodel.HomeViewModel
import com.wish.spideplan.ui.viewmodel.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToAddTask: () -> Unit,
    onNavigateToSleep: () -> Unit,
    onNavigateToBrainDump: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SpiderRed)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Greeting Section
                item {
                    GreetingCard(
                        greeting = uiState.greeting,
                        completedTasks = uiState.completedTasksToday,
                        totalTasks = uiState.totalTasksToday
                    )
                }
                
                // Daily Quote Section
                item {
                    uiState.dailyQuote?.let { quote ->
                        QuoteCard(
                            quote = quote.text,
                            author = quote.author,
                            source = quote.source,
                            onRefresh = { viewModel.refreshQuote() }
                        )
                    }
                }
                
                // Quick Actions
                item {
                    QuickActionsRow(
                        onAddTask = onNavigateToAddTask,
                        onSleep = onNavigateToSleep,
                        onBrainDump = onNavigateToBrainDump
                    )
                }
                
                // Overdue Tasks
                if (uiState.overdueTasks.isNotEmpty()) {
                    item {
                        Text(
                            text = "⚠️ Overdue Tasks",
                            style = MaterialTheme.typography.titleMedium,
                            color = WarningColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(uiState.overdueTasks) { task ->
                        TaskCard(
                            task = task,
                            onToggleComplete = { 
                                if (task.isCompleted) {
                                    viewModel.uncompleteTask(task.id)
                                } else {
                                    viewModel.completeTask(task.id)
                                }
                            },
                            isOverdue = true
                        )
                    }
                }
                
                // Today's Tasks
                item {
                    Text(
                        text = "📅 Today's Tasks",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                if (uiState.todaysTasks.isEmpty()) {
                    item {
                        EmptyTasksCard(onAddTask = onNavigateToAddTask)
                    }
                } else {
                    items(uiState.todaysTasks) { task ->
                        TaskCard(
                            task = task,
                            onToggleComplete = { 
                                if (task.isCompleted) {
                                    viewModel.uncompleteTask(task.id)
                                } else {
                                    viewModel.completeTask(task.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Error handling
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Show error snackbar or dialog
        }
    }
}

@Composable
private fun GreetingCard(
    greeting: String,
    completedTasks: Int,
    totalTasks: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = SpiderRed.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineSmall,
                color = SpiderRed,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (totalTasks > 0) {
                val progress = completedTasks.toFloat() / totalTasks.toFloat()
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = SpiderRed,
                    trackColor = SpiderRed.copy(alpha = 0.3f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "$completedTasks of $totalTasks tasks completed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            } else {
                Text(
                    text = "Ready to plan your day?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun QuoteCard(
    quote: String,
    author: String,
    source: String,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = SpiderBlue.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "🕷️ Daily Motivation",
                    style = MaterialTheme.typography.titleSmall,
                    color = SpiderBlue,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh quote",
                        tint = SpiderBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "\"$quote\"",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "— $author",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
            
            if (source.isNotEmpty()) {
                Text(
                    text = source,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun QuickActionsRow(
    onAddTask: () -> Unit,
    onSleep: () -> Unit,
    onBrainDump: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionButton(
            icon = Icons.Default.Add,
            label = "Add Task",
            color = SpiderRed,
            onClick = onAddTask,
            modifier = Modifier.weight(1f)
        )
        
        QuickActionButton(
            icon = Icons.Default.Bedtime,
            label = "Sleep",
            color = SpiderBlue,
            onClick = onSleep,
            modifier = Modifier.weight(1f)
        )
        
        QuickActionButton(
            icon = Icons.Default.Psychology,
            label = "Brain Dump",
            color = WebSilver,
            onClick = onBrainDump,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TaskCard(
    task: Task,
    onToggleComplete: () -> Unit,
    isOverdue: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isOverdue) {
                WarningColor.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleComplete() },
                colors = CheckboxDefaults.colors(
                    checkedColor = SpiderRed,
                    uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                
                if (task.description.isNotEmpty()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CategoryChip(category = task.category)
                    PriorityIndicator(priority = task.priority)
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    category: TaskCategory,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(android.graphics.Color.parseColor(category.color)).copy(alpha = 0.2f)
    ) {
        Text(
            text = category.displayName,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color(android.graphics.Color.parseColor(category.color))
        )
    }
}

@Composable
private fun PriorityIndicator(
    priority: com.wish.spideplan.data.model.TaskPriority,
    modifier: Modifier = Modifier
) {
    val color = when (priority) {
        com.wish.spideplan.data.model.TaskPriority.HIGH -> ErrorColor
        com.wish.spideplan.data.model.TaskPriority.MEDIUM -> WarningColor
        com.wish.spideplan.data.model.TaskPriority.LOW -> SuccessColor
    }
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, RoundedCornerShape(4.dp))
        )
        
        Spacer(modifier = Modifier.width(4.dp))
        
        Text(
            text = priority.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
private fun EmptyTasksCard(
    onAddTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.TaskAlt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No tasks for today",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Time to plan your day like a true Spider!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onAddTask,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SpiderRed
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add First Task")
            }
        }
    }
}