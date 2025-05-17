@echo off
REM Script per concedere i permessi necessari all'app DNS Toggle senza root
REM Questo script deve essere eseguito da un computer Windows con ADB installato e dispositivo collegato

echo Script per concedere permessi a DNS Toggle
echo ===========================================
echo.
echo Questo script concede il permesso WRITE_SECURE_SETTINGS all'app DNS Toggle
echo Assicurati che:
echo 1. Il dispositivo sia collegato via USB
echo 2. Il debug USB sia abilitato
echo 3. L'app DNS Toggle sia già installata sul dispositivo
echo.

REM Verifica che ADB sia installato
where adb >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Errore: ADB non trovato. Installa Android Debug Bridge prima di continuare.
    goto :end
)

REM Verifica che il dispositivo sia collegato
adb devices | findstr "device$" >nul
if %ERRORLEVEL% NEQ 0 (
    echo Errore: Nessun dispositivo trovato. Collega il dispositivo e abilita il debug USB.
    goto :end
)

echo Dispositivo trovato. Concedo i permessi...

REM Concede il permesso
adb shell pm grant com.example.dnstoggle android.permission.WRITE_SECURE_SETTINGS

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Permesso concesso con successo!
    echo Ora puoi utilizzare l'app DNS Toggle per attivare/disattivare il DNS privato.
) else (
    echo.
    echo Errore durante la concessione del permesso.
    echo Verifica che:
    echo 1. L'app sia correttamente installata
    echo 2. Il tuo dispositivo abbia il debug USB attivo
    echo 3. Hai confermato la richiesta di debug sul dispositivo
)

:end
pause
