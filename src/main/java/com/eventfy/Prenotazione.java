package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;

public class Prenotazione {

    private static int currentId = 0;

    private final int id;
    private LocalDate data;
    private LocalTime ora;
    private Evento evento;
    private Artista artista;

    public Prenotazione(String titolo, String descrizione, int durata, LocalDate data, LocalTime ora, Utente artista) {
        if (artista instanceof Artista) {
            this.id = currentId++;
            this.data = LocalDate.now();
            this.ora = LocalTime.now();
            evento = new Evento(id, titolo, descrizione, durata, data, ora);
        } else {
            throw new IllegalArgumentException("Utente non artista");
        }
    }

}
