package com.example.project

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onSharedPreferenceChanged(sp: SharedPreferences, s: String) {
        when(s) {
            "theme" -> {
                val lol = sp.getString("theme", null)
                when (lol) {
                    "light" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    "dark" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
            }
            "name" -> {
                val name_string = findViewById<TextView>(R.id.tv_name)
                name_string.text = "${getString(R.string.hello)} ${sp.getString("name", null)}"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview = findViewById<RecyclerView>(R.id.recycler_view)

        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<CardViewModel>()

        val dbHelper = DatabaseHelper(this)

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

        val adapter = CardsAdapter(data)

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
        val intent = Intent(this, QuizActivity::class.java);
        startActivity(intent);
    }

    fun addCard(view: View?) {
        val question = findViewById<TextInputEditText>(R.id.question_input).text.toString();
        val answer = findViewById<TextInputEditText>(R.id.answer_input).text.toString();

        val dbHelper = DatabaseHelper(this);

        val newRowId = dbHelper.addCard(question, answer)

        val rv = findViewById<RecyclerView>(R.id.recycler_view)
        (rv.adapter as CardsAdapter).updateList(rv)

    }

    fun openSettings(view: View?) {
        val intent = Intent(this, SettingsActivity::class.java);
        startActivity(intent);
    }
}