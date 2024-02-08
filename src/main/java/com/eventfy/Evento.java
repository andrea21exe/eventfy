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
    private List<Invito> listaInviti;
    private Invito invito;

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
        this.listaInviti = new ArrayList<Invito>();
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
    
    public LocalTime getOra(){
        return ora;
    }

    public void addFan(Fan utenteCorrente) throws Exception{
        if(this.listaPartecipanti.contains(utenteCorrente)){
            throw new Exception("Utente già partecipante");
        }
        this.listaPartecipanti.add(utenteCorrente);
        utenteCorrente.addPartecipazione(this);
    }

    public List<Fan> getListaFan() {
        return this.listaPartecipanti;
    }

    public void recensisci(String commento, int voto, Fan fan) throws Exception{
        if(fanHaRecensito(fan)){
            throw new Exception("L'evento è già stato recensito dal fan corrente");
        }
        RecensioneEvento recensione = new RecensioneEvento(commento, voto, fan);
        this.listaRecensioni.add(0, recensione);
    }

    public boolean hasPartecipante(Fan fan) {
        return this.listaPartecipanti.contains(fan);
    }

    public boolean hasBrano(int id){
        for(Brano b : listaBrani){
            if(b.hasId(id)){
                return true;
            }
        }
        return false;
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

    public List<RecensioneEvento> getListaRecensioni() {
        return listaRecensioni;
    }

    public void creaInvito(Artista artistaMittente){
        this.invito = new Invito(this, artistaMittente);
    }

    public void setDestinatarioInvito(Artista artista){
        this.invito.setDestinatario(artista);
        this.invito = null;
    }

    public List<Invito> getInvitiAccettati(){
        return this.listaInviti;
    }
    
    public void addInvito(Invito in){
        listaInviti.add(in);
    }

    public boolean isScaduto(){
        LocalDate dataScadenza = this.getData().minusMonths(3);
        return LocalDate.now().isAfter(dataScadenza);
    }

    @Override
    public String toString() {
        return "-- Evento [id=" + id + ", titolo=" + titolo + ", descrizione=" + descrizione + ", durata=" + durata
                + ", data=" + data + ", ora=" + ora +", partecipanti="+ listaPartecipanti.size() +  ", \nbrani=" + listaBrani + "]";
    }

    private boolean fanHaRecensito(Fan fan){
        for(RecensioneEvento recensione : this.listaRecensioni){
            if(recensione.hasFan(fan)){
                return true;
            }
        }

        return false;
    }

}
