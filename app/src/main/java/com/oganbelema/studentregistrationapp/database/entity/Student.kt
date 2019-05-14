package com.oganbelema.studentregistrationapp.database.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.oganbelema.studentregistrationapp.BR

@Entity(tableName = "students")
class Student @Ignore constructor(): BaseObservable() {

    constructor(id: Int, name: String, email: String, country: String, registeredTime: String) : this() {
        this.id = id
        this.name = name
        this.email = email
        this.country = country
        this.registeredTime = registeredTime
    }

    @PrimaryKey(autoGenerate = true)
    @Bindable
    var id: Int = 0
    set(value) {
        field = value
        notifyPropertyChanged(BR.id)
    }

    @Bindable
    var name: String = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.name)
    }

    @Bindable
    var email: String = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.email)
    }

    @Bindable
    var country: String =""
    set(value) {
        field = value
        notifyPropertyChanged(BR.country)
    }

    @ColumnInfo(name = "registered_time")
    @Bindable
    var registeredTime: String = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.registeredTime)
    }
}