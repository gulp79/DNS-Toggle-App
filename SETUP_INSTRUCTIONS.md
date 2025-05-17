# Istruzioni per l'utilizzo e la compilazione dell'app DNS Toggle

## Introduzione

DNS Toggle è un'applicazione Android che consente di abilitare o disabilitare rapidamente il DNS privato senza dover navigare nelle impostazioni del sistema. Questa guida ti fornirà i passaggi necessari per:

1. Configurare l'ambiente di sviluppo
2. Compilare l'applicazione
3. Installare l'app sul tuo dispositivo
4. Concedere i permessi necessari
5. Utilizzare l'applicazione

## Prerequisiti

- Android Studio (ultima versione stabile)
- Android SDK (API level 23 o superiore)
- Dispositivo Android o emulatore
- Cavo USB per il collegamento del dispositivo (se si utilizza un dispositivo fisico)
- Conoscenza base di ADB (Android Debug Bridge)

## Configurazione dell'ambiente di sviluppo

1. Clona il repository GitHub:
   ```
   git clone https://github.com/username/dns-toggle.git
   ```

2. Apri Android Studio e seleziona "Open an existing Android Studio project"

3. Seleziona la directory del progetto clonato

4. Attendi che Android Studio completi la sincronizzazione del progetto con Gradle

## Compilazione dell'applicazione

### Compilazione da Android Studio

1. In Android Studio, vai su "Build" > "Build Bundle(s) / APK(s)" > "Build APK(s)"
2. Attendi il completamento della compilazione
3. Android Studio ti notificherà il percorso in cui il file APK è stato salvato

### Compilazione tramite linea di comando

1. Naviga alla directory principale del progetto
2. Esegui:
   ```
   ./gradlew assembleDebug
   ```
3. Il file APK sarà disponibile in `app/build/outputs/apk/debug/app-debug.apk`

## Installazione dell'app

### Metodo 1: Direttamente da Android Studio

1. Collega il dispositivo Android al computer via USB
2. Assicurati che il debugging USB sia abilitato sul dispositivo
3. In Android Studio, seleziona il dispositivo dal menu a discesa e clicca su "Run"

### Metodo 2: Utilizzando l'APK compilato

1. Trasferisci il file APK sul dispositivo
2. Sul dispositivo, vai nelle impostazioni e abilita "Consenti installazione da fonti sconosciute"
3. Utilizza un file manager per trovare e installare l'APK

## Concessione dei permessi

L'app richiede il permesso speciale `WRITE_SECURE_SETTINGS` che non può essere concesso dall'interfaccia utente standard. È necessario utilizzare ADB:

### Utilizzo dello script di concessione dei permessi

1. Collega il dispositivo via USB
2. Abilita il debugging USB nelle impostazioni sviluppatore
3. Esegui lo script incluso:
   - Su Linux/Mac: `./grant_permissions.sh`
   - Su Windows: `grant_permissions.bat`

### Concessione manuale dei permessi

1. Assicurati che ADB sia installato e configurato sul tuo computer
2. Collega il dispositivo al computer via USB
3. Apri un terminale o prompt dei comandi
4. Esegui il seguente comando:
   ```
   adb shell pm grant com.example.dnstoggle android.permission.WRITE_SECURE_SETTINGS
   ```

## Utilizzo dell'applicazione

1. Apri l'app DNS Toggle
2. L'interfaccia mostrerà lo stato attuale del DNS privato
3. Premi il pulsante "ON" per abilitare il DNS privato
4. Premi il pulsante "OFF" per disabilitare il DNS privato
5. Puoi anche premere il pulsante "Apri impostazioni DNS" per accedere direttamente alle impostazioni DNS di sistema

## Risoluzione dei problemi

### L'app mostra "Permessi mancanti"
- Assicurati di aver concesso il permesso `WRITE_SECURE_SETTINGS` tramite ADB
- Riavvia l'app dopo aver concesso il permesso

### L'operazione fallisce
- Alcuni dispositivi potrebbero avere implementazioni diverse delle impostazioni di sistema
- Verifica che il tuo dispositivo supporti il DNS privato nelle impostazioni di sistema
- Assicurati di avere Android 9 (Pie) o superiore, poiché il DNS privato è stato introdotto in questa versione

### L'app si chiude inaspettatamente
- Verifica che tutte le autorizzazioni siano state concesse correttamente
- Controlla i log di sistema per informazioni dettagliate sull'errore

## Contribuire al progetto

Se desideri contribuire al progetto, ti invitiamo a:

1. Fare un fork del repository
2. Creare un nuovo branch per le tue modifiche
3. Inviare una pull request con le tue modifiche
4. Descrivere dettagliatamente le modifiche proposte

## Licenza

L'app è distribuita sotto licenza MIT. Vedi il file `LICENSE` per i dettagli.
