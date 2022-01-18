package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils.readLines
import org.apache.commons.io.FileUtils.writeLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    val listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //remove item from list first
                listOfTasks.removeAt(position)
                //then notify adapter that something has changed
                adapter.notifyDataSetChanged()

                saveItem()
            }
        }

        loadItems()

        //look up RV in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskSpace)

        findViewById<Button>(R.id.button).setOnClickListener{
            // grab the text the user has inputted
            val userInputtedTask = inputTextField.text.toString()

            // add the string to list of tasks
             listOfTasks.add(userInputtedTask)

            //Add the string to list of tasks
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //reset text field
            inputTextField.setText("")

            saveItem()
        }
    }

    //get file we need
    fun getDataFile() : File {
        //each line represents a specific task in our list
        return File(filesDir, "data.txt")
    }

    //reads every line in the data file
    fun loadItems() {
        listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
    }

    //save items by writing them into data file
    fun saveItem() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}