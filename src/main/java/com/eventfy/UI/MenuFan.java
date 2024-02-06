package com.eventfy.UI;

import java.util.List;
import java.util.Scanner;

import com.eventfy.Evento;
import com.eventfy.Prenotazione;
import com.eventfy.Recensione;
import com.eventfy.RecensioneEvento;
import com.eventfy.Utente;
import com.eventfy.Exceptions.LogException;

public class MenuFan extends MenuStrategy {

    @Override
    protected void displayMenu() {
        System.out.println("Seleziona un'opzione:");
        System.out.println("1. Partecipa ad un evento");
        System.out.println("2. Scrivi una recensione per un evento a cui hai partecipato");
        System.out.println("3. Visualizza eventi di un dato artista");
        System.out.println("4. Visualizza commenti inseirti");
        System.out.println("5. Logout");

    }

    @Override
    protected void processaScelta(int scelta) throws LogException{
        switch (scelta) {
            case 1:
                partecipaEvento();
                break;
            case 2:
                inserisciRecensioneEvento();
                break;
            case 3:
                visualizzaEventiArtista();
                break;
            case 4:
                visualizzaRecensioni();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Opzione non valida");
        }
    }

    private void partecipaEvento() {
        Scanner input = new Scanner(System.in);

        List<Utente> artisti = sistema.mostraArtistiEventi();
        System.out.println("Lista degli artisti:");
        for (Utente artista : artisti) {
            System.out.println(artista);
        }
        System.out.println("Seleziona un artista inserendo il suo codice:");
        int codice_artista = input.nextInt();
        // per evitare che il cursore rimanga incastrato e non funzioni correttamente
        input.nextLine();
        List<Prenotazione> prenotazioniPartecipabili = sistema.partecipaEvento(codice_artista);
        System.out.println("Prenotazioni partecipabili:");
        for (Prenotazione p : prenotazioniPartecipabili) {
            System.out.println(p);
        }
        System.out.println("Inserisci il codice della prenotazione a cui vuoi partecipare:");
        int codice_prenotazione = input.nextInt();
        sistema.confermaPartecipazione(codice_prenotazione);
        System.out.println("Partecipazione confermata con successo!");

    }

    private void inserisciRecensioneEvento() {
        Scanner input = new Scanner(System.in);
        // Ottiene la lista degli eventi disponibili per la recensione
        List<Evento> eventi = sistema.inserisciRecensioneEvento();
        System.out.println("Eventi disponibili per recensione:");
        for (Evento e : eventi) {
            System.out.println(e);
        }
        System.out.print("Inserisci l'id dell'evento per cui vuoi scrivere una recensione: ");
        int codice_evento = input.nextInt();
        // Richiede all'utente di inserire il commento per la recensione
        System.out.print("Inserisci il commento: ");
        String commento = input.nextLine();
        // per evitare che il cursore rimanga incastrato e non funzioni correttamente
        input.nextLine();
        // Richiede all'utente di inserire il voto per l'evento
        System.out.print("Inserisci il voto (da 0 a 5): ");
        int voto = input.nextInt();
        // Conferma la recensione
        sistema.confermaRecensioneEvento(codice_evento, commento, voto);
    }

    private void visualizzaEventiArtista() {

        Scanner input = new Scanner(System.in);
        System.out.print("Inserisci il nome dell'artista di cui vuoi vedere eventi disponibili: ");
        String nomeArtista = input.nextLine();

        List<Evento> eventiArtista = sistema.mostraEventiArtista(nomeArtista);

        if (eventiArtista == null || eventiArtista.isEmpty()) {
            System.out.println("Nessuna evento presente dell' artista: " + nomeArtista);
            return;
        }

        // Stampa gli eventi
        System.out.println("eventi artista " + nomeArtista);

        for (Evento e : eventiArtista) {
            System.out.println(e);
            System.out.println("\n\n");
        }
    }

    private void visualizzaRecensioni(){
        List<Recensione> listaRecensioni = sistema.mostraRecensioniFan();

        if (listaRecensioni.isEmpty()) {
            System.out.println("Non hai nessuna recensione inserita");
            return;
        }

        for (Recensione r : listaRecensioni) {
            System.out.println(r);
            System.out.println("");
        }
    }

}
