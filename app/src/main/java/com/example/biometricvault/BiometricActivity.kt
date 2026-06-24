package com.example.biometricvault

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class BiometricActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Must be FIRST — before super.onCreate and before theme apply
        installSplashScreen()

        // Apply dark/light mode so splash respects user preference
        ThemePreferenceManager.applyMode(this)

        super.onCreate(savedInstanceState)
        // No setContentView needed — biometric prompt is a system dialog

        checkAndPromptBiometric()
    }

    private fun checkAndPromptBiometric() {
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> showBiometricPrompt()

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "No biometrics enrolled. Please set up in Settings.", Toast.LENGTH_LONG).show()
                finish()
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                // Skip auth on devices without hardware
                goToMain()
            }

            else -> {
                Toast.makeText(this, "Biometric authentication unavailable.", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                goToMain()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                // System handles retry UI automatically — just show a hint
                Toast.makeText(
                    this@BiometricActivity,
                    "Authentication failed. Try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                when (errorCode) {
                    BiometricPrompt.ERROR_USER_CANCELED,
                    BiometricPrompt.ERROR_NEGATIVE_BUTTON -> finish()
                    else -> {
                        Toast.makeText(
                            this@BiometricActivity,
                            "Error: $errString",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
            }
        }

        val prompt = BiometricPrompt(this, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Veri Biometric Vault")
            .setSubtitle("Verify your identity to continue")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()

        prompt.authenticate(promptInfo)
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}