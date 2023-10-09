package com.example.assignment_two

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class editDetailsActivity : AppCompatActivity() {

         private lateinit var editName: EditText
         private lateinit var editEmail: EditText

         private lateinit var editUsername: EditText
         private lateinit var editPassword: EditText
         private lateinit var editDate: EditText
         private lateinit var editPhone: EditText
         private lateinit var editRole: EditText
         private lateinit var editButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_details)

        editName = findViewById(R.id.editDetailsName)
        editEmail = findViewById(R.id.editDetailsEmail)
        editUsername = findViewById(R.id.editDetailsUsername)
        editPassword = findViewById(R.id.editDetailsPassword)
        editDate = findViewById(R.id.editDetailsDob)
        editPhone = findViewById(R.id.editDetailsPhone)
        editRole = findViewById(R.id.editDetailsRole)
        editButton = findViewById(R.id.editDetailsSave)
        var editMale = findViewById<CheckBox>(R.id.editDetailsMale)
        var editFemale = findViewById<CheckBox>(R.id.editDetailsFemale)



        val userList = loadUserDataList()
        val i = intent.getIntExtra("index",-1)

        for((index, userData) in userList.withIndex()){

//            "name" to name,
//            "email" to email,
//            "gender" to gender,
//            "username" to username,
//            "password" to password,
//            "dob" to dob,
//            "phone" to phone,
//            "role" to role

            if(i>=0 && index==i){

                editName.setText(userData["name"])
                editEmail.setText(userData["email"])
                editUsername.setText(userData["username"])
                editPassword.setText(userData["password"])
                editDate.setText(userData["dob"])
                editPhone.setText(userData["phone"])
                editRole.setText(userData["role"])

                if(userData["gender"]=="Male"){
                    editMale.isChecked = true
                }else{
                    editFemale.isChecked = true
                }
            }


        }

        editButton.setOnClickListener {

            val name = editName.text.toString()
            val email = editEmail.text.toString()

            val username = editUsername.text.toString()
            val password = editPassword.text.toString()
            val date = editDate.text.toString()
            val phone = editPhone.text.toString()
            val role = editRole.text.toString()

            val gender: String = if(editMale.isChecked){
                "Male"
            }else{
                "Female"
            }




            for((index,userData) in userList.withIndex()){

                if(i>=0 && index==i){



                      userData["name"] = name
                    userData["email"] =email
                    userData["gender"] = gender
                    userData["username"] = username
                    userData["password"] = password
                    userData["dob"] = date
                    userData["phone"] = phone
                    userData["role"] = role


                }

            }

            saveUserDataList(userList)

            showToast("Details updated successfully")

            val intent  = Intent(this,adminActivity::class.java)
            startActivity(intent)

        }
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
}