package com.eventfy.UI;

import java.util.List;
import java.util.Scanner;

import com.eventfy.Impianto;
import com.eventfy.Prenotazione;
import com.eventfy.RecensioneEvento;
import com.eventfy.RecensioneImpianto;
import com.eventfy.Exceptions.LogException;

public class MenuGestore extends MenuStrategy {

    @Override
    protected void displayMenu() {
        System.out.println("Seleziona un'opzione:");
        System.out.println("1. Inserisci un nuovo impianto");
        System.out.println("2. Gestisci prenotazione");
        System.out.println("3. Visualizza prenotazioni accettate");
        System.out.println("4. Visualizza prenotazioni pendenti");
        System.out.println("5. Visualizza impianti registrati");
        System.out.println("6. Visualizza recensione impianto");
        System.out.println("7. Logout");

    }

    @Override
    protected void processaScelta(int scelta) throws LogException {
        switch (scelta) {
            case 1:
                inserisciImpianto();
                break;
            case 2:
                gestisciPrenotazioni();
                break;
            case 3:
                visualizzaPrenotazioniAccettate();
                break;
            case 4:
                visualizzaPrenotazioniPendenti();
                break;
            case 5:
                visualizzaImpianti();
                break;
            case 6:
                mostraRecensione();
                break;
            case 7:
                logout();
                break;

            default:
                System.out.println("Opzione non valida");
        }
    }

    private void inserisciImpianto() {

        Scanner input = new Scanner(System.in);

        // Inserisci dati impianto
        System.out.println("Hai selezionato: Inserisci nuovo impianto");
        System.out.println("Inserisci il nome dell'impianto");
        String nome = input.nextLine(); // nome

        System.out.println("Inserisci il luogo dell'impianto");
        String luogo = input.nextLine(); // luogo
        // per evitare che il cursore rimanga incastrato e non funzioni correttamente
        input.nextLine();
        // capienza (int)
        System.out.println("Inserisci la capienza (intero)");
        while (!input.hasNextInt()) {
            System.out.println("Capienza deve essere un numero intero, riprova");
            input.next();
        }
        int capienza = input.nextInt();

        // superficie (int)
        System.out.println("Inserisci la superficie (intero)");
        while (!input.hasNextInt()) {
            System.out.println("Superficie deve essere un numero intero, riprova");
            input.next();
        }
        // per evitare che il cursore rimanga incastrato e non funzioni correttamente
        input.nextLine();
        int superficie = input.nextInt();
        input.nextLine();

        // Visualizza il riepilogo dell'impianto inserito
        Impianto impianto = sistema.nuovoImpianto(nome, luogo, capienza, superficie);
        System.out.println("");
        System.out.println("Riepilogo impianto:");
        System.out.println(impianto.toString());
        System.out.println("");

        System.out.println("Premi invio per confermare l'inserimento");
        System.out.println("");
        input.nextLine();

        // Conferma l'inserimento
        sistema.confermaImpianto();

    }

    private void gestisciPrenotazioni() {

        Scanner input = new Scanner(System.in);
        // Ottiene la lista delle prenotazioni pendenti dal sistema
        List<Prenotazione> prenotazioniPendenti = sistema.mostraPrenotazioniPendenti();
        // Se non ci sono prenotazioni pendenti, torna al menu principale
        if (prenotazioniPendenti == null || prenotazioniPendenti.isEmpty()) {
            System.out.println("Nessuna prenotazione pendente.");
            return;
        }
        // Stampa le prenotazioni pendenti
        System.out.println("Prenotazioni Pendenti");
        for (Prenotazione p : prenotazioniPendenti) {
            System.out.println(p);
        }
        // Chiede all'utente di inserire il codice della prenotazione da gestire
        System.out.println("Inserisci il codice della prenotazione da gestire:");
        while (!input.hasNextInt()) {
            System.out.println("Inserisci un numero intero, riprova");
            input.next();
        }
        int codice_prenotazione = input.nextInt();
        // Se la prenotazione è valida
        Prenotazione p = sistema.selezionaPrenotazionePendente(codice_prenotazione);
        if (p != null) {
            System.out.println("Hai selezionato la prenotazione:" + p);
            sistema.accettaPrenotazione();
            // Se la prenotazione non è valida
        } else {
            System.out.println("Prenotazione non valida.");
            // CONSIDERARE IL CASO IN CUI NON VENGONO ACCETTATE LE PRENOTAZIONI NELLE
            // ESTENSIONI
        }

    }

    private void visualizzaPrenotazioniAccettate() {
        // stampa le prenotazioni

        List<Prenotazione> listaPrenotazioniAccettate = sistema.mostraPrenotazioniAccettateGestore();

        if (listaPrenotazioniAccettate == null || listaPrenotazioniAccettate.isEmpty()) {
            System.out.println("Nessuna prenotazione accettata.");
            return;
        }

        // Stampa le prenotazioni pendenti
        System.out.println("Prenotazioni accettate");
        for (Prenotazione p : listaPrenotazioniAccettate) {
            System.out.println(p);
            System.out.println("\n\n");
        }
    }

    private void visualizzaPrenotazioniPendenti() {

        List<Prenotazione> prenotazioniPendenti = sistema.mostraPrenotazioniPendentiGestore();

        if (prenotazioniPendenti == null || prenotazioniPendenti.isEmpty()) {
            System.out.println("Nessuna richiesta di prenotazione.");
            return;
        }

        // Stampa le prenotazioni pendenti
        System.out.println("Prenotazioni pendenti");
        for (Prenotazione p : prenotazioniPendenti) {
            System.out.println(p);
            System.out.println("\n\n");
        }

    }

    private void visualizzaImpianti(){

        List<Impianto> listaImpianti = sistema.visualizzaImpiantiGestore();
        
        if (listaImpianti == null || listaImpianti.isEmpty()) {
            System.out.println("Nessun impianto inserito.");
            return;
        }

        System.out.println("Impianti inseriti");
        for (Impianto im : listaImpianti) {
            System.out.println(im);
            System.out.println("\n\n");
        }

    }


    private void mostraRecensione(){

        visualizzaImpianti();

        System.out.println("inserisci l'id dell'impianto");

        Scanner input = new Scanner(System.in);

        int id = input.nextInt();

        List<RecensioneImpianto> listaRecensione = sistema.mostraRecensioneImpianti(id);

        if (listaRecensione.isEmpty()) {
            System.out.println("Non hai nessuna recensione sul tuo impianto");
            return;
        }

        for (RecensioneImpianto rI : listaRecensione) {
            System.out.println(rI);
        }
    }
}