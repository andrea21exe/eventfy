package com.eventfy;

public class RecensioneImpianto extends Recensione {

    private Artista artista;

    public RecensioneImpianto(String commento, int voto, Artista artista) throws Exception{
        super(commento, voto);
        this.artista = artista;
    }

    @Override
    public Utente getUtente() {
        return this.artista;
    }
}
