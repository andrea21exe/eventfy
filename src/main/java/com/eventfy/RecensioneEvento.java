package com.eventfy;

public class RecensioneEvento extends Recensione{

    private Fan fan;

    public RecensioneEvento(String commento, int voto, Fan fan) throws Exception{
        super(commento, voto);
        this.fan = fan;
    }

    public boolean hasFan(Fan fan){
        return this.fan.equals(fan);
    }

    @Override
    public Utente getUtente() {
        return this.fan;
    }

}
