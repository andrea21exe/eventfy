package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class MenuArtista extends MenuStrategy {

    @Override
    protected void displayMenu() {
        System.out.println("Seleziona un'opzione:");
        System.out.println("1. Richiedi prenotazione impianto");
        System.out.println("2. Aggiungi scaletta ad un evento");
        System.out.println("3. Invita artista");
        System.out.println("4. Gestisci Inviti");
        System.out.println("5. Inserisci una recensione per una struttura");
        System.out.println("6. Elimina prenotazione");
    }

    @Override
    protected void processaScelta(int scelta) {
        switch (scelta) {
            case 1:
                System.out.println("Hai selezionato l'Opzione 1");
                nuovaPrenotazione();
                break;
            case 2:
                System.out.println("Hai selezionato l'Opzione 2");
                aggiungiScaletta();
                break;
            case 3:
                System.out.println("Hai selezionato l'Opzione 3");
                invitaArtista();
                break;
            case 4:
                System.out.println("Hai selezionato l'Opzione 4");
                gestisciInvito();
                break;
            case 5:
                System.out.println("Hai selezionato l'Opzione 5");
                inserisciRecensioneStruttura();
                break;
            case 6:
                System.out.println("Hai selezionato l'Opzione 6");
                eliminaPrenotazione();
                break;
            default:
                System.out.println("Opzione non valida");
        }
    }

    private void nuovaPrenotazione() {

        Scanner input = new Scanner(System.in);

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
        List<Impianto> impiantiDisponibili = sistema.nuovaPrenotazione(titolo,
                descrizione, durata, capienza, data,
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

    }

    private void aggiungiScaletta() {

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

            } else {
                // Se l'artista non ha prenotazioni accettate
                System.out.println("Nessuna prenotazione accettata per questo artista. ");
            }

        } else {
            System.out.println("Solo un artista può inserire una scaletta di brani ");
        }

    }

    private void invitaArtista() {

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
    }

    private void gestisciInvito() {

        Scanner input = new Scanner(System.in);
        // solo per provare il corretto funzionamento

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
    }

    private void inserisciRecensioneStruttura() {
        Scanner input = new Scanner(System.in);
        // solo per prova

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
                // per evitare che il cursore rimane incastrato e non funzioni correttamente
                input.nextLine();
                System.out.println("Inserisci il voto (da 0 a 5):");
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

    }

    private void eliminaPrenotazione() {
        Scanner input = new Scanner(System.in);
        // solo per provare il corretto funzionamento
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
        }
    }

}