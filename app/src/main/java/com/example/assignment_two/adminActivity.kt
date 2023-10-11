package com.example.assignment_two

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class adminActivity : AppCompatActivity() {


    var i: Int = 0
    private lateinit var adminLogOut: Button
    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

       val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        adminLogOut = findViewById(R.id.adminLogOut)
       val userList  = loadUserDataList()

        adminLogOut.setOnClickListener {

            val sP = getSharedPreferences("LoginPreferences", MODE_PRIVATE)

            val editor = sP.edit()

            editor.remove("username").apply()
            editor.remove("role").apply()

            showToast("admin logout successfully")

            val intent = Intent(this,loginPageActivity::class.java)
            startActivity(intent)
        }


        for ((index, userData) in userList.withIndex()) {
            val name = userData["name"]
            val email = userData["email"]
            val username = userData["username"]
            val dob = userData["dob"]
            val gender = userData["gender"]
            val phone = userData["phone"]

              i++

            val tableRow = TableRow(this)


            val serialNumber = createTextView(i.toString())
            val nameTextView = createTextView(name)
            val emailTextView = createTextView(email)
            val genderTextView = createTextView(gender)
            val userTextView = createTextView(username)
            val dobTextView = createTextView(dob)
            val phoneTextView = createTextView(phone)
            val deleteButton = createDeleteButton(index,userList)
            val editButton = createEditButton(index,userList)


            tableRow.addView(serialNumber)
            tableRow.addView(nameTextView)
            tableRow.addView(emailTextView)
            tableRow.addView(genderTextView)
            tableRow.addView(userTextView)
            tableRow.addView(dobTextView)
            tableRow.addView(phoneTextView)
            tableRow.addView(editButton)
            tableRow.addView(deleteButton)


            tableLayout.addView(tableRow)
        }

    }

    private fun loadUserDataList(): MutableList<MutableMap<String, String>> {
        val preferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val gson = Gson()
        val dataJson = preferences.getString("user_data_list", null)
        val type = object : TypeToken<MutableList<MutableMap<String, String>>>() {}.type
        return gson.fromJson(dataJson, type) ?: mutableListOf()
    }

    private fun createTextView(text: String?): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.layoutParams = TableRow.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.setPadding(8, 8, 8, 8)
        textView.gravity = android.view.Gravity.CENTER
        return textView
    }

    private fun createEditButton(index:Int,userDataList: MutableList<MutableMap<String, String>>): Button {

        val editButton = Button(this)
        editButton.text = "Edit"
        editButton.layoutParams = TableRow.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        editButton.setPadding(8,8,8,8)

        editButton.setOnClickListener {

            if(index>=0 && index < userDataList.size){

                val intent  = Intent(this,editDetailsActivity::class.java)
                intent.putExtra("index",index)
                startActivity(intent)
            }
        }

        return editButton
    }

    private fun createDeleteButton(index: Int, userDataList: MutableList<MutableMap<String, String>>): Button {
        val deleteButton = Button(this)
        deleteButton.text = "Delete"
        deleteButton.layoutParams = TableRow.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        deleteButton.setPadding(8, 8, 8, 8)

        deleteButton.setOnClickListener {

            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Do you want to Delete?.")
            alertDialogBuilder.setPositiveButton("Yes") { dialogInterface: DialogInterface, _: Int ->

                i--
                if (index >= 0 && index < userDataList.size) {
                    userDataList.removeAt(index)

                    saveUserDataList(userDataList)

                    val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
                    tableLayout.removeViewAt(index + 1) //
                }
                dialogInterface.dismiss()
            }
            alertDialogBuilder.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->

                dialogInterface.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        return deleteButton
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






