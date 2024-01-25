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
        listaEventi.add(0, e);
    }

    public List<Evento> getListaEventi() {
        return this.listaEventi;
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

}
