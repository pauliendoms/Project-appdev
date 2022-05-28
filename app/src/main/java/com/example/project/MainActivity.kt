package com.example.project

import android.app.UiModeManager.MODE_NIGHT_YES
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    var theme: String = ""
    var name: String = ""

    override fun onSharedPreferenceChanged(sp: SharedPreferences, s: String) {
        when(s) {
            "theme" -> {
                Log.d("TRYING", s)
                val lol = sp.getString("theme", null)
                Log.d("TRYING", " lol $lol")
                when (lol) {
                    "light" -> {
                        Log.d("TRYING", "light")
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    "dark" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
            }
            "name" -> {
                Log.d("TRYING", s)
                val name_string = findViewById<TextView>(R.id.tv_name)
                name_string.text = "${getString(R.string.hello)} ${sp.getString("name", null)}"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recycler_view)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<CardViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view

        val dbHelper = DatabaseHelper(this)

// Define a projection that specifies which columns from the database
// you will actually use after this query.

        val cursor = dbHelper.getCards()

        val getal = cursor.count;

        Log.d("TRYING", getal.toString());

        if (getal > 0) {
            with(cursor) {
                moveToFirst()
                do {
                    val q = getColumnIndex(DatabaseHelper.COLUMN_QUESTION);
                    val a = getColumnIndex(DatabaseHelper.COLUMN_ANSWER);

                    val qu = getString(q);
                    val an = getString(a);
                    val listItem = CardViewModel(qu, an);
                    data.add(listItem);
                    Log.d("TRYING", "cursor")
                } while (moveToNext())
            }
        }


        cursor.close()
        Log.d("TRYING", "DATA: $data")
        // This will pass the ArrayList to our Adapter
        val adapter = CardsAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sp.registerOnSharedPreferenceChangeListener(this)

        val n = findViewById<TextView>(R.id.tv_name)
        n.text = "${getString(R.string.hello)} ${sp.getString("name", null)}"

        when (sp.getString("theme", null)) {
            "light" -> {
                Log.d("TRYING", "light")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            "dark" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    fun startQuiz(view: View?) {
        // Do something in response to button click
        val intent = Intent(this, MainActivityQuiz::class.java);
        startActivity(intent);
    }

    fun addCard(view: View?) {
        val question = findViewById<TextInputEditText>(R.id.question_input).text.toString();
        val answer = findViewById<TextInputEditText>(R.id.answer_input).text.toString();

        Log.d("TRYING", question);
        Log.d("TRYING", answer);

        val dbHelper = DatabaseHelper(this);

        val newRowId = dbHelper.addCard(question, answer)
        Log.d("TRYING", newRowId.toString());

        /*
        finish()
        startActivity(getIntent());

         */

        val rv = findViewById<RecyclerView>(R.id.recycler_view)
        (rv.adapter as CardsAdapter).updateList(rv)

    }

    fun openSettings(view: View?) {
        val intent = Intent(this, SettingsActivity::class.java);
        startActivity(intent);
    }
}