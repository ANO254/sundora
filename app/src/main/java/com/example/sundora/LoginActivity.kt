package com.example.sundora

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class LoginActivity : AppCompatActivity() {
    lateinit var gotoreg:TextView
    lateinit var login:Button
    lateinit var biometric:Button

    private var cancellationSignal: CancellationSignal? = null

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser("Authentication success!")
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        gotoreg = findViewById(R.id.txt_gotoreg)
        gotoreg.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        login = findViewById(R.id.btn_log)
        login.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Biometric Support
        checkBiometricSupport()
        biometric = findViewById(R.id.btn_biometrics)

        biometric.setOnClickListener {
            val biometricPrompt = BiometricPrompt.Builder(this)
                .setTitle("Log in to Sundora using Biometrics")
                .setSubtitle("")
                .setDescription("Scan your fingerprint")
                .setNegativeButton("cancel", this.mainExecutor,DialogInterface.OnClickListener { dialog, which ->
                } ).build()

            biometricPrompt.authenticate(getCancellationSignal(),mainExecutor, authenticationCallback)
        }

    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport() :Boolean {
        val keguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint authentication permission is not enabled")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }
}