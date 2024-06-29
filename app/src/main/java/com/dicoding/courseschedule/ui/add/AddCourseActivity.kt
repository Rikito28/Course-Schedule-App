package com.dicoding.courseschedule.ui.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.util.TimePickerFragment

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var addCourseViewModel : AddCourseViewModel
    private lateinit var startDay: String
    private lateinit var endDay: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.apply {
            title = getString(R.string.add_course)
            setDisplayHomeAsUpEnabled(true)

        }

            val viewModelFactory = AddCourseViewModelFactory.createFactory(this)
            addCourseViewModel = ViewModelProvider(this, viewModelFactory)[AddCourseViewModel::class.java]
            showTime()
            addCourseViewModel.saved

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val courseName = findViewById<TextView>(R.id.ed_course_name)
        val spinner = findViewById<Spinner>(R.id.spinner_day)
        val lecture = findViewById<TextView>(R.id.ed_lecture)
        val note = findViewById<TextView>(R.id.ed_note)

        return if (item.itemId == R.id.action_insert) {
            val courseNameInput = courseName.text.toString()
            val spinnerDayInput = spinner.selectedItemPosition
            val lectureInput = lecture.text.toString()
            val noteInput = note.text.toString()

            addCourseViewModel.insertCourse(courseNameInput, spinnerDayInput, startDay, endDay , lectureInput, noteInput)
            Toast.makeText(this, "Berhasil Membuat Course", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            true

        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val time = "${hour}:${minute}"
        val startTime = findViewById<TextView>(R.id.start_time)
        val endTime = findViewById<TextView>(R.id.end_time)
        if (tag == STARTDAY) {
            startTime.text = time
            startDay = time
        } else {
            endTime.text = time
            endDay = time
        }
    }

    private fun showTime() {
        val timeFragment = TimePickerFragment()
        val ibStartTime = findViewById<ImageButton>(R.id.ib_start_time)
        val ibEndTime = findViewById<ImageButton>(R.id.ib_end_time)

        ibStartTime.setOnClickListener {
            timeFragment.show(supportFragmentManager, STARTDAY)
        }
        ibEndTime.setOnClickListener {
            timeFragment.show(supportFragmentManager, ENDDAY)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val STARTDAY = "startDay"
        const val ENDDAY = "EndDay"
    }
}