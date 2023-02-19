package com.example.citytransport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroCustomLayoutFragment.Companion.newInstance
import com.github.appintro.AppIntroFragment

class MainActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setImmersiveMode()
        addSlide(newInstance(R.layout.activity_main))

        setImmersiveMode()
        addSlide(newInstance(R.layout.activity_main2))

        setImmersiveMode()
        addSlide(newInstance(R.layout.activity_main3))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        val b = Intent(this, Login:: class.java)
        startActivity(b)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        val a = Intent(this, Login:: class.java)
        startActivity(a)
        finish()
    }
}