package com.example.dnstoggle

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var btnEnableDns: Button
    private lateinit var btnDisableDns: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inizializza le viste
        statusTextView = findViewById(R.id.statusTextView)
        btnEnableDns = findViewById(R.id.btnEnableDns)
        btnDisableDns = findViewById(R.id.btnDisableDns)
        val btnSettings = findViewById<Button>(R.id.btnSettings)

        // Imposta i listener per i pulsanti
        btnEnableDns.setOnClickListener {
            if (hasWriteSecureSettingsPermission()) {
                enablePrivateDns()
            } else {
                showPermissionError()
            }
        }

        btnDisableDns.setOnClickListener {
            if (hasWriteSecureSettingsPermission()) {
                disablePrivateDns()
            } else {
                showPermissionError()
            }
        }

        btnSettings.setOnClickListener {
            openDnsSettings()
        }

        // Controlla lo stato iniziale del DNS privato
        updateDnsStatus()
    }

    override fun onResume() {
        super.onResume()
        // Aggiorna lo stato quando l'app torna in primo piano
        updateDnsStatus()
    }

    // Controlla se l'app ha il permesso WRITE_SECURE_SETTINGS
    private fun hasWriteSecureSettingsPermission(): Boolean {
        return try {
            Settings.System.canWrite(this)
        } catch (e: Exception) {
            false
        }
    }

    // Abilita il DNS privato
    private fun enablePrivateDns() {
        try {
            // Recupera il valore del DNS privato precedentemente configurato
            // Se non esiste, usa un valore predefinito (ad esempio Cloudflare)
            val currentHost = Settings.Global.getString(contentResolver, "private_dns_specifier")
            val hostToUse = if (currentHost.isNullOrEmpty()) "dns.google" else currentHost

            // Imposta la modalità del DNS privato su "hostname" (modalità automatica)
            Settings.Global.putString(contentResolver, "private_dns_mode", "hostname")
            Settings.Global.putString(contentResolver, "private_dns_specifier", hostToUse)
            
            Toast.makeText(this, R.string.operation_success, Toast.LENGTH_SHORT).show()
            updateDnsStatus()
        } catch (e: Exception) {
            Toast.makeText(this, R.string.operation_failed, Toast.LENGTH_SHORT).show()
        }
    }

    // Disabilita il DNS privato
    private fun disablePrivateDns() {
        try {
            // Imposta la modalità del DNS privato su "off"
            Settings.Global.putString(contentResolver, "private_dns_mode", "off")
            
            Toast.makeText(this, R.string.operation_success, Toast.LENGTH_SHORT).show()
            updateDnsStatus()
        } catch (e: Exception) {
            Toast.makeText(this, R.string.operation_failed, Toast.LENGTH_SHORT).show()
        }
    }

    // Aggiorna il testo di stato in base alla configurazione attuale del DNS privato
    private fun updateDnsStatus() {
        try {
            val dnsMode = Settings.Global.getString(contentResolver, "private_dns_mode")
            val isEnabled = dnsMode != "off" && dnsMode != null
            
            if (isEnabled) {
                statusTextView.setText(R.string.status_enabled)
            } else {
                statusTextView.setText(R.string.status_disabled)
            }
        } catch (e: Exception) {
            statusTextView.setText(R.string.status_checking)
        }
    }

    // Apre direttamente le impostazioni DNS privato
    private fun openDnsSettings() {
        try {
            val intent = Intent(Settings.ACTION_PRIVATE_DNS_SETTINGS)
            startActivity(intent)
        } catch (e: Exception) {
            // Se l'intent specifico non è supportato, apre le impostazioni di rete generali
            try {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, R.string.operation_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Mostra un errore di permessi
    private fun showPermissionError() {
        Toast.makeText(this, R.string.permission_error, Toast.LENGTH_LONG).show()
    }
}
