package com.eventfy;

import java.util.ArrayList;
import java.util.List;

public class Fan extends Utente {

    List<Evento> listaEventi;

    public Fan(String nome) {
        super(nome);
        this.listaEventi = new ArrayList<Evento>();

    }

    public void addPartecipazione(Evento e) {
        listaEventi.add(e);
    }

    public List<Evento> getListaEventi() {
        return this.listaEventi;
    }

    public List<Evento> getListaEventiRecensibili(){
        List<Evento> listaEventiRecensibili = new ArrayList<Evento>();

        for(Evento evento : this.listaEventi){
            if(evento.isRecensibile()){
                listaEventiRecensibili.add(evento);
            }
        }

        return listaEventiRecensibili;
    }

    public boolean isPartecipante(Evento e) {
        return this.listaEventi.contains(e);
    }

    public boolean isPartecipante(int id_evento) {
        for (Evento evento : listaEventi) {
            if (evento.getId() == id_evento) {
                return true;
            }
        }
        return false;
    }

    public void printRecensioni(){

        if(this.listaEventi.isEmpty()){
            System.out.println("Nessuna recensione");
            return;
        }

        boolean haRecensito = false;
        for(Evento e : this.listaEventi){
            if(e.fanHaRecensito(this)){
                haRecensito = true;
            }
        }

        if(!haRecensito){
            System.out.println("Nessuna recensione");
            return;
        }

        for(Evento e : this.listaEventi){
            e.printRecensioniDi(this);
        }
    }

}
