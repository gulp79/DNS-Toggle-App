# DNS Toggle App

Un'applicazione Android semplice per abilitare o disabilitare velocemente l'impostazione del DNS privato senza dover navigare nelle impostazioni di sistema.

## Funzionalità

- Pulsante ON: Attiva il DNS privato utilizzando il server DNS già configurato nelle impostazioni
- Pulsante OFF: Disattiva il DNS privato
- Visualizzazione dello stato attuale del DNS privato
- Accesso rapido alle impostazioni DNS di sistema

## Requisiti

- Android 6.0 (API livello 23) o superiore
- Permessi di sistema speciali (vedi sotto)

## Permessi richiesti

Questa app richiede il permesso `WRITE_SECURE_SETTINGS` per modificare le impostazioni di sistema. Questo permesso non può essere concesso tramite l'interfaccia utente standard e richiede un dispositivo con root o l'utilizzo di ADB (Android Debug Bridge).

### Concedere i permessi tramite ADB (per dispositivi non root)

1. Abilita le opzioni sviluppatore sul tuo dispositivo Android (vai su Impostazioni > Informazioni sul telefono e tocca "Numero build" 7 volte)
2. Abilita il debug USB nelle opzioni sviluppatore
3. Collega il dispositivo al computer tramite USB
4. Apri un terminale/prompt dei comandi e esegui:

```
adb shell pm grant com.example.dnstoggle android.permission.WRITE_SECURE_SETTINGS
```

## Compilazione

1. Clona il repository
2. Apri il progetto in Android Studio
3. Compila e installa sul dispositivo

## Generazione APK

Per generare un APK firmato:

1. In Android Studio, vai su Build > Generate Signed Bundle / APK
2. Seleziona APK
3. Segui le istruzioni per creare o utilizzare una chiave esistente
4. Seleziona "release" come variante di build

## Licenza

MIT License

## Contribuire

I contributi sono benvenuti! Per favore, apri una issue prima di inviare modifiche sostanziali.
