package com.example.dnstoggle

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * Classe migliorata del MainActivity con gestione del ciclo di vita e delle autorizzazioni.
 * Questa classe mostra un esempio di implementazione più completa che può essere utilizzata
 * al posto di MainActivity.kt per un'applicazione più robusta.
 */
class EnhancedMainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var btnEnableDns: Button
    private lateinit var btnDisableDns: Button
    private lateinit var infoTextView: TextView

    // Registro un launcher per il risultato dell'attività delle impostazioni di sistema
    private val settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Aggiorna lo stato dopo il ritorno dalle impostazioni
            updateDnsStatus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inizializza le viste
        statusTextView = findViewById(R.id.statusTextView)
        btnEnableDns = findViewById(R.id.btnEnableDns)
        btnDisableDns = findViewById(R.id.btnDisableDns)
        infoTextView = findViewById(R.id.infoTextView)
        val btnSettings = findViewById<Button>(R.id.btnSettings)

        // Imposta i listener per i pulsanti
        btnEnableDns.setOnClickListener {
            if (hasRequiredPermissions()) {
                enablePrivateDns()
            } else {
                showPermissionExplanationDialog()
            }
        }

        btnDisableDns.setOnClickListener {
            if (hasRequiredPermissions()) {
                disablePrivateDns()
            } else {
                showPermissionExplanationDialog()
            }
        }

        btnSettings.setOnClickListener {
            openDnsSettings()
        }

        // Controlla lo stato iniziale e i permessi
        checkPermissionsAndUpdateUI()
    }

    override fun onResume() {
        super.onResume()
        // Aggiorna lo stato quando l'app torna in primo piano
        updateDnsStatus()
        
        // Aggiorna anche il testo informativo in base ai permessi
        updatePermissionInfoText()
    }

    // Verifica se l'app ha i permessi necessari
    private fun hasRequiredPermissions(): Boolean {
        return try {
            Settings.System.canWrite(this)
        } catch (e: Exception) {
            false
        }
    }

    // Controlla permessi e aggiorna l'interfaccia di conseguenza
    private fun checkPermissionsAndUpdateUI() {
        updateDnsStatus()
        updatePermissionInfoText()
    }

    // Aggiorna il testo informativo in base allo stato dei permessi
    private fun updatePermissionInfoText() {
        if (!hasRequiredPermissions()) {
            infoTextView.text = getString(R.string.info_text)
            infoTextView.visibility = View.VISIBLE
        } else {
            infoTextView.visibility = View.GONE
        }
    }

    // Abilita il DNS privato
    private fun enablePrivateDns() {
        try {
            val currentHost = Settings.Global.getString(contentResolver, "private_dns_specifier")
            val hostToUse = if (currentHost.isNullOrEmpty()) "dns.google" else currentHost

            Settings.Global.putString(contentResolver, "private_dns_mode", "hostname")
            Settings.Global.putString(contentResolver, "private_dns_specifier", hostToUse)
            
            Toast.makeText(this, R.string.operation_success, Toast.LENGTH_SHORT).show()
            updateDnsStatus()
        } catch (e: Exception) {
            Toast.makeText(this, R.string.operation_failed, Toast.LENGTH_SHORT).show()
            logError("Error enabling DNS", e)
        }
    }

    // Disabilita il DNS privato
    private fun disablePrivateDns() {
        try {
            Settings.Global.putString(contentResolver, "private_dns_mode", "off")
            
            Toast.makeText(this, R.string.operation_success, Toast.LENGTH_SHORT).show()
            updateDnsStatus()
        } catch (e: Exception) {
            Toast.makeText(this, R.string.operation_failed, Toast.LENGTH_SHORT).show()
            logError("Error disabling DNS", e)
        }
    }

    // Aggiorna il testo di stato in base alla configurazione attuale del DNS privato
    private fun updateDnsStatus() {
        try {
            val dnsMode = Settings.Global.getString(contentResolver, "private_dns_mode")
            val isEnabled = dnsMode != "off" && dnsMode != null
            
            if (isEnabled) {
                statusTextView.setText(R.string.status_enabled)
                
                // Opzionalmente, mostra anche il server DNS configurato
                try {
                    val dnsServer = Settings.Global.getString(contentResolver, "private_dns_specifier")
                    if (!dnsServer.isNullOrEmpty()) {
                        val statusText = "${getString(R.string.status_enabled)} ($dnsServer)"
                        statusTextView.text = statusText
                    }
                } catch (e: Exception) {
                    // Ignora errori nella lettura del server
                }
                
            } else {
                statusTextView.setText(R.string.status_disabled)
            }
        } catch (e: Exception) {
            statusTextView.setText(R.string.status_checking)
            logError("Error checking DNS status", e)
        }
    }

    // Apre direttamente le impostazioni DNS privato
    private fun openDnsSettings() {
        try {
            val intent = Intent(Settings.ACTION_PRIVATE_DNS_SETTINGS)
            settingsLauncher.launch(intent)
        } catch (e: Exception) {
            // Se l'intent specifico non è supportato, apre le impostazioni di rete generali
            try {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                settingsLauncher.launch(intent)
            } catch (e: Exception) {
                Toast.makeText(this, R.string.operation_failed, Toast.LENGTH_SHORT).show()
                logError("Error opening settings", e)
            }
        }
    }

    // Mostra un dialog che spiega come concedere i permessi
    private fun showPermissionExplanationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permessi necessari")
        builder.setMessage(
            "Per modificare il DNS privato, questa app necessita di permessi speciali. " +
            "Questi permessi possono essere concessi solo tramite ADB con il seguente comando:\n\n" +
            "adb shell pm grant com.example.dnstoggle android.permission.WRITE_SECURE_SETTINGS\n\n" +
            "Vuoi aprire le impostazioni di sistema per abilitare il debug USB?"
        )
        
        builder.setPositiveButton("Apri impostazioni") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Impossibile aprire le impostazioni sviluppatore",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        
        builder.setNegativeButton("Annulla") { dialog, _ ->
            dialog.dismiss()
        }
        
        builder.show()
    }

    // Metodo di supporto per il logging degli errori
    private fun logError(message: String, error: Exception) {
        // In un'app reale, qui si potrebbe implementare un sistema di logging più robusto
        android.util.Log.e("DNSToggle", "$message: ${error.message}", error)
    }
}
