package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import scala.Array;

public class Evento {

    private final int id;
    private String titolo;
    private String descrizione;
    private int durata;
    private LocalDate data;
    private LocalTime ora;
    private List<Brano> listaBrani;

    public Evento(int id, String titolo, String descrizione, int durata, LocalDate data, LocalTime ora) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.durata = durata;
        this.data = data;
        this.ora = ora;
        this.listaBrani = new ArrayList<Brano>();
    }


    public void addBrano(Brano b){
        listaBrani.add(b);
    }

    public int getId(){
        return this.id;
    }

    public List<Brano> getListaBrani(){
        return listaBrani;
    }

    @Override
    public String toString() {
        return "Evento [id=" + id + ", titolo=" + titolo + ", descrizione=" + descrizione + ", durata=" + durata
                + ", data=" + data + ", ora=" + ora + "brani="+listaBrani +"]";
    }

}
