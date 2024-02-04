package com.eventfy;

import java.util.List;
import java.util.Scanner;

public class MenuGestore extends MenuStrategy {

    @Override
    protected void displayMenu() {
        System.out.println("Seleziona un'opzione:");
        System.out.println("1. Inserisci un nuovo impianto");
        System.out.println("2. Gestisci prenotazione");
        System.out.println("3. Visualizza prenotazioni accettate");
        System.out.println("4. Visualizza prenotazioni pendenti");

    }

    @Override
    protected void processaScelta(int scelta) {
        switch (scelta) {
            case 1:
                inserisciImpianto();
                break;
            case 2:
                gestisciPrenotazioni();
                System.out.println("Hai selezionato l'Opzione 2");
                break;
            case 3:
                visualizzaPrenotazioniAccettate();
                System.out.println("Hai selezionato l'Opzione 3");
                break;
            case 4:
                visualizzaPrenotazioniPendenti();
                System.out.println("Hai selezionato l'Opzione 4");
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

    private void visualizzaPrenotazioniAccettate(){
        //stampa le prenotazioni

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

    private void visualizzaPrenotazioniPendenti(){

        List<Prenotazione> prenotazioniPendenti = sistema.mostraPrenotazioniPendentiGestore();

        if (prenotazioniPendenti == null || prenotazioniPendenti.isEmpty()) {
            System.out.println("Nessuna richiesta di prenotazione.");
            return;
        }

        // Stampa le prenotazioni pendenti
        System.out.println("Prenotazioni accettate");
        for (Prenotazione p : prenotazioniPendenti) {
            System.out.println(p);
            System.out.println("\n\n");
        }

    }
}