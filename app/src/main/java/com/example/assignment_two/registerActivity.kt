package com.example.assignment_two

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class registerActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editMale: CheckBox
    private lateinit var editFemale: CheckBox
    private lateinit var editUser: EditText
    private lateinit var editPass: EditText
    private lateinit var editDob: EditText
    private lateinit var editPhone: EditText
    private lateinit var editRole: Switch
    private lateinit var registerButton: Button
    private lateinit var backbtn: Button


    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)




        editName = findViewById(R.id.registerName)
        editEmail = findViewById(R.id.registerEmail)
        editMale = findViewById(R.id.checkMale)
        editFemale = findViewById(R.id.checkFemale)
        editUser = findViewById(R.id.registerUser)
        editPass = findViewById(R.id.registerPass)
        editDob = findViewById(R.id.registerDate)
        editPhone = findViewById(R.id.registerPhone)
        editRole = findViewById(R.id.adminRole)
        registerButton = findViewById(R.id.registerUp)
        backbtn = findViewById(R.id.backButton)









        backbtn.setOnClickListener {

            val intent  = Intent(this,loginPageActivity::class.java)
            startActivity(intent)
        }



        editDob.setOnClickListener{
            showDatePickerDialog()
        }

        registerButton.setOnClickListener {


//            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//            val emailRegex = Regex(emailPattern)
//
//            if(!emailRegex.matches(email)){
//
//                showToast("Invalid Email Format")
//                editEmail.text.clear()
//
//            }
//
//            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
//
//            val passwordRegex = Regex(passwordPattern)
//
//            if(password.length<8 || !passwordRegex.matches(password)){
//
//                showToast("Enter Valid Password")
//            }
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val gender: String=if(editMale.isChecked && !editFemale.isChecked){
                "Male"
            }else{
                "Female"
            }
            val username = editUser.text.toString()
            val password = editPass.text.toString()
            val dob = editDob.text.toString()
            val phone = editPhone.text.toString()
            val role: String = if(editRole.isChecked){
                "Admin"
            }else{
                "User"
            }

            val userData = mutableMapOf(

                "name" to name,
                "email" to email,
                "gender" to gender,
                "username" to username,
                "password" to password,
                "dob" to dob,
                "phone" to phone,
                "role" to role

            )

            val userDataList = loadUserDataList()

            userDataList.add(userData)

            saveUserDataList(userDataList)

            showToast("Successfully Registered")

            resetForm()

            val intent = Intent(this,loginPageActivity::class.java)

            startActivity(intent)


        }


    }

    private fun resetForm() {
        editName.text.clear()
        editEmail.text.clear()
        editUser.text.clear()
        editPass.text.clear()
        editDob.text.clear()
        editPhone.text.clear()

    }


    private fun loadUserDataList(): MutableList<MutableMap<String, String>> {
        val preferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val gson = Gson()
        val dataJson = preferences.getString("user_data_list", null)
        val type = object : TypeToken<MutableList<MutableMap<String, String>>>() {}.type
        return gson.fromJson(dataJson, type) ?: mutableListOf()
    }

    private fun saveUserDataList(userDataList: MutableList<MutableMap<String, String>>) {
        val preferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val editor = preferences.edit()
        val gson = Gson()
        val dataJson = gson.toJson(userDataList)
        editor.putString("user_data_list", dataJson)
        editor.apply()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun showDatePickerDialog() {


        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.YEAR, -18)
        val minDate = Calendar.getInstance()
        minDate.add(Calendar.YEAR, -100)


        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            editDob.setText(dateFormat.format(calendar.time))
        }

        val datePickerDialog = DatePickerDialog(
            this@registerActivity,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = minDate.timeInMillis
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        datePickerDialog.show()
    }
}