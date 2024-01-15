package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;

public class Brano {
    private String titolo;
    private String album;
    private int durata;

     public Brano(String titolo, String album, int durata) {
        this.titolo = titolo;
        this.album = album;
        this.durata = durata;
    }
    

    @Override
    public String toString() {
        return "Brano [titolo=" + titolo + ", album=" + album + ", durata=" + durata + "]";
    }


}
