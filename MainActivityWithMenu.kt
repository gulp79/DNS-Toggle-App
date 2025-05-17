package com.example.dnstoggle

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

/**
 * Questa classe estende MainActivity aggiungendo un menu con funzionalità aggiuntive
 * come guida ai permessi, informazioni sull'app e altre opzioni utili.
 */
class MainActivityWithMenu : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_permission_guide -> {
                showPermissionGuide()
                true
            }
            R.id.action_about -> {
                showAboutDialog()
                true
            }
            R.id.action_app_settings -> {
                openAppSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPermissionGuide() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Guida ai Permessi")
        builder.setMessage("""
            Per funzionare correttamente, questa app richiede il permesso WRITE_SECURE_SETTINGS.
            
            Per concedere questo permesso (richiede ADB da PC):
            
            1. Abilita il debug USB nelle impostazioni sviluppatore
            2. Collega il dispositivo al PC
            3. Esegui il seguente comando:
            
            adb shell pm grant com.example.dnstoggle android.permission.WRITE_SECURE_SETTINGS
            
            Oppure usa gli script forniti nell'archivio del progetto.
        """.trimIndent())
        
        builder.setPositiveButton("Ho capito") { dialog, _ ->
            dialog.dismiss()
        }
        
        builder.setNeutralButton("Apri impostazioni sviluppatore") { _, _ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                startActivity(intent)
            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Impossibile aprire le impostazioni sviluppatore",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        
        builder.show()
    }

    private fun showAboutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Informazioni su DNS Toggle")
        builder.setMessage("""
            DNS Toggle v1.0
            
            Un'app semplice per attivare/disattivare rapidamente il DNS privato di Android.
            
            Questa app è open source e disponibile su GitHub.
        """.trimIndent())
        
        builder.setPositiveButton("Chiudi") { dialog, _ ->
            dialog.dismiss()
        }
        
        builder.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}
