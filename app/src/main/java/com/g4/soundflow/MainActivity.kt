package com.g4.soundflow

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/** Contiene los dos Fragments visibles simultáneamente. */
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.navigation_container, NavigationFragment())
                .replace(R.id.content_container, ContentFragment())
                .commitNow()
        }
    }
}
