package com.eventfy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Artista extends Utente {

    private Map<Integer, Brano> mappaBrani;
    private Map<Integer, Invito> mappaInvitiPendenti;
    private Map<Integer, Invito> mappaInvitiAccettati;

    public Artista(String nome) {
        super(nome);
        this.mappaBrani = new HashMap<Integer, Brano>();
        this.mappaInvitiPendenti = new HashMap<Integer, Invito>();
    }

    public Artista(String nome, Map<Integer, Brano> mappaBrani) {
        super(nome);
        this.mappaBrani = mappaBrani;
    }

    public List<Brano> getListaBrani() {
        return new ArrayList<Brano>(this.mappaBrani.values());
    }

    public Brano getBrano(int codice_brano) {
        return mappaBrani.get(codice_brano);
    }

    public void setMappaBrani(List<Brano> lista) {

        mappaBrani = new HashMap<Integer, Brano>();

        for (Brano b : lista) {
            mappaBrani.put(b.getId(), b);
        }
    }

    public void nuovoBrano(String titolo, String album, int durata) {
        Brano b = new Brano(titolo, album, durata);
        this.mappaBrani.put(b.getId(), b);
    }

    public Map<Integer, Brano> getMappaBrani() {
        return mappaBrani;
    }

    public String getNome() {
        return super.getNome();
    }

    public void addInvitoPendente(Invito inv){
        this.mappaInvitiPendenti.put(inv.getId(), inv);
    }
    
    public List<Invito> getListaInvitiPendenti() {
        return new ArrayList<Invito>(this.mappaInvitiPendenti.values());
    }

    public void addInvitoAccettato(Invito inv){
        this.mappaInvitiAccettati.put(inv.getId(), inv);
    }
    
    public List<Invito> getListaInvitiAccettati() {
        return new ArrayList<Invito>(this.mappaInvitiAccettati.values());
    }

    public Invito getInvitoPendente(int id){

        return this.mappaInvitiPendenti.get(id);
    }

    public void EliminaInvitoPendente(int id){
        mappaInvitiPendenti.remove(id);
    }

}
