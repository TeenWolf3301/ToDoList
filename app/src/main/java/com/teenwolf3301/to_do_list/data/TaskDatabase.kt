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
                dao.insertTask(Task(name = "First"))
                dao.insertTask(Task(name = "Second", isCompleted = true))
                dao.insertTask(Task(name = "Third"))
                dao.insertTask(Task(name = "Fourth"))
                dao.insertTask(Task(name = "Fifth"))
                dao.insertTask(Task(name = "Sixth"))
                dao.insertTask(Task(name = "Seventh"))
                dao.insertTask(Task(name = "Eighth"))
                dao.insertTask(Task(name = "Ninth"))
                dao.insertTask(Task(name = "Tenth"))
            }
        }
    }
}