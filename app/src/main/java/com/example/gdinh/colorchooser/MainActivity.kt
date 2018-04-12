package com.example.gdinh.colorchooser

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*


class MainActivity : AppCompatActivity() {

    var red : Int = 0
    var green : Int = 0
    var blue: Int = 0
    private val fileName = "Colors"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setIcon(getDrawable(R.drawable.ic_invert_colors_black_24dp))

        //Getting values of button, seekBar and textViews
        val button: Button = findViewById(R.id.button)

        val sb_red: SeekBar = findViewById(R.id.sb_red)
        val sb_green: SeekBar = findViewById(R.id.sb_green)
        val sb_blue: SeekBar = findViewById(R.id.sb_blue)

        // Sets the max for colors
        sb_red.max = 255
        sb_green.max = 255
        sb_blue.max = 255

        // Method to change the red colors when seekbar is changed
        sb_red.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(Seekbar: SeekBar?, progress: Int, p2: Boolean) {
                red = progress
                tv_red.text = progress.toString()
                button.setBackgroundColor(Color.rgb(red, green, blue))
                hex.setText("#" + Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue))
            }
            override fun onStartTrackingTouch(SeekBar: SeekBar?) {}
            override fun onStopTrackingTouch(SeekBar: SeekBar?) {}
        })

        // Method that changed the green color when seekbar is changed
        sb_green.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(SeekBar: SeekBar?, progress: Int, p2: Boolean) {
                green = progress
                tv_green.text = progress.toString()
                hex.setText("#" + Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue))
                button.setBackgroundColor(Color.rgb(red, green, blue))
            }
            override fun onStartTrackingTouch(SeekBar: SeekBar?) {}
            override fun onStopTrackingTouch(SeekBar: SeekBar?) {}
        })

        // Method that changes the blue color when seekbar is changed
        sb_blue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(SeekBar: SeekBar?, progress: Int, p2: Boolean) {
                blue = progress
                tv_blue.text = progress.toString()
                hex.setText("#" + Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue))
                button.setBackgroundColor(Color.rgb(red, green, blue))
            }
            override fun onStartTrackingTouch(SeekBar: SeekBar?) {}
            override fun onStopTrackingTouch(SeekBar: SeekBar?) {}
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
        //When saved button in action bar is clicked
            R.id.action_save -> {
                showSavedDialog()
                return true
            }

        //When recall button in action bar is clicked
            R.id.action_recall -> {
                showRecallSpinner()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    //Method to show save dialog when Saved button in action bar is clicked
    private fun showSavedDialog(){
        //Creates the dialog popup
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.save_dialog, null) as View
        dialogBuilder.setView(dialogView)

        // Creates the edit text in dialog view for users to save color name
        val colorName = dialogView.findViewById<EditText>(R.id.save_text) as EditText

        //Actions for save dialog box
        dialogBuilder.setCancelable(false)
        dialogBuilder.setTitle("Save")
        dialogBuilder.setMessage("Enter Name of Color")

        //Creates the save button in the dialog box
        dialogBuilder.setPositiveButton("Save"){dialog, which ->
            // Forces user to enter name of the color
            if(colorName.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Must Enter Color Name", Toast.LENGTH_SHORT).show()
            }
            // Calls method to save color data
            saveFile(colorName.text.toString())

        }

        //Creates the cancel button in the dialog box
        dialogBuilder.setNegativeButton("Cancel"){ dialog, which ->
            dialog.dismiss()
        }

        dialogBuilder.create()
        dialogBuilder.show()
    }

    // Method to save data into file
    private fun saveFile(colorName: String){
        //Creates a new file to save data into
        val file = File(filesDir,"Colors")

        //Searches to see if file exists and saves color data into the file
        if(file.exists()){
            file.appendText("\n$colorName, $red, $green, $blue")
            Toast.makeText(this, "$colorName Saved", Toast.LENGTH_SHORT).show()
        }
        else {
            val out = file.printWriter()
            out.println("$colorName, $red, $green, $blue")
            out.close()
            Toast.makeText(this, "$colorName Saved", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to get a list of colors saved
    private fun getSavedColors() : ArrayList<String> {
        // Looks for file name
        val file = File(filesDir, fileName)
        val sc = Scanner(file)
        var colorsList = ArrayList<String>()
        try{
            var line: String
            while(sc.hasNext()){
                line = sc.nextLine()
                if(line != null){
                    var colorName = line.split(",")
                    colorsList.add(colorName[0])
                }
            }
        }
        catch(e: NullPointerException){
            Toast.makeText(this, "No Colors Saved", Toast.LENGTH_SHORT).show()
        }
        return colorsList
    }

    // Method to recall colors when clicked on the one wanted
    private fun recallColor(colorName: String){

        //Finds the file that wants to be recalled
        val file = File(filesDir, "Colors")
        val sc = Scanner(file)
        var line: String
        var fileColor: List<String>
        var r = 0
        var g = 0
        var b = 0
        while (sc.hasNext()){
            line = sc.nextLine()
            fileColor = line.split(",")
            if(colorName.equals(fileColor[0])){
                //Recalls red text view and seekbar
                r = fileColor[1].trim().toInt()
                tv_red.text = fileColor[1].trim()
                sb_red.progress = r

                //Recalls green text view and seekbar
                g = fileColor[2].trim().toInt()
                tv_green.text = fileColor[2].trim()
                sb_green.progress = g

                //Recalls blue text view and seekbar
                b = fileColor[3].trim().toInt()
                tv_blue.text = fileColor[3].trim()
                sb_blue.progress = b
            }
        }
        setButtonColor(r,g,b)
    }

    //Method to create a spinner in order to show all colors saved and able to recall them
    private fun showRecallSpinner() {
        val colorsList = getSavedColors()

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.recall_items, null) as View
        builder.setTitle("Recall a Color")
        var spinner = dialogView.findViewById<Spinner>(R.id.recall_items)
        val colorAdapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,
                colorsList)
        colorAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = colorAdapter

        builder.setPositiveButton("Recall") {dialog, which ->
            val toast = Toast.makeText(this, spinner.selectedItem.toString() + " loaded successfully",
                    Toast.LENGTH_LONG)
            toast.show()
            dialog.dismiss()
            recallColor(spinner.selectedItem.toString())
        }
        builder.setNegativeButton("Cancel") {dialog, which ->
            dialog.dismiss()
        }
        builder.setView(dialogView)
        builder.create()
        builder.show()
    }

    //Sets button background color to appropriate color
    private fun setButtonColor(r: Int, g: Int, b: Int ){
        button.setBackgroundColor(Color.rgb(r,g,b))
    }

}
