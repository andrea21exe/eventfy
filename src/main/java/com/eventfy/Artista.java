package com.eventfy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Artista extends Utente {

    private Map<Integer, Brano> mappaBrani;
    private Map<Integer, Invito> mappaInvitiPendenti;
    private Map<Integer, Invito> mappaInvitiAccettati;
    //aggiunta dall'estensione
    private Map<Integer,Invito> mappaInvitiRifiutati;

    public Artista(String nome) {
        super(nome);
        this.mappaBrani = new HashMap<Integer, Brano>();
        this.mappaInvitiPendenti = new HashMap<Integer, Invito>();
        this.mappaInvitiAccettati = new HashMap<Integer, Invito>();
        //aggiunta dall'estensione
        this.mappaInvitiRifiutati=new HashMap<Integer, Invito>();
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

    public void addBranoAdEvento(int id_brano, Evento e) throws Exception{
        Brano b = mappaBrani.get(id_brano);
        if(b == null){
            throw new Exception("Id brano non valido");
        }
        e.addBrano(b);
    }

    public Map<Integer, Brano> getMappaBrani() {
        return mappaBrani;
    }

    public String getNome() {
        return super.getNome();
    }

    public void addInvitoPendente(Invito inv) {
        this.mappaInvitiPendenti.put(inv.getId(), inv);
    }

    public List<Invito> getListaInvitiPendenti() {
        return new ArrayList<Invito>(this.mappaInvitiPendenti.values());
    }

    public void accettaInvito(int id) throws Exception{
        if(!mappaInvitiPendenti.containsKey(id)){
            throw new Exception("Id invito non valido");
        }
        Invito invitoAccettato = this.mappaInvitiPendenti.remove(id);
        this.mappaInvitiAccettati.put(invitoAccettato.getId(), invitoAccettato);
        invitoAccettato.addInvitoEvento();
    }

    //aggiunta dall'estensione
    public void rifiutaInvito(int id) throws Exception{
        if(!mappaInvitiPendenti.containsKey(id)){
            throw new Exception("Id invito non valido");
        }
        Invito invitoRifiutato = this.mappaInvitiPendenti.remove(id);
        this.mappaInvitiRifiutati.put(invitoRifiutato.getId(), invitoRifiutato);
    }

    public List<Invito> getListaInvitiAccettati() {
        return new ArrayList<Invito>(this.mappaInvitiAccettati.values());
    }

    public List<Invito> getListaInvitiRifiutati() {
        return new ArrayList<Invito>(this.mappaInvitiRifiutati.values());
    }


    public Invito getInvitoPendente(int id) {
        return this.mappaInvitiPendenti.get(id);
    }


}
