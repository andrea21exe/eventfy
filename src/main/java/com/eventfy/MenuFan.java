package com.eventfy;

import java.util.List;
import java.util.Scanner;

public class MenuFan extends MenuStrategy {

    @Override
    protected void displayMenu() {
        System.out.println("Seleziona un'opzione:");
        System.out.println("1. Partecipa ad un evento");
        System.out.println("2. Scrivi una recensione per un evento a cui hai partecipato");
    }

    @Override
    protected void processaScelta(int scelta) {
        switch (scelta) {
            case 1:
                partecipaEvento();
                break;
            case 2:
                inserisciRecensioneEvento();
                break;
            default:
                System.out.println("Opzione non valida");
        }
    }

    private void partecipaEvento() {
        Scanner input = new Scanner(System.in);
        // solo per provare il corretto funzionamento
        if (sistema.hasFanCorrente()) {
            List<Utente> artisti = sistema.mostraArtistiEventi();
            System.out.println("Lista degli artisti:");
            for (Utente artista : artisti) {
                System.out.println(artista);
            }
            System.out.println("Seleziona un artista inserendo il suo codice:");
            int codice_artista = input.nextInt();
            // per evitare che il cursore rimane incastrato e non funzioni correttamente
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
        } else {
            System.out.println("Solo un fan può esprimere la sua partecipazione");
        }
    }

    private void inserisciRecensioneEvento() {
        Scanner input = new Scanner(System.in);
        // solo per provare il corretto funzionamento
        if (sistema.hasFanCorrente()) {
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
            input.nextLine();
            // Richiede all'utente di inserire il voto per l'evento
            System.out.print("Inserisci il voto (da 0 a 5): ");
            int voto = input.nextInt();
            // Conferma la recensione
            sistema.confermaRecensioneEvento(codice_evento, commento, voto);
            System.out.println("Recensione inserita");
        } else {
            System.out.println("Solo un fan può recensire un evento");
        }
    }

}
