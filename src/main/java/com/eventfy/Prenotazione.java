package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Prenotazione {

    private static int currentId = 0;

    private final int id;
    private LocalDate data;
    private LocalTime ora;
    private Evento evento;
    private Artista artista;
    private Impianto impianto;

    public Prenotazione(String titolo, String descrizione, int durata, LocalDate data, LocalTime ora, Artista artista,
            Impianto impianto) {
            this.id = currentId++;
            this.data = LocalDate.now();
            this.ora = LocalTime.now();
            this.evento = new Evento(id, titolo, descrizione, durata, data, ora);
            this.impianto = impianto;
            this.artista = artista;
        
    }

    public void setImpianto(Impianto impianto) {
        this.impianto = impianto;
    }

    public int getId() {
        return this.id;
    }

    public Impianto getImpianto() {
        return this.impianto;
    }

    public boolean hasGestore(Gestore gestore) {
        return gestore == this.impianto.getGestore();
    }

    public boolean hasArtista(Artista artista) {
        return artista == this.artista;
    }

    public Artista getArtista() {
        return this.artista;
    }

    public Evento getEvento() {
        return this.evento;
    }

    public void addBrano(Brano brano) {
        evento.addBrano(brano);
    }

    public boolean isRecensibile() {
        return this.evento.isRecensibile();
    }

    public boolean isEliminabile(){
        return this.evento.isEliminabile();
    }
    //uguali
    public boolean isPartecipabile() {
        return this.evento.isPartecipabile();
    }

    public String getNomeArtista() {
        return artista.getNome();
    }

    public int getIdArtista() {
        return artista.getId();
    }

    public void addPartecipazioneFan(Fan fan) {
        this.evento.addFan(fan);
    }

    public void creaRecensione(String commento, int voto) {
        this.impianto.recensisci(commento, voto, this.artista);
    }

    public void creaRecensione(String commento, int voto, Fan fan) {
        this.evento.recensisci(commento, voto, fan);
    }

    public boolean hasPartecipante(Fan fan) {
        return this.evento.hasPartecipante(fan);
    }

    public List<RecensioneEvento> getListaRecensioni(){
        return this.evento.getListaRecensioni();
    } 

    public int getNumRecensioniEvento(){
        return this.evento.getNumRecensioni();
    }

    @Override
    public String toString() {
        return "-- Prenotazione [id=" + id + ", data=" + data + ", ora=" + ora + ", \nevento=" + evento + ",\nartista="
                + artista.getNome() + ", \nimpianto=" + impianto + "]";
    }

}
