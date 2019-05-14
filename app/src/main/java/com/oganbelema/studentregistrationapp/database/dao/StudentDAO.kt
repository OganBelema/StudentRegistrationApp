package com.oganbelema.studentregistrationapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.oganbelema.studentregistrationapp.database.entity.Student

@Dao
interface StudentDAO {

    @Insert
    fun insertStudent(student: Student): Long

    @Delete
    fun deleteStudent(student: Student)

    @Query("select * from students")
    fun getStudents(): List<Student>
}