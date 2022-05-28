package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class QuizActivity : AppCompatActivity() {

    var index: Int = 0;
    var data: ArrayList<CardViewModel> = ArrayList();
    var status: String = "question";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val textView = findViewById<TextView>(R.id.card_content)

        val dbHelper = DatabaseHelper(this)

        val cursor = dbHelper.getCards()

        if (cursor.count > 0) {
            with(cursor) {
                cursor.moveToFirst()

                do {
                    val q: String = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUESTION))
                    val a: String = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_ANSWER))

                    data.add(CardViewModel(q, a))
                } while (moveToNext())
            }
        }

        cursor.close()

        textView.setText(data[0].question)
    }

    fun nextCard(view: View?) {
        if (index != (data.size -1)) {
            index += 1;
            val textView: TextView = findViewById(R.id.card_content)
            textView.setText(data[index].question)
            status = "question"
        } else {
            Toast.makeText(this, "Can't go further!", Toast.LENGTH_SHORT).show()
        }

    }

    fun previousCard(view: View?) {
        if (index != 0) {
            index -= 1
            val textView: TextView = findViewById(R.id.card_content)
            textView.setText(data[index].question)
            status = "question"
        } else {
            Toast.makeText(this, "Can't go back further!", Toast.LENGTH_SHORT).show()
        }
    }

    fun toggleCard(view: View?) {
        if (status == "question") {
            val textView: TextView = findViewById(R.id.card_content)
            textView.setText(data[index].answer)
            status = "answer"
        } else {
            val textView: TextView = findViewById(R.id.card_content)
            textView.setText(data[index].question)
            status = "question"
        }
    }

    fun stopQuiz(view: View?) {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }
}