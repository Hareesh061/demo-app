
package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has been drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

  
        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === END IF STATEMENT ===

        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()
        lemonImage!!.setOnClickListener {
            clickLemonImage()
            // TODO: call the method that handles the state when the image is clicked
        }
        lemonImage!!.setOnLongClickListener {
            showSnackbar()
            // TODO: replace 'false' with a call to the function that shows the squeeze count

        }
    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Clicking will elicit a different response depending on the state.
     * This method determines the state and proceeds with the correct action.
     */
    private fun clickLemonImage() {
        //based on current lemonadeState the data would be changed
        when (lemonadeState) {
            SELECT -> {
                //if the state is SELECT that means on tap we will try to squeeze the lemon so set the state as SQUEEZE
                lemonadeState = SQUEEZE
                //determining the size of tree by peek() function
                val tree: LemonTree = lemonTree
                lemonSize = tree.pick();
                //in starting there is no squeeze so count=0, 0, after the count will be increased by 1
                squeezeCount = 0
            }
            SQUEEZE -> {
                //now we are squeezing so count will be increased by 1 and size will be decreased by 1
                squeezeCount += 1
                lemonSize -= 1
                //now the lemonadeState will in Squeeze state until we do not complete the whole lemon(lemonSize=0)
                //so if the lemonSize is 0, then the state will be DRINK till then we have to Squeeze
                lemonadeState = if (lemonSize == 0) {
                    DRINK
                } else SQUEEZE
            }
            //if state is Drink then we will just drink the lemonade and state will be restart
            DRINK -> {
                lemonadeState = RESTART
                lemonSize = -1
            }
            //restart means now we will have to select the lemon form the tree
            RESTART -> lemonadeState = SELECT
        }
        //as per result of the above we will call the setViewElements() function and set the view accordingly
        setViewElements()
    }

    /**
     * Set up the view elements according to the state.
     */
    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)
        val lemonImage: ImageView = findViewById(R.id.image_lemon_state)

        //What ever the state is, we have to set properties of image and text as follow
        when (lemonadeState) {
            SELECT -> {
                textAction.text = getString(R.string.lemon_select)
                lemonImage.setImageResource(R.drawable.lemon_tree)
            }
            SQUEEZE -> {
                textAction.text = getString(R.string.lemon_squeeze)
                lemonImage.setImageResource(R.drawable.lemon_squeeze)
            }
            DRINK -> {
                textAction.text = getString(R.string.lemon_drink)
                lemonImage.setImageResource(R.drawable.lemon_drink)
            }
            RESTART -> {
                textAction.text = getString(R.string.lemon_empty_glass)
                lemonImage.setImageResource(R.drawable.lemon_restart)
            }
        }
    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

/**
 * A Lemon tree class with a method to "pick" a lemon. The "size" of the lemon is randomized
 * and determines how many times a lemon needs to be squeezed before you get lemonade.
 */
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
