package org.dclm.live.ui.util.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.dclm.live.ui.message.Blog
import org.dclm.live.ui.message.BlogDao
import org.dclm.live.ui.sermon.Note
import org.dclm.live.ui.sermon.NoteDao

@Database(entities = [Note::class], version = 6, exportSchema = false)
abstract class DCLMDataBase : RoomDatabase() {

    abstract fun noteDAO(): NoteDao
  // abstract fun blogDAO(): BlogDao

    companion object {
        var INSTANCE: DCLMDataBase? = null

        fun getAppDataBase(context: Context): DCLMDataBase? {
            if (INSTANCE == null){
                synchronized(DCLMDataBase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, DCLMDataBase::class.java, "note_database")
                        .addMigrations(
                            MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                         //.fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }

        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("ALTER TABLE note_table  ADD COLUMN point4 TEXT")
                }
            }

        private val MIGRATION_4_5: Migration = object : Migration(4, 5) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("ALTER TABLE note_table ADD COLUMN point4_description TEXT")
                }
            }

        private val MIGRATION_5_6: Migration = object : Migration(5, 6) {
                override fun migrate(database: SupportSQLiteDatabase) {
                  // database.execSQL("CREATE TABLE IF NOT EXISTS `blog` (`id` INTEGER  , `title` TEXT NOT NULL,  `date` TEXT NOT NULL,`body` TEXT NOT NULL, PRIMARY KEY(`id`))")
                    //database.execSQL("CREATE TABLE IF NOT EXISTS `blog` (`id` INTEGER NOT NULL, `title` TEXT,  `date` TEXT,`body` TEXT, PRIMARY KEY(`id`))")
                    //database.execSQL("CREATE TABLE IF NOT EXISTS `blog` (`id` INTEGER NOT NULL, `title` TEXT,  `date` TEXT,`body` TEXT, PRIMARY KEY(`id`))")

                    database.execSQL("CREATE TABLE IF NOT EXISTS note_table_Tmp (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, service TEXT, date TEXT , speaker TEXT, topic TEXT, description TEXT, point1 TEXT, point1_description TEXT, point2 TEXT, point2_description TEXT, point3 TEXT, point3_description TEXT, point4 TEXT, point4_description TEXT, decision TEXT)")
                    database.execSQL("INSERT INTO note_table_tmp(id, service, date , speaker, topic, description, point1, point1_description , point2 , point2_description , point3 , point3_description , point4 , point4_description , decision) SELECT id, service, date , speaker, topic, description, point1, point1_description , point2 , point2_description , point3 , point3_description , point4 , point4_description , decision FROM note_table")
                    database.execSQL("DROP TABLE note_table")
                    database.execSQL("ALTER TABLE note_table_Tmp RENAME TO note_table")
                }
            }
    }


}