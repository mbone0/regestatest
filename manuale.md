# Installazione

- Decidere una cartella radice (d'ora in poi chiamata `base`)

- Compilare ed esportare il codice java in formato jar (d'ora in poi il file è chiamato `nomejar`) in un percorso a scelta (d'ora in poi chiamato `percorsojar`)

- Aprire l'editor del registro di sistema e inserire la seguente chiave `Computer\HKEY_CLASSES_ROOT\jerp`

- Fare click destro sulla chiave e creare una nuova stringa. Dare il nome `URL Protocol`, e lasciare il valore vuoto
- Creare una chiave `Computer\HKEY_CLASSES_ROOT\jerp\shell\open\command`
- Al suo interno modificare il valore `(predefinito)` nel percorso della JVM, seguito da `-jar` e dal `percorsojar`+`nomejar`. Ad esempio: `"C:\Program Files\jdk-17.0.1\bin\java.exe" -jar n:/regesta/jerp.jar`

- Aprire Firefox
- Digitare nella barra degli indirizzi `about:config`
- Cercare il valore `security.fileuri.strict_origin_policy false` e metterlo a falso
- Installare oracle mysql 8 community edition
- Aprire la cartella `db` ed eseguire gli script per creare il database e riempirlo dei dati della consegna. Potrebbe essere necessario personalizzare lo script per cambiare il nome del database.

- Copiare il contenuto di `web` in `base`

- Creare una sottocartella chiamata `configuration` 

- Al suo interno creare un file chiamato `dbconfig.txt`

- Al suo interno scrivere, seguiti ciascuno da un accapo:

- - indirizzo

- - nome

- - nome utente

- - password

    del database.

# Uso

- Aprire `base/web/index.html`
- Inserire nel campo di testo il percorso dii installazione (`base`)
- premere il bottone "Initialize 1" per inizializzare la GUI con i dati del database. In caso non funzionasse, premere "Initialize 2"
- Riempire i campi con la richiesta
- Premere "Search" per avviare l'applicazione. La risposta dovrebbe apparire dopo poco. In caso contrario, premere "Pull Result"