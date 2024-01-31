package com.eventfy;

public class RecensioneEvento extends Recensione{

    private Fan fan;

    public RecensioneEvento(String commento, int voto, Fan fan) {
        super(commento, voto);
        this.fan = fan;
    }


    @Override
    public Utente getUtente() {
        return this.fan;
    }
}
