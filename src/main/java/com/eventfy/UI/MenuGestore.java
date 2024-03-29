package com.eventfy.UI;

import java.util.List;
import java.util.Scanner;

import com.eventfy.Impianto;
import com.eventfy.Prenotazione;
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
                mostraRecensioni();
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

        // capienza (int)
        int capienza;
        do {
            System.out.println("Inserisci la capienza (deve essere un numero intero maggiore di 0):");
            while (!input.hasNextInt()) {
                System.out.println("Capienza deve essere un numero intero, riprova:");
                input.next();
            }
            capienza = input.nextInt();

            if (capienza <= 0) {
                System.out.println("La capienza deve essere maggiore di 0, riprova.");
            }
        } while (capienza <= 0);

        // superficie (int)
        int superficie;
        do {
            System.out.println("Inserisci la superficie (deve essere un numero intero maggiore di 0):");
            while (!input.hasNextInt()) {
                System.out.println("La superficie deve essere un numero intero, riprova:");
                input.next();
            }
            superficie = input.nextInt();

            if (superficie <= 0) {
                System.out.println("La superficie deve essere maggiore di 0, riprova.");
            }
        } while (superficie <= 0);
        // per evitare che il cursore rimanga incastrato e non funzioni correttamente
        input.nextLine();

        // Visualizza il riepilogo dell'impianto inserito
        Impianto impianto;
        try {
            impianto = sistema.nuovoImpianto(nome, luogo, capienza, superficie);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

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
        Prenotazione p;
        try {
            p = sistema.selezionaPrenotazionePendente(codice_prenotazione);
        } catch (Exception e) {
            System.out.println("La prenotazione inserita non è valida");
            return;
        }
        if (p != null) {
            System.out.println("Hai selezionato la prenotazione:" + p);
            System.out.println("Digita 0 se vuoi accettare la prenotazione, 1 per rifiutarla o 2 per tornare indietro");

            int scelta = 0; // Inizializzo una variabile

            while (!input.hasNextInt()) {
                System.out.println("Inserisci un numero intero, riprova:");
                input.next(); // Scarta l'input non valido
            }

            // Leggi l'input come intero
            scelta = input.nextInt();

            // Loop per assicurarsi che l'input sia tra 0, 1 o 2
            while (scelta != 0 && scelta != 1 && scelta != 2) {
                System.out.println("Input non valido. Devi inserire 0, 1 o 2:");
                scelta = input.nextInt(); // Leggi un nuovo input
            }

            switch (scelta) {
                case 0:
                    try {
                        sistema.accettaPrenotazione();
                    } catch (Exception e) {
                        System.out.println("La data per il relativo impianto è già occupata");
                        return;
                    }
                    System.out.println("prenotazione accettata");
                    break;
                case 1:
                    try {
                        sistema.confermaEliminazione(codice_prenotazione);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    System.out.println("Prenotazione annullata");

                    break;
                case 2:
                    return;
            }

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

    private void visualizzaImpianti() {

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

    private void mostraRecensioni() {

        visualizzaImpianti();

        System.out.println("inserisci l'id dell'impianto");
        Scanner input = new Scanner(System.in);
        while (!input.hasNextInt()) {
            System.out.println("Inserisci un numero intero, riprova:");
            input.next(); // Scarta l'input non valido
        }
        int id = input.nextInt();

        sistema.mostraRecensioneImpianti(id);

    }
}