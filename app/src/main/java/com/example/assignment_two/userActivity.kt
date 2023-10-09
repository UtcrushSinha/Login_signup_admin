package com.example.assignment_two

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class userActivity : AppCompatActivity() {

    private lateinit var editName: TextView
    private lateinit var editEmail: TextView
    private lateinit var editGender: TextView
    private lateinit var editUser: TextView
    private lateinit var editDob: TextView
    private lateinit var editPhone: TextView
    private lateinit var logOutBtn: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        editName = findViewById(R.id.name)
        editEmail = findViewById(R.id.email)
        editGender = findViewById(R.id.gender)
        editUser = findViewById(R.id.username)
        editDob = findViewById(R.id.dob)
        editPhone = findViewById(R.id.phone)
        logOutBtn = findViewById(R.id.logOut)





        val sharedPreferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE)
        val user = sharedPreferences.getString("username","")

        val userList = loadUserDataList()

        for(userData in userList){

            val username = userData["username"]
            val name = userData["name"]
            val email = userData["email"]
            val gender = userData["gender"]
            val dob  = userData["dob"]
            val phone = userData["phone"]

            if(username == user){

                editName.text = name
                editEmail.text = email
                editGender.text = gender
                editUser.text = username
                editDob.text = dob
                editPhone.text = phone

            }
        }


        logOutBtn.setOnClickListener {

            val sP = getSharedPreferences("LoginPreferences", MODE_PRIVATE)

            val editor = sP.edit()

            editor.remove("username").apply()
            editor.remove("role").apply()

            showToast("$user logout successfully")

            val intent = Intent(this,loginPageActivity::class.java)
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}