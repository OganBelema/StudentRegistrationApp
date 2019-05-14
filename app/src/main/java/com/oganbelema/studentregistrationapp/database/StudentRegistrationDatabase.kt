package com.oganbelema.studentregistrationapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oganbelema.studentregistrationapp.database.dao.StudentDAO
import com.oganbelema.studentregistrationapp.database.entity.Student

private const val DATABASE_NAME = "StudentDb"

@Database(entities = [Student::class], version = 2)
abstract class StudentRegistrationDatabase: RoomDatabase() {

    abstract fun studentDao(): StudentDAO

    companion object {
        private var INSTANCE: StudentRegistrationDatabase? = null

        fun getInstance(context: Context): StudentRegistrationDatabase? {
            if (INSTANCE == null){
                synchronized(StudentRegistrationDatabase::class){
                    INSTANCE = Room.databaseBuilder(context, StudentRegistrationDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE;
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}