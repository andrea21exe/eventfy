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
    private boolean impiantoRecensito;

    public Prenotazione(String titolo, String descrizione, int durata, LocalDate data, LocalTime ora, Artista artista,
            Impianto impianto) {
            this.id = currentId++;
            this.data = LocalDate.now();
            this.ora = LocalTime.now();
            this.evento = new Evento(id, titolo, descrizione, durata, data, ora);
            this.impianto = impianto;
            this.artista = artista;
            this.impiantoRecensito = false;
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

    public void addBrano(int id_brano) throws Exception {
        if(this.evento.hasBrano(id_brano)){
            throw new Exception("Brano già inserito");
        }
        this.artista.addBranoAdEvento(id_brano, this.evento);
    }

    public boolean hasBrano(int id_brano){
        return evento.hasBrano(id_brano);
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

    public void addPartecipazioneFan(Fan fan) throws Exception{
        this.evento.addFan(fan);
    }

    public void creaRecensione(String commento, int voto) throws Exception{

        if(this.impiantoRecensito == true){
            throw new Exception("E' già stato recensito l'impianto per questa prenotazione");
        }

        this.impianto.recensisci(commento, voto, this.artista);
        this.impiantoRecensito = true;
    }

    public void creaRecensione(String commento, int voto, Fan fan) throws Exception{
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

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOra(){
        return ora;
    }

    public void creaInvito(){
        this.evento.creaInvito(this.artista);
    }

    public void setDestinatarioInvito(Artista artista)throws Exception{
        
        if(artista.isInvitato(this.evento)){
            throw new Exception("Artista già invitato");
        }
        this.evento.setDestinatarioInvito(artista);
        
    }

    public boolean hasImpianto(Impianto im){
        return this.impianto.equals(im);
    }

    public boolean hasDataEvento(LocalDate data){
        return this.evento.getData().equals(data);
    }

    public LocalDate getDataEvento(){
        return this.evento.getData();
    }

    public boolean hasEventoScaduto(){
        return this.evento.isScaduto();
    }

    public List<Invito> getInvitiAccettati(){
        return this.evento.getInvitiAccettati();
    }

    public void printEvento(){
        System.out.println(this.evento);
    }

    public void printRecensioniEvento(){
        this.evento.printRecensioniEvento();
    }

    public void printInvitiAccettati(){
        this.evento.printInvitiAccettati();
    }

    @Override
    public String toString() {
        return "-- Prenotazione [id=" + id + ", data=" + data + ", ora=" + ora + ", \nevento=" + evento + ",\nartista="
                + artista.getNome() + ", \nimpianto=" + impianto + "]";
    }

    public String toStringEventInfo(){
        return "Evento: id= " + id + ", data evento= " + evento.getData() + ", ora evento= " + evento.getOra() + ", impianto: " + impianto.getNome();
    }

}
