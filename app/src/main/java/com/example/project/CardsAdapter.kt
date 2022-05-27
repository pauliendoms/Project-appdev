package com.example.project

import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout

class CardsAdapter(private var mList: List<CardViewModel>) : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val cardViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.questionView.editText?.setText(cardViewModel.question)

        // sets the text to the textview from our itemHolder class
        holder.answerView.editText?.setText(cardViewModel.answer)

        holder.deleteButton.setOnClickListener {
            Log.d("TRYING", "Here we are now")
            Log.d("TRYING", position.toString())

            val dbHelper = DatabaseHelper(holder.deleteButton.context)
            val rows = dbHelper.deleteCard(cardViewModel.question, cardViewModel.answer)
            Log.d("TRYING", "why")
            Log.d("TRYING", rows.toString())
            updateList(holder.itemView)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val questionView: TextInputLayout = itemView.findViewById(R.id.question_input)
        val answerView: TextInputLayout = itemView.findViewById(R.id.answer_input)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btn_del_card)
    }

    fun updateList(view: View) {
        val dbHelper = DatabaseHelper(view.context)

        val cursor = dbHelper.getCards()

        val data = ArrayList<CardViewModel>()

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
        this.mList = data;
        notifyDataSetChanged()

        // ik weet het ni meer
    }
}
