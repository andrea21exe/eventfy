package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class App {

    private static Eventfy sistema;

    public static void main(String[] args) {

        sistema = Eventfy.getIstanceEventfy();
        sistema.populate();
        sistema.signUpLogIn(new Gestore("Andrea"));

        menu();
    }

    private static void menu() {
        displayMenu();
        int scelta = getOperazioneUtente();
        processaScelta(scelta);
    }

    private static void displayMenu() {
        System.out.println("Seleziona un'opzione:");
        System.out.println("1. Inserisci un nuovo impianto");
        System.out.println("2. Richiedi prenotazione impianto");
        System.out.println("3. Gestisci prenotazione");
        System.out.println("4. Aggiungi scaletta ad un evento");
        System.out.println("5. Invita artista");
        System.out.println("6. Gestisci inviti");
        System.out.println("7. Inserisci recensione");
        System.out.println("8. Elimina Prenotazione");


    }

    private static int getOperazioneUtente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il numero dell'opzione desiderata: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Inserisci un numero valido.");
            scanner.next();
        }

        int i = scanner.nextInt();
        // scanner.close();
        return i;
    }

    private static void processaScelta(int scelta) {
        switch (scelta) {
            case 1:
                inserisciImpianto();
                break;
            case 2:
                nuovaPrenotazione();
                System.out.println("Hai selezionato l'Opzione 2");
                break;
            case 3:
                gestisciPrenotazioni();
                System.out.println("Hai selezionato l'Opzione 3");
                break;
            case 4:
                aggiungiScaletta();
                System.out.println("Hai selezionato l'Opzione 4");
                break;
            case 5:
                invitaArtista();
                System.out.println("Hai selezionato l'Opzione 5");
                break;
            case 6:
                gestisciInvito();
                System.out.println("Hai selezionato l'Opzione 6");
                break;
            case 7:
                inserisciRecensione();
                System.out.println("Hai selezionato l'Opzione 7");
                break;
            case 8:
                eliminaPrenotazione();
                System.out.println("Hai selezionato l'Opzione 8");
                break;
            case 9:

                System.out.println("Hai selezionato l'Opzione 9");
                break;
            default:
                System.out.println("Opzione non valida");
        }
    }


    private static void inserisciImpianto() {

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

        // Ritorna al menù
        menu();
    }

    public static void nuovaPrenotazione() {

        Scanner input = new Scanner(System.in);

        sistema.signUpLogIn(new Artista("Andrea"));

        System.out.println("Hai selezionato: Inserisci nuova prenotazione");
        System.out.println("Inserisci il titolo dell'evento");
        String titolo = input.nextLine(); // titolo

        System.out.println("Inserisci una descrizione dell'evento");
        String descrizione = input.nextLine(); // descrizione

        System.out.println("Per quante ore vuoi prenotare l'impianto?");
        while (!input.hasNextInt()) {
            System.out.println("Inserisci un numero intero, riprova");
            input.next();
        }
        int durata = input.nextInt(); // durata

        System.out.println("Qual è la capienza minima dell'impianto in cui vuoi esibirti?");
        while (!input.hasNextInt()) {
            System.out.println("Inserisci un numero intero, riprova");
            input.next();
        }
        int capienza = input.nextInt(); // capienza
        input.nextLine();

        System.out.println("Quando vuoi esibirti? Inserisci la data nel formato dd.mm.yyyy");
        String dataString = input.nextLine(); // data
        LocalDate data = null;
        try {
            // Dividi la stringa in giorno, mese e anno
            String[] giornoMeseAnno = dataString.split("\\.");

            // Estrai giorno, mese e anno come interi
            int giorno = Integer.parseInt(giornoMeseAnno[0]);
            int mese = Integer.parseInt(giornoMeseAnno[1]);
            int anno = Integer.parseInt(giornoMeseAnno[2]);

            data = LocalDate.of(anno, mese, giorno);

        } catch (Exception e) {
            System.out.println("Formato non valido");
        }

        System.out.println("Inserisci l'ora nel formato hh.mm");
        String oraString = input.nextLine(); // ora
        LocalTime orario = null;
        try {
            // Dividi la stringa in ora, minuti
            String[] oraMinuti = oraString.split("\\.");

            // Estrai ora minuti come interi
            int ora = Integer.parseInt(oraMinuti[0]);
            int minuti = Integer.parseInt(oraMinuti[1]);

            orario = LocalTime.of(ora, minuti);

        } catch (Exception e) {
            System.out.println("Formato non valido");
        }

        // Si DOVREBBE verificare che i vari parametri siano validi
        List<Impianto> impiantiDisponibili = sistema.nuovaPrenotazione(titolo, descrizione, durata, capienza, data,
                orario);

        // Stampa la lista di impianti disponibili
        for (Impianto i : impiantiDisponibili) {
            System.out.println(i);
        }

        System.out.println("");
        System.out.println("Inserisci l'ID dell'impianto di cui vuoi richiedere la prenotazione");
        while (!input.hasNextInt()) {
            System.out.println("Inserisci un numero intero, riprova");
            input.next();
        }
        int id = input.nextInt();
        input.nextLine();

        sistema.prenotaImpianto(id);
        System.out.println("");
        System.out.println("Premi invio per confermare la prenotazione");
        input.nextLine();
        System.out.println(sistema.confermaPrenotazione());

        menu();

    }

    public static void gestisciPrenotazioni() {

        Scanner input = new Scanner(System.in);
        // Ottiene la lista delle prenotazioni pendenti dal sistema
        List<Prenotazione> prenotazioniPendenti = sistema.mostraPrenotazioniPendenti();
        // Se non ci sono prenotazioni pendenti, torna al menu principale
        if (prenotazioniPendenti == null || prenotazioniPendenti.isEmpty()) {
            System.out.println("Nessuna prenotazione pendente.");
            menu(); // Torna al menu principale se non ci sono prenotazioni pendenti.
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

        // Ritorna al menù
        menu();
    }

    private static void aggiungiScaletta() {

        Scanner input = new Scanner(System.in);

        if ((sistema.hasArtistaCorrente())) {
            List<Prenotazione> prenotazioni = sistema.aggiungiScaletta();

            if (!prenotazioni.isEmpty()) {
                System.out.println("Prenotazioni accettate per questo artista:");
                for (Prenotazione prenotazione : prenotazioni) {
                    System.out.println(prenotazione);
                }
                System.out.print("Inserisci il codice della prenotazione: ");
                int codice_prenotazione = input.nextInt();
                List<Brano> listaBrani = sistema.recuperaBraniArtista(codice_prenotazione);
                // Se trova brani da aggiungere
                if (listaBrani != null) {
                    System.out.println("Brani dell'artista :");
                    for (Brano brano : listaBrani) {
                        System.out.println(brano);
                    }
                    // Aggiungi il brano alla scaletta
                    System.out.println("Inserisci il codice del brano da aggiungere: ");
                    int codice_brano = input.nextInt();
                    sistema.aggiungiBrano(codice_brano);
                    System.out.println("Brano aggiunto alla scaletta");
                } else {
                    // Nel caso in cui non abbia brani da poter inserire
                    System.out.println("Errore nel recupero dei brani.");
                }

                menu();

            } else {
                // Se l'artista non ha prenotazioni accettate
                System.out.println("Nessuna prenotazione accettata per questo artista. ");
                menu();
            }

        } else {
            System.out.println("Solo un artista può inserire una scaletta di brani ");
        }

        menu();

    }

    private static void invitaArtista() {

        Scanner input = new Scanner(System.in);

        if ((sistema.hasArtistaCorrente())) {
            // Mostra prenotazioni accettate
            List<Prenotazione> prenotazioni = sistema.mostraPrenotazioniAccettate();
            if (!prenotazioni.isEmpty()) {
                System.out.println("Prenotazioni accettate:");
                for (Prenotazione p : prenotazioni) {
                    System.out.println(p);
                }
                System.out.println("Inserisci il codice della prenotazione per cui desideri inviare un invito:");
                int codice_prenotazione = input.nextInt();
                input.nextLine();
                List<Utente> artistiDisponibili = sistema.selezionaPrenotazioneInvito(codice_prenotazione);
                System.out.println("Artisti disponibili per l'invito:");
                for (Utente artista : artistiDisponibili) {
                    System.out.println(artista);
                }
                // Invita artista
                System.out.println("Inserisci il codice dell'artista che desideri invitare:");
                int codice_artista = input.nextInt();
                input.nextLine();
                sistema.invitaArtista(codice_artista);

                System.out.println("Invito inviato con successo!");

            } else {
                // Se l'artista non ha prenotazioni accettate

                System.out.println("Nessuna prenotazione accettata per questo artista. ");
            }
        } else {
            System.out.println("Solo un artista può invitare ");
        }
        menu();
    }

    private static void gestisciInvito() {

        Scanner input = new Scanner(System.in);
        sistema.logIn(3);

        if ((sistema.hasArtistaCorrente())) {
            List<Invito> invitiPendenti = sistema.gestisciInvito();
            if (!invitiPendenti.isEmpty()) {
                System.out.println("Inviti Pendenti:");
                for (Invito invito : invitiPendenti) {
                    System.out.println(invito);
                }
                System.out.println("Inserisci il codice dell'invito che vuoi accettare:");
                int codice_invito = input.nextInt();
                Evento invito = sistema.selezionaInvito(codice_invito);
                System.out.println("Hai selezionato l'invito per l'evento: " + invito);
                sistema.accettaInvito();
            } else {
                System.out.println("Nessun invito pendente.");
            }
        } else {
            System.out.println("Solo un artista può gestire gli inviti");
        }
        // Ritorna al menu principale
        menu();
    }

    private static void inserisciRecensione() {
        Scanner input = new Scanner(System.in);
        // solo per prova
        sistema.logIn(3);

        if ((sistema.hasArtistaCorrente())) {
            List<Prenotazione> prenotazioniRecensibili = sistema.inserisciRecensione();
            // Ottiene la lista delle prenotazioni accettate e svolte da poter recensire
            if (!prenotazioniRecensibili.isEmpty()) {
                System.out.println("Prenotazioni recensibili:");
                for (Prenotazione p : prenotazioniRecensibili) {
                    System.out.println(p);
                }
                // Chiede all'utente di inserire il codice della prenotazione per cui vuole
                // scrivere una recensione
                System.out.println("Inserisci il codice della prenotazione per cui vuoi scrivere una recensione:");
                int codice_prenotazione = input.nextInt();
                System.out.println("Inserisci il commento:");
                String commento = input.nextLine();
                input.next();
                System.out.println("Inserisci il voto (da 1 a 5):");
                int voto = input.nextInt();
                // Chiama il metodo per creare la recensione
                sistema.creaRecensione(codice_prenotazione, commento, voto);
                System.out.println("Recensione inserita ");
            } else {
                System.out.println("Non hai prenotazioni da poter recensire");
            }
        } else {
            System.out.println("Solo un artista può recensire una struttura");
        }
        menu();

    }

    private static void eliminaPrenotazione() {
        Scanner input = new Scanner(System.in);
        // solo per prova
        sistema.logIn(3);
        if ((sistema.hasArtistaCorrente())) {
            List<Prenotazione> prenotazioniEliminabili = sistema.eliminaPrenotazione();
            if (!prenotazioniEliminabili.isEmpty()) {
                System.out.println("Prenotazioni eliminabili:");
                for (Prenotazione p : prenotazioniEliminabili) {
                    System.out.println(p);
                }
                
             System.out.println("Inserisci il codice della prenotazione che vuoi eliminare:");
             int codice_prenotazione = input.nextInt();
             
            sistema.confermaEliminazione(codice_prenotazione);
            System.out.println("Prenotazione eliminata con successo!");

        } else {
            System.out.println("Solo un artista può eliminare una prenotazione di una struttura");
        }
        menu();
    }
    }
}
