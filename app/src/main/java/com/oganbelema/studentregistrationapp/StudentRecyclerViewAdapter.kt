package com.oganbelema.studentregistrationapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.oganbelema.studentregistrationapp.database.entity.Student
import com.oganbelema.studentregistrationapp.databinding.StudentListItemBinding


class StudentRecyclerViewAdapter(private val context: Context):
    RecyclerView.Adapter<StudentRecyclerViewAdapter.StudentItemViewHolder>() {

    private var _studentList: ArrayList<Student> = ArrayList()

    val studentList: List<Student>
        get() = _studentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentItemViewHolder {
        return StudentItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.student_list_item,
            parent, false))
    }

    override fun getItemCount(): Int {
        return _studentList.size
    }

    override fun onBindViewHolder(holder: StudentItemViewHolder, position: Int) {
        holder.bindData(_studentList[position])
    }

    fun addStudents(students: List<Student>){
        _studentList.addAll(students)
        notifyDataSetChanged()
    }


    class StudentItemViewHolder(private val studentListItemBinding: StudentListItemBinding):
        RecyclerView.ViewHolder(studentListItemBinding.root) {

        fun bindData(student: Student){
            studentListItemBinding.student = student
        }
    }
}