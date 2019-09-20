package com.scurab.android.samples

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scurab.android.features.ui.themes.app.ThemesSampleActivity

class AppActivity : AppCompatActivity() {

    private val items = listOf(
        Screen("Themes", ThemesSampleActivity::class.java)
    ).sortedBy { it.name }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerView = RecyclerView(this)
        setContentView(recyclerView)
        recyclerView.adapter = ScreenAdapter(items) {
            onOpenScreen(it.intent(this))
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun onOpenScreen(intent: Intent) {
        startActivity(intent)
    }
}

private class Screen(val name: String, val intent: (Activity) -> Intent) {
    constructor(name: String, clazz: Class<out Activity>) :
            this(name, { activity -> Intent(activity, clazz) })
}


private class ScreenAdapter(
    private val items: List<Screen>,
    clickListener: (Screen) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemClickListener = View.OnClickListener { v ->
        (v.tag as? Screen)?.let { clickListener(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(Button(parent.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setOnClickListener(itemClickListener)
        }) {}
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val textView = holder.itemView as TextView
        val screen = items[position]
        textView.tag = screen
        textView.text = screen.name
    }
}