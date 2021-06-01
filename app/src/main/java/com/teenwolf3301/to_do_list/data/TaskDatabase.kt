package com.teenwolf3301.to_do_list.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
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
                dao.insertTask(Task(name = "First", category = "Freelance"))
                dao.insertTask(Task(name = "Second", isCompleted = true, category = "Wedding"))
                dao.insertTask(Task(name = "Third", priority = Priority.MEDIUM))
                dao.insertTask(Task(name = "Fourth", priority = Priority.MEDIUM))
                dao.insertTask(Task(name = "Fifth", priority = Priority.HIGH, category = "Wedding"))
                dao.insertTask(Task(name = "Sixth", isCompleted = true))
                dao.insertTask(Task(name = "Seventh", isCompleted = true, priority = Priority.HIGH, category = "Shopping list"))
                dao.insertTask(Task(name = "Eighth"))
                dao.insertTask(Task(name = "Ninth"))
                dao.insertTask(Task(name = "Tenth", category = "Shopping list"))
            }
        }
    }
}