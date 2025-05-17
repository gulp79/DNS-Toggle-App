package com.example.dnstoggle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

/**
 * Ricevitore per tile delle Impostazioni rapide
 * Per implementare completamente questa funzionalità è necessario aggiungere codice per
 * registrare il tile nelle impostazioni rapide, non incluso in questa versione base
 */
class QuickSettingsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "com.example.dnstoggle.TOGGLE_DNS" -> {
                toggleDnsState(context)
            }
            "com.example.dnstoggle.ENABLE_DNS" -> {
                enablePrivateDns(context)
            }
            "com.example.dnstoggle.DISABLE_DNS" -> {
                disablePrivateDns(context)
            }
        }
    }

    private fun toggleDnsState(context: Context) {
        try {
            val dnsMode = Settings.Global.getString(context.contentResolver, "private_dns_mode")
            val isEnabled = dnsMode != "off" && dnsMode != null
            
            if (isEnabled) {
                disablePrivateDns(context)
            } else {
                enablePrivateDns(context)
            }
        } catch (e: Exception) {
            Toast.makeText(context, R.string.operation_failed, Toast.LENGTH_SHORT).show()
        }
    }

    private fun enablePrivateDns(context: Context) {
        try {
            // Recupera il valore del DNS privato precedentemente configurato
            val currentHost = Settings.Global.getString(context.contentResolver, "private_dns_specifier")
            val hostToUse = if (currentHost.isNullOrEmpty()) "dns.google" else currentHost

            // Imposta la modalità del DNS privato su "hostname"
            Settings.Global.putString(context.contentResolver, "private_dns_mode", "hostname")
            Settings.Global.putString(context.contentResolver, "private_dns_specifier", hostToUse)
            
            Toast.makeText(context, R.string.operation_success, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, R.string.operation_failed, Toast.LENGTH_SHORT).show()
        }
    }

    private fun disablePrivateDns(context: Context) {
        try {
            // Imposta la modalità del DNS privato su "off"
            Settings.Global.putString(context.contentResolver, "private_dns_mode", "off")
            
            Toast.makeText(context, R.string.operation_success, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, R.string.operation_failed, Toast.LENGTH_SHORT).show()
        }
    }
}
