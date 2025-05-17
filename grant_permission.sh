#!/bin/bash

# Script per concedere i permessi necessari all'app DNS Toggle senza root
# Questo script deve essere eseguito da un computer con ADB installato e dispositivo collegato

echo "Script per concedere permessi a DNS Toggle"
echo "==========================================="
echo ""
echo "Questo script concede il permesso WRITE_SECURE_SETTINGS all'app DNS Toggle"
echo "Assicurati che:"
echo "1. Il dispositivo sia collegato via USB"
echo "2. Il debug USB sia abilitato"
echo "3. L'app DNS Toggle sia già installata sul dispositivo"
echo ""

# Verifica che ADB sia installato
if ! command -v adb &> /dev/null; then
    echo "Errore: ADB non trovato. Installa Android Debug Bridge prima di continuare."
    exit 1
fi

# Verifica che il dispositivo sia collegato
DEVICES=$(adb devices | grep -v "List" | grep "device" | wc -l)
if [ "$DEVICES" -eq 0 ]; then
    echo "Errore: Nessun dispositivo trovato. Collega il dispositivo e abilita il debug USB."
    exit 1
fi

echo "Dispositivo trovato. Concedo i permessi..."

# Concede il permesso
adb shell pm grant com.example.dnstoggle android.permission.WRITE_SECURE_SETTINGS

if [ $? -eq 0 ]; then
    echo ""
    echo "Permesso concesso con successo!"
    echo "Ora puoi utilizzare l'app DNS Toggle per attivare/disattivare il DNS privato."
else
    echo ""
    echo "Errore durante la concessione del permesso."
    echo "Verifica che:"
    echo "1. L'app sia correttamente installata"
    echo "2. Il tuo dispositivo abbia il debug USB attivo"
    echo "3. Hai confermato la richiesta di debug sul dispositivo"
fi
