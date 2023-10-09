package com.example.assignment_two

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class loginPageActivity : AppCompatActivity() {

    private lateinit var registerBtn: Button
    private lateinit var editUser: EditText
    private lateinit var editPass: EditText
    private lateinit var loginBtn: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        registerBtn = findViewById(R.id.loginRegister)
        editUser = findViewById(R.id.loginUser)
        editPass = findViewById(R.id.loginPass)
        loginBtn = findViewById(R.id.submitLogin)



        registerBtn.setOnClickListener {
            val intent  = Intent(this,registerActivity::class.java)

            startActivity(intent)
        }

        loginBtn.setOnClickListener {

            val username = editUser.text.toString()
            val password = editPass.text.toString()

            val userDataList = loadUserDataList()


            val isLoginSuccessful = checkLoginCredentials(userDataList, username, password)


            val sharedPreferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE)

            val role = sharedPreferences.getString("role","")
            val user = sharedPreferences.getString("username","")
            if (isLoginSuccessful) {

                showToast("Login Successful")

                showToast("Welcome $user")

                if(role=="Admin"){


                    val intent = Intent(this,adminActivity::class.java)
                    startActivity(intent)


                }else{


                    val intent = Intent(this,userActivity::class.java)
                    startActivity(intent)



                }




            } else {

                showToast("Incorrect Username or Password")
            }
        }





    }

    private fun checkLoginCredentials(
        userDataList: List<Map<String, String>>,
        username: String,
        password: String
    ): Boolean {

        for (userData in userDataList) {
            val storedUsername = userData["username"]
            val storedPassword = userData["password"]
            val role = userData["role"]

            if (storedUsername == username && storedPassword == password) {

                val sharedPreferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE)
                val edit = sharedPreferences.edit()
                edit.putString("role",role)
                edit.putString("username",storedUsername)
                edit.apply()

                return true
            }
        }
        return false
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun loadUserDataList(): MutableList<MutableMap<String, String>> {
        val preferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val gson = Gson()
        val dataJson = preferences.getString("user_data_list", null)
        val type = object : TypeToken<MutableList<MutableMap<String, String>>>() {}.type
        return gson.fromJson(dataJson, type) ?: mutableListOf()
    }





}