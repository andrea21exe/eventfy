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
    private Impianto impianto;

    public Prenotazione(String titolo, String descrizione, int durata, LocalDate data, LocalTime ora, Utente artista, Impianto impianto) {
        if (artista instanceof Artista) {
            this.id = currentId++;
            this.data = LocalDate.now();
            this.ora = LocalTime.now();
            evento = new Evento(id, titolo, descrizione, durata, data, ora);
            this.impianto = impianto;
            this.artista = (Artista)artista;
        } else {
            throw new IllegalArgumentException("Utente non artista");
        }
    }

    public void setImpianto(Impianto impianto){
        this.impianto = impianto;
    }

    public int getId(){
        return this.id;
    }

    public Impianto getImpianto(){
        return this.impianto;
    }

    public boolean hasGestore(Gestore gestore){
        return gestore == this.impianto.getGestore();
    }


    public boolean hasArtista(Artista artista){
        return artista == this.artista;
    }

    @Override
    public String toString() {
        return "Prenotazione [id=" + id + ", data=" + data + ", ora=" + ora + ", evento=" + evento + ", artista="+ artista + ", impianto=" + impianto + "]";
    }
    
}
