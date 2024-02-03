package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Evento {

    private final int id;
    private String titolo;
    private String descrizione;
    private int durata;
    private LocalDate data;
    private LocalTime ora;
    private List<Brano> listaBrani;
    private List<Fan> listaPartecipanti;
    private List<RecensioneEvento> listaRecensioni;

    public Evento(int id, String titolo, String descrizione, int durata, LocalDate data, LocalTime ora) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.durata = durata;
        this.data = data;
        this.ora = ora;
        this.listaBrani = new ArrayList<Brano>();
        this.listaPartecipanti = new ArrayList<Fan>();
        this.listaRecensioni = new ArrayList<RecensioneEvento>();
    }

    public void addBrano(Brano b) {
        this.listaBrani.add(b);
    }

    public int getId() {
        return this.id;
    }

    public List<Brano> getListaBrani() {
        return this.listaBrani;
    }

    public LocalDate getData() {
        return this.data;
    }

    public void addFan(Fan utenteCorrente) {
        this.listaPartecipanti.add(utenteCorrente);
        utenteCorrente.addPartecipazione(this);
    }

    public List<Fan> getListaFan() {
        return this.listaPartecipanti;
    }

    public void recensisci(String commento, int voto, Fan fan) {
        RecensioneEvento recensione = new RecensioneEvento(commento, voto, fan);
        this.listaRecensioni.add(0, recensione);
    }

    public boolean hasPartecipante(Fan fan) {
        return this.listaPartecipanti.contains(fan);
    }

    public boolean isRecensibile(){
        return LocalDate.now().isAfter(this.data);
    }

    public boolean isEliminabile(){
        return LocalDate.now().isBefore(this.data);
    }
    //uguali
    public boolean isPartecipabile(){
        return LocalDate.now().isBefore(this.data);
    }

    public int getNumRecensioni(){
        return this.listaRecensioni.size();
    }

    @Override
    public String toString() {
        return "Evento [id=" + id + ", titolo=" + titolo + ", descrizione=" + descrizione + ", durata=" + durata
                + ", data=" + data + ", ora=" + ora + ", brani=" + listaBrani + ", partecipanti="
                + listaPartecipanti.size() + "]";
    }

}
