package edu.umb.cs443.hw2

import android.app.Activity
import android.widget.GridView
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import java.util.*
import kotlinx.coroutines.*
import kotlin.math.floor

private const val TAG = "MainActivity"

class MainActivity : Activity() {
    private lateinit var gridView: GridView
    private val r = Random()
    private var lastPosition = 0;
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
            // toast the position
            Toast.makeText(
                applicationContext,
                position.toString() as CharSequence,
                Toast.LENGTH_SHORT
            ).show()
            // move the player (horizontal first, then vertical)
            GlobalScope.launch {
                moveUser(position)
            }
        }

        GlobalScope.launch {
            treasureSpawn()
        }
    }

    // an asynchronous function that spawns the treasure
    // it is a Kotlin coroutine, similar to a Java Thread
    private suspend fun treasureSpawn() {
        while (true) {
            delay(3000L)
            var treasureX: Int
            var treasureY: Int
            // this loop prevents placing a treasure on top of an already existing one or on top
            // of the user
            do {
                treasureX = r.nextInt(w)
                treasureY = r.nextInt(w)
            } while (tiles[treasureY * w + treasureX] == "X" || tiles[treasureY * w + treasureX] == "O")
            tiles[treasureY * w + treasureX] = "X"
            this.runOnUiThread {
                (gridView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
            }

        }
    }

    // keep track of textCell and textTreasures
    private suspend fun moveUser(position: Int) {
        val xIndexOld = lastPosition % 5
        val xIndexNew = position % 5
        val yIndexOld = floor((lastPosition / 5).toDouble()).toInt()
        val yIndexNew = floor((position / 5).toDouble()).toInt()

        suspend fun moveY() {
            when {
                yIndexOld < yIndexNew -> {
                    for (y in yIndexOld until yIndexNew) {
                        delay(1000L)
                        tiles[lastPosition] = " "
                        lastPosition += 5
                        tiles[lastPosition] = "O"
                        this.runOnUiThread {
                            (gridView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                        }
                    }
                }
                yIndexOld > yIndexNew -> {
                    for (y in yIndexNew until yIndexOld) {
                        delay(1000L)
                        tiles[lastPosition] = " "
                        lastPosition -= 5
                        tiles[lastPosition] = "O"
                        this.runOnUiThread {
                            (gridView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        when {
            xIndexOld < xIndexNew -> {
                for (x in xIndexOld until xIndexNew) {
                    delay(1000L)
                    tiles[lastPosition] = " "
                    tiles[++lastPosition] = "O"
                    this.runOnUiThread {
                        (gridView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                    }
                }
                moveY()
            }
            xIndexOld > xIndexNew -> {
                for (x in xIndexNew until xIndexOld) {
                    delay(1000L)
                    tiles[lastPosition] = " "
                    tiles[--lastPosition] = "O"
                    this.runOnUiThread {
                        (gridView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                    }
                }
                moveY()
            }
            else -> {
                moveY()
            }
        }
    }

    private suspend fun updateCell() {
        // TODO
    }

    private suspend fun updateScore() {
        // TODO
    }

    fun reset(view: View?) {
        init()
    }

    private fun init() {
        // erases all tiles
        for (i in tiles.indices) tiles[i] = " "
        // sets random tile as O
        curx = r.nextInt(w)
        cury = r.nextInt(w)
        tiles[cury * w + curx] = "O"
        (gridView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        lastPosition = cury * w + curx
    }

    companion object {
        private const val w = 5
        private var curx = 0
        private var cury = 0
        var tiles = arrayOfNulls<String>(w * w)
    }
}