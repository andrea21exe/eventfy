package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Impianto implements Recensibile {

    private static int currentId = 0;

    private final int id;
    private String nome;
    private String luogo;
    private int capienza;
    private int superficie;
    private Gestore gestore;
    private List<Recensione> listaRecensioni;

    public Impianto(String nome, String luogo, int capienza, int superficie, Utente gestore) {

        if (gestore instanceof Gestore) {
            this.nome = nome;
            this.luogo = luogo;
            this.capienza = capienza;
            this.superficie = superficie;
            this.gestore = (Gestore) gestore;
            this.id = currentId++;
            this.listaRecensioni = new ArrayList<Recensione>();
        } else {
            throw new IllegalArgumentException("Il gestore inserito non è di tipo Gestore.");
        }
    }

    public int getId() {
        return this.id;
    }

    public int getCapienza() {
        return this.capienza;
    }

    public boolean maggioreUgualeDi(int capienza) {
        if (this.capienza >= capienza) {
            return true;
        }
        return false;
    }

    public Gestore getGestore() {
        return this.gestore;
    }

    /*
     * public void addRecensione(Recensione rec){
     * listaRecensioni.add(rec);
     * }
     */

    public List<Recensione> getListaRecensioni() {
        return this.listaRecensioni;
    }

    public void recensisci(String commento, int voto, Utente artista) {
        Recensione recensione = new Recensione(commento, voto, artista);
        listaRecensioni.add(0, recensione);
    }

    @Override
    public String toString() {
        return "Impianto [id=" + id + ", nome=" + nome + ", luogo=" + luogo + ", capienza=" + capienza + ", superficie="
                + superficie + ", gestore=" + gestore.toString() + "]";
    }

}
