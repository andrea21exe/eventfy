package com.eventfy.UI;

import java.util.List;
import java.util.Scanner;

import com.eventfy.Evento;
import com.eventfy.Prenotazione;
import com.eventfy.Utente;
import com.eventfy.Exceptions.LogException;

public class MenuFan extends MenuStrategy {

    @Override
    protected void displayMenu() {
        System.out.println("Seleziona un'opzione:");
        System.out.println("1. Partecipa ad un evento");
        System.out.println("2. Scrivi una recensione per un evento a cui hai partecipato");
        System.out.println("3. Visualizza commenti inseriti");
        System.out.println("4. Logout");

    }

    @Override
    protected void processaScelta(int scelta) throws LogException {
        switch (scelta) {
            case 1:
                partecipaEvento();
                break;
            case 2:
                inserisciRecensioneEvento();
                break;
            case 3:
                visualizzaRecensioni();
                break;
            case 4:
                logout();
                break;
            default:
                System.out.println("Opzione non valida");
        }
    }

    private void partecipaEvento() {
        Scanner input = new Scanner(System.in);
        List<Utente> artisti = sistema.mostraArtistiEventi();
        if (artisti.isEmpty()) {
            System.out.println("Non sono presenti artisti nel sistema");
            return;
        }
        System.out.println("Lista degli artisti:");
        for (Utente artista : artisti) {
            System.out.println(artista);
        }
        System.out.println("Seleziona un artista inserendo il suo codice:");
        while (!input.hasNextInt()) {
            System.out.println("Inserisci un numero intero, riprova:");
            input.next(); // Scarta l'input non valido
        }
        int codice_artista = input.nextInt();
        // Consuma il resto della linea
        input.nextLine();

        List<Prenotazione> prenotazioniPartecipabili;
        try {
            prenotazioniPartecipabili = sistema.partecipaEvento(codice_artista);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Prenotazioni partecipabili:");
        for (Prenotazione p : prenotazioniPartecipabili) {
            System.out.println(p);
        }

        System.out.println("Inserisci il codice della prenotazione a cui vuoi partecipare:");
        while (!input.hasNextInt()) {
            System.out.println("Inserisci un numero intero, riprova:");
            input.next(); // Scarta l'input non valido
        }
        int codice_prenotazione = input.nextInt();
        try {
            sistema.confermaPartecipazione(codice_prenotazione);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Partecipazione confermata con successo!");

    }

    private void inserisciRecensioneEvento() {
        Scanner input = new Scanner(System.in);
        // Ottiene la lista degli eventi disponibili per la recensione
        List<Evento> eventi = sistema.inserisciRecensioneEvento();
        if (eventi.isEmpty()) {
            System.out.println("Eventi non disponibili per recensione:");
            return;
        }
        System.out.println("Eventi disponibili per recensione:");
        for (Evento e : eventi) {
            System.out.println(e);
        }

        System.out.print("Inserisci l'id dell'evento per cui vuoi scrivere una recensione: ");
        while (!input.hasNextInt()) {
            System.out.println("Inserisci un numero intero, riprova:");
            input.next(); // Scarta l'input non valido
        }
        int codice_evento = input.nextInt();

        // Richiede all'utente di inserire il commento per la recensione
        System.out.print("Inserisci il commento: ");
        String commento = input.nextLine();
        // Consuma il resto della linea
        input.nextLine();

        // Richiede all'utente di inserire il voto per l'evento
        System.out.print("Inserisci il voto (da 0 a 5): ");
        while (!input.hasNextInt()) {
            System.out.println("Inserisci un numero intero, riprova:");
            input.next(); // Scarta l'input non valido
        }
        int voto = input.nextInt();
        while (voto < 0 || voto > 5) {
            System.out.println("Il voto deve essere compreso tra 0 e 5. Riprova.");
            voto = input.nextInt();
        }
        
        // Conferma la recensione
        try {
            sistema.confermaRecensioneEvento(codice_evento, commento, voto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void visualizzaRecensioni() {
        sistema.mostraRecensioniFan();
    }

}
