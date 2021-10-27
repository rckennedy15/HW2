package edu.umb.cs443.hw2

import android.app.Activity
import android.widget.GridView
import android.os.Bundle
import android.view.View
import edu.umb.cs443.hw2.R
import android.widget.ArrayAdapter
import android.widget.Toast
import java.util.*

class MainActivity : Activity() {
    private lateinit var gridView: GridView
    private val r = Random()
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gridView = findViewById(R.id.gridView1)
        val adapter = ArrayAdapter(
            this,
            R.layout.list_item, tiles
        )
        gridView.adapter = adapter
        init()
        gridView.setOnItemClickListener { parent, v, position, id ->
            Toast.makeText(
                applicationContext,
                position.toString() as CharSequence,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun reset(view: View?) {
        init()
    }

    private fun init() {
        for (i in tiles.indices) tiles[i] = " "
        curx = r.nextInt(w)
        cury = r.nextInt(w)
        tiles[cury * w + curx] = "O"
        (gridView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }

    companion object {
        private const val w = 5
        private var curx = 0
        private var cury = 0
        var tiles = arrayOfNulls<String>(w * w)
    }
}