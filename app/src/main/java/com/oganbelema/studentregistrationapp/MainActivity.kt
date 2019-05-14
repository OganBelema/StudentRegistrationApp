package com.oganbelema.studentregistrationapp

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.oganbelema.studentregistrationapp.database.StudentRegistrationDatabase
import com.oganbelema.studentregistrationapp.database.entity.Student
import com.oganbelema.studentregistrationapp.databinding.ActivityMainBinding
import com.oganbelema.studentregistrationapp.databinding.AddStudentBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val studentRecyclerViewAdapter: StudentRecyclerViewAdapter by lazy {
        StudentRecyclerViewAdapter(this)
    }

    private val studentRegistrationDatabase: StudentRegistrationDatabase? by lazy {
        StudentRegistrationDatabase.getInstance(this)
    }

    private val databaseWorkerThread: DatabaseWorkerThread by lazy {
        DatabaseWorkerThread("DatabaseWorkerThread")
    }

    private val uiHandler = Handler()

    private lateinit var activityMainBinding: ActivityMainBinding

    private lateinit var addStudentBinding: AddStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.clickHandler = ClickHandler()

        databaseWorkerThread.start()

        activityMainBinding.studentRecyclerView.layoutManager = LinearLayoutManager(this)
        activityMainBinding.studentRecyclerView.adapter = studentRecyclerViewAdapter

        getStudents()

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false

            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                if (direction == ItemTouchHelper.LEFT){
                   val student = studentRecyclerViewAdapter.studentList[viewHolder.adapterPosition]
                    deleteStudent(student)
                }

            }

        })

        itemTouchHelper.attachToRecyclerView(studentRecyclerView)

    }

    private fun getStudents() {
        val task = Runnable {
            val students = studentRegistrationDatabase?.studentDao()?.getStudents()
            uiHandler.post {
                if (students != null){
                    studentRecyclerViewAdapter.addStudents(students)
                } else {
                    Toast.makeText(this, "Empty Record!!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        databaseWorkerThread.postTask(task)
    }

    private fun insertStudent(student: Student){
        val task = Runnable {
            val studentId = studentRegistrationDatabase?.studentDao()?.insertStudent(student)
            uiHandler.post{
                if (studentId != null){
                    Toast.makeText(this, "Student added successfully. Student id is $studentId",
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Student addition failed", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        databaseWorkerThread.postTask(task)
    }

    private fun deleteStudent(student: Student){
        val task = Runnable {
            studentRegistrationDatabase?.studentDao()?.deleteStudent(student)
        }

        databaseWorkerThread.postTask(task)
    }

    private fun displayAddStudentView() {

        addStudentBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.add_student, null,
            false)

        val student = Student()

        addStudentBinding.student = student

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setView(addStudentBinding.root)

        val studentNameEditTextView = addStudentBinding.nameEditTextView

        val studentEmailEditTextView = addStudentBinding.emailEditTextView

        val studentCountryEditTextView = addStudentBinding.countryEditTextView

        alertDialog.setPositiveButton("Add Student") { dialog, _ ->

            var isValid = true

            if (student.name.isEmpty()) {
                studentNameEditTextView.error = getString(R.string.required_field)
                studentNameEditTextView.requestFocus()
                isValid = false
            }

            if (student.email.isEmpty()){
                studentEmailEditTextView.error = getString(R.string.required_field)
                studentEmailEditTextView.requestFocus()
                isValid = false
            }

            if (student.country.isEmpty()){
                studentCountryEditTextView.error = getString(R.string.required_field)
                studentCountryEditTextView.requestFocus()
                isValid = false
            }

            if (isValid){
                val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy")
                val currentDate = simpleDateFormat.format(Date())
                student.registeredTime = currentDate

                insertStudent(student)
                dialog.dismiss()
            }
        }

        alertDialog.create().show()
    }

    override fun onDestroy() {
        StudentRegistrationDatabase.destroyInstance()
        databaseWorkerThread.quit()
        super.onDestroy()
    }

    inner class ClickHandler {

        fun onAddStudentButtonClicked(view: View){
            displayAddStudentView()
        }
    }
}
