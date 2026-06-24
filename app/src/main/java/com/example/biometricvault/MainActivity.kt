package com.example.biometricvault

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AppCompatDelegate
import com.example.biometricvault.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemePreferenceManager.applyMode(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fab = binding.fabTheme
        updateFabIcon(fab)

        fab.setOnClickListener {
            val newMode = if (isNightMode()) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }

            ThemePreferenceManager.saveMode(this, newMode)
            AppCompatDelegate.setDefaultNightMode(newMode)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun isNightMode(): Boolean {
        val currentMode = resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return currentMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    private fun updateFabIcon(fab: com.google.android.material.floatingactionbutton.FloatingActionButton) {
        val iconRes = if (isNightMode()) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
        fab.setImageResource(iconRes)
    }
}