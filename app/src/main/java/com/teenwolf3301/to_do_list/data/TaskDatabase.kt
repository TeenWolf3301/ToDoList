package com.teenwolf3301.to_do_list.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
@TypeConverters(Converter::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        private val appScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            appScope.launch {
                dao.insertTask(
                    Task(
                        name = "First",
                        category = "\uD83D\uDCBBFreelance",
                        priority = Priority.HIGH,
                        isCompleted = true
                    )
                )
                dao.insertTask(
                    Task(
                        name = "Second",
                        category = "\uD83D\uDCB0Finance",
                        priority = Priority.HIGH
                    )
                )
                dao.insertTask(
                    Task(
                        name = "Third",
                        category = "\uD83D\uDED2Shopping list",
                        priority = Priority.LOW
                    )
                )
                dao.insertTask(
                    Task(
                        name = "Fourth",
                        category = "\uD83D\uDCBBFreelance",
                        priority = Priority.LOW
                    )
                )
                dao.insertTask(
                    Task(
                        name = "Fifth",
                        category = "\uD83D\uDC8DWedding",
                        priority = Priority.MEDIUM
                    )
                )
                dao.insertTask(
                    Task(
                        name = "Sixth",
                        category = "\uD83D\uDCB0Finance",
                        priority = Priority.LOW,
                        isCompleted = true
                    )
                )
                dao.insertTask(
                    Task(
                        name = "Seventh",
                        category = "\uD83D\uDCB0Finance",
                        priority = Priority.LOW
                    )
                )
                dao.insertTask(
                    Task(
                        name = "Eighth",
                        category = "\uD83D\uDC8DWedding",
                        priority = Priority.MEDIUM
                    )
                )
                dao.insertTask(
                    Task(
                        name = "Ninth",
                        category = "\uD83D\uDCB0Finance",
                        priority = Priority.LOW
                    )
                )
                dao.insertTask(
                    Task(
                        name = "Tenth",
                        category = "\uD83D\uDC8DWedding",
                        priority = Priority.MEDIUM,
                        isCompleted = true
                    )
                )
            }
        }
    }
}