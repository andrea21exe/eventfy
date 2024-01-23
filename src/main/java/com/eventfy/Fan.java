package com.eventfy;

import java.util.ArrayList;
import java.util.List;

public class Fan extends Utente{

    List<Evento> listaEventi;

    public Fan(String nome) {
        super(nome);
        this.listaEventi = new ArrayList<Evento>();
        
    }

    public void addEvento(Evento e){
        listaEventi.add(e);
    }

    public List<Evento> getListaEventi(){
        return this.listaEventi;
    }
    
}
