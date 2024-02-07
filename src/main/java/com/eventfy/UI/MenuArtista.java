package com.eventfy.UI;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import com.eventfy.Brano;
import com.eventfy.Evento;
import com.eventfy.Impianto;
import com.eventfy.Invito;
import com.eventfy.Prenotazione;
import com.eventfy.Recensione;
import com.eventfy.RecensioneEvento;
import com.eventfy.RecensioneImpianto;
import com.eventfy.Utente;

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
        System.out.println("7. visualizza prenotazioni pendenti");
        System.out.println("8. Visualizza eventi organizzati");
        System.out.println("9. Registra brano");
        System.out.println("10. Visualizza i tuoi brani");
        System.out.println("11. Visualizza recensioni sui tuoi eventi");
        System.out.println("12. Visualizza gli inviti inviati ed accettati per un evento");
        System.out.println("13. Visualizza le recensioni che hai inserito");
        System.out.println("14. Logout");
    }

    @Override
    protected void processaScelta(int scelta) throws Exception {
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
            case 7:
                System.out.println("Hai selezionato l'Opzione 7");
                visualizzaPrenotazioni();
                break;
            case 8:
                System.out.println("Hai selezionato l'Opzione 8");
                visualizzaEventi();
                break;
            case 9:
                System.out.println("Hai selezionato l'Opzione 9");
                registraBrano();
                break;
            case 10:
                System.out.println("Hai selezionato l'Opzione 10");
                visualizzaBrani();
                break;
            case 11:
                System.out.println("Hai selezionato l'Opzione 11");
                visualizzaRecensioni();
                break;
            case 12:
                System.out.println("Hai selezionato l'Opzione 12");
                invitiAccettatiMittente();
                break;
            case 13:
                System.out.println("Hai selezionato l'Opzione 12");
                mostraRecensioniInserite();
                break;
            case 14:
                System.out.println("Hai selezionato l'Opzione 14");
                logout();
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

        // durata
        System.out.println("Per quante ore vuoi prenotare l'impianto?");
        int durata;
        do {
            System.out.println("Inserisci la durata (deve essere un numero intero maggiore di 0):");
            while (!input.hasNextInt()) {
                System.out.println("La durata deve essere un numero intero, riprova:");
                input.next();
            }
            durata = input.nextInt();

            if (durata <= 0) {
                System.out.println("La durata deve essere maggiore di 0, riprova.");
            }
        } while (durata <= 0);

        input.nextLine(); // Consuma il resto della linea

        // capienza
        System.out.println("Qual è la capienza minima dell'impianto in cui vuoi esibirti?");
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

        input.nextLine(); // Consuma il resto della linea

        // data
        System.out.println("Quando vuoi esibirti? Inserisci la data nel formato dd.mm.yyyy");
        String dataString = input.nextLine();
        LocalDate data = null;
        try {
            // Divide la stringa in giorno, mese e anno
            String[] giornoMeseAnno = dataString.split("\\.");

            // Estrae giorno, mese e anno come interi
            int giorno = Integer.parseInt(giornoMeseAnno[0]);
            int mese = Integer.parseInt(giornoMeseAnno[1]);
            int anno = Integer.parseInt(giornoMeseAnno[2]);

            data = LocalDate.of(anno, mese, giorno);

        } catch (Exception e) {
            System.out.println("Hai inserito una data non valida");
            return;
        }

        System.out.println("Inserisci l'ora nel formato hh.mm");
        String oraString = input.nextLine(); // ora
        LocalTime orario = null;
        try {
            // Divide la stringa in ora, minuti
            String[] oraMinuti = oraString.split("\\.");

            // Estrae ora minuti come interi
            int ora = Integer.parseInt(oraMinuti[0]);
            int minuti = Integer.parseInt(oraMinuti[1]);

            orario = LocalTime.of(ora, minuti);

        } catch (Exception e) {
            System.out.println("Hai inserito un orario non valido");
            return;
        }

        List<Impianto> impiantiDisponibili;
        try {
            impiantiDisponibili = sistema.nuovaPrenotazione(titolo,
                    descrizione, durata, capienza, data,
                    orario);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        // Stampa la lista di impianti che possono ospitare l'evento
        if (impiantiDisponibili.isEmpty()) {
            System.out.println("Nessun impianto può ospitare il tuo evento");
            sistema.setPrenotazioneCorrenteNull();
            return;
        }

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

        try {
            sistema.prenotaImpianto(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("");
        System.out.println("Premi invio per confermare la prenotazione");
        input.nextLine();
        System.out.println(sistema.confermaPrenotazione());

    }

    private void aggiungiScaletta() {

        Scanner input = new Scanner(System.in);

        List<Prenotazione> prenotazioni = sistema.aggiungiScaletta();

        if (!prenotazioni.isEmpty()) {
            System.out.println("Prenotazioni accettate per questo artista:");
            for (Prenotazione prenotazione : prenotazioni) {
                System.out.println(prenotazione);
            }
            System.out.print("Inserisci il codice della prenotazione: ");

            while (!input.hasNextInt()) {
                System.out.println("Inserisci un numero intero, riprova:");
                input.next(); // Scarta l'input non valido
            }

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

                while (!input.hasNextInt()) {
                    System.out.println("Inserisci un numero intero, riprova:");
                    input.next(); // Scarta l'input non valido
                }

                int codice_brano = input.nextInt();

                try {
                    sistema.aggiungiBrano(codice_brano);
                } catch (Exception e) {
                    System.out.println("Il brano è già presente in lista");
                }
                System.out.println("Brano aggiunto alla scaletta");
            } else {
                // Nel caso in cui non abbia brani da poter inserire
                System.out.println("Errore nel recupero dei brani.");
            }

        } else {
            // Se l'artista non ha prenotazioni accettate
            System.out.println("Nessuna prenotazione accettata per questo artista. ");
        }

    }

    private void invitaArtista() {

        Scanner input = new Scanner(System.in);

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
           
            try {
                List<Utente> artistiDisponibili = sistema.selezionaPrenotazioneInvito(codice_prenotazione);
                
            for (Utente artista : artistiDisponibili) {
                System.out.println(artista);
            }
            } catch (Exception e) {
                System.out.println("La'prenotazione selezionata è errata");
                return;
            } 
            System.out.println("Artisti disponibili per l'invito:");
    
            // Invita artista
            System.out.println("Inserisci il codice dell'artista che desideri invitare:");
            int codice_artista = input.nextInt();
            input.nextLine();
            try {
                sistema.invitaArtista(codice_artista);
            } catch (Exception e) {
                System.out.println("L'artista è già stato invitato");
                return;
            }
            System.out.println("Invito inviato con successo!");
        } else {
            // Se l'artista non ha prenotazioni accettate

            System.out.println("Nessuna prenotazione accettata per questo artista. ");
        }
    }

    private void gestisciInvito(){

        Scanner input = new Scanner(System.in);
        List<Invito> invitiPendenti = sistema.gestisciInvito();
        if (!invitiPendenti.isEmpty()) {
            System.out.println("Inviti Pendenti:");
            for (Invito invito : invitiPendenti) {
                System.out.println(invito);
            }
            System.out.println("Inserisci il codice dell'invito che vuoi accettare:");
            int codice_invito = input.nextInt();
            input.nextLine();  
            System.out.println("Vuoi accettare o rifiutare l'invito");
            String scelta = input.nextLine();

            if (scelta.equals("accettare")) {
                try {
                    sistema.accettaInvito(codice_invito);
                    System.out.println("invito accettato");
                } catch (Exception e) {
                    System.out.println("id codice errato");
                    return;
                }
            } else if (scelta.equals("rifiutare")) {
                try {
                    sistema.rifiutaInvito(codice_invito);
                    System.out.println("invito rifiutato");

                } catch (Exception e) {
                    System.out.println("id codice errato");
                    return;
                }
            } else {
                System.out.println("Scelta non valida");
            }
        } else {
            System.out.println("Nessun invito pendente.");
        }

    }

    private void inserisciRecensioneStruttura() {
        Scanner input = new Scanner(System.in);
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
            // per evitare che il cursore rimanga incastrato e non funzioni correttamente
            input.nextLine();
            System.out.println("Inserisci il voto (da 0 a 5):");
            int voto = input.nextInt();
            // Chiama il metodo per creare la recensione
            sistema.creaRecensione(codice_prenotazione, commento, voto);
            System.out.println("Recensione inserita ");
        } else {
            System.out.println("Non hai prenotazioni da poter recensire");
        }

    }

    private void eliminaPrenotazione() {
        Scanner input = new Scanner(System.in);
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

        }
    }

    private void visualizzaEventi() {

        List<Evento> eventiOrganizzati = sistema.visualizzaEventiOrganizzati();

        if (eventiOrganizzati == null || eventiOrganizzati.isEmpty()) {
            System.out.println("Nessuna evento organizzato.");
            return;
        }

        // Stampa gli eventi
        System.out.println("Prenotazioni accettate");
        for (Evento e : eventiOrganizzati) {
            System.out.println(e);
            System.out.println("\n\n");
        }

    }

    private void registraBrano() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserisci il titolo del tuo brano:");
        String titolo = scanner.nextLine();

        System.out.println("Inserisci l'album del tuo brano:");
        String album = scanner.nextLine();

        System.out.println("Inserisci l'album del tuo brano:");
        int durata = scanner.nextInt();

        sistema.registraBrano(titolo, album, durata);

        System.out.println("Brano registrato!");
    }

    private void visualizzaBrani() {

        List<Brano> listaBrani = sistema.visualizzaBrani();

        if (listaBrani.isEmpty()) {
            System.out.println("Non hai registrato alcun brano");
            return;
        }

        for (Brano b : listaBrani) {
            System.out.println(b);
        }

    }

    private void visualizzaRecensioni() {

        visualizzaEventi();

        System.out.println("inserisci l'id dell'evento");
        Scanner input = new Scanner(System.in);

        int id = input.nextInt();

        List<RecensioneEvento> listaRecensioni = sistema.mostraRecensioneEvento(id);

        if (listaRecensioni.isEmpty()) {
            System.out.println("Non hai nessuna recensione sul tuo evento");
            return;
        }

        for (RecensioneEvento rE : listaRecensioni) {
            System.out.println(rE);
        }

    }

    private void visualizzaPrenotazioni() {

        List<Prenotazione> listaPrenotazioni = sistema.visualizzaPrenotazioniPendentiArtista();

        if (listaPrenotazioni.isEmpty()) {
            System.out.println("Non hai nessuna recensione sul tuo evento");
            return;
        }

        for (Prenotazione p : listaPrenotazioni) {
            System.out.println(p);
        }
    }

    private void invitiAccettatiMittente() {

        List<Prenotazione> listaPrenotazioni = sistema.mostraPrenotazioniAccettate();
        if (listaPrenotazioni.isEmpty()) {
            System.out.println("Non hai prenotazioni accettate, dunque nessun invito");
            return;
        }

        for (Prenotazione p : listaPrenotazioni) {
            System.out.println(p.toStringEventInfo());
        }

        Scanner input = new Scanner(System.in);

        System.out.println("Inserisci l'id dell'evento di cui vuoi visualizzare gli inviti");
        int idEvento = input.nextInt();

        List<Invito> listaInviti = sistema.mostraInvitiAccettati(idEvento);

        if (listaInviti.isEmpty()) {
            System.out.println("Nessun invito accettato per questo evento");
            return;
        }

        for (Invito i : listaInviti) {
            System.out.println(i);
        }

    }

    private void mostraRecensioniInserite() {

        List<RecensioneImpianto> listaRecensioni = sistema.mostraRecensioniArtista();

        if (listaRecensioni.isEmpty()) {
            System.out.println("Nessuna recensione inserita");
            return;
        }

        for (Recensione r : listaRecensioni) {
            System.out.println(r);
            System.out.println("");
        }
    }

}