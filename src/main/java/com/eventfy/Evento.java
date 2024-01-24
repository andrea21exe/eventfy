package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Evento implements Recensibile{

    private final int id;
    private String titolo;
    private String descrizione;
    private int durata;
    private LocalDate data;
    private LocalTime ora;
    private List<Brano> listaBrani;
    private List<Fan> listaPartecipanti;
    private List<Recensione> listaRecensioni;

    public Evento(int id, String titolo, String descrizione, int durata, LocalDate data, LocalTime ora) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.durata = durata;
        this.data = data;
        this.ora = ora;
        this.listaBrani = new ArrayList<Brano>();
        this.listaPartecipanti = new ArrayList<Fan>();
        this.listaRecensioni = new ArrayList<Recensione>();
    }


    public void addBrano(Brano b){
        this.listaBrani.add(b);
    }

    public int getId(){
        return this.id;
    }

    public List<Brano> getListaBrani(){
        return this.listaBrani;
    }    

    public LocalDate getData() {
        return this.data;
    }

    public void addFan(Fan utenteCorrente){
        this.listaPartecipanti.add(utenteCorrente);
    }

    public List<Fan> getListaFan(){
        return this.listaPartecipanti;
    }

    public void recensisci(String commento, int voto, Utente fan){
        Recensione recensione = new Recensione(commento, voto, fan);
        this.listaRecensioni.add(0, recensione);
    }

    public boolean hasPartecipante(Fan fan){
        return this.listaPartecipanti.contains(fan);
    }

    @Override
    public String toString() {
        return "Evento [id=" + id + ", titolo=" + titolo + ", descrizione=" + descrizione + ", durata=" + durata
                + ", data=" + data + ", ora=" + ora + ", brani="+listaBrani + ", partecipanti=" + listaPartecipanti.size() + "]" ;
    }

}
