package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;

public class Brano {
    private String titolo;
    private String album;
    private int durata;
    private Artista artista;

     public Brano(String titolo, String album, int durata, Artista artista) {
        this.titolo = titolo;
        this.album = album;
        this.durata = durata;
        this.artista = artista;
    }
    

    @Override
    public String toString() {
        return "Brano [titolo=" + titolo + ", album=" + album + ", durata=" + durata + ", artista=" +artista + "]";
    }


}
