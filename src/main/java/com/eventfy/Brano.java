package com.eventfy;

public class Brano {
    private static int currentId = 0;

    private final int id;
    private String titolo;
    private String album;
    private int durata;

     public Brano(String titolo, String album, int durata) {
        this.titolo = titolo;
        this.album = album;
        this.durata = durata;
        this.id = currentId++;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public String toString() {
        return "Brano [id=" + id + ", titolo=" + titolo + ", album=" + album + ", durata=" + durata + "]";
    }

}
