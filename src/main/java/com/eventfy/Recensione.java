package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Recensione {
    private static int currentId = 0;

    private int id;
    private LocalDate dataCommento;
    private LocalTime oraCommento;
    private String commento;
    private int voto;

    public Recensione(String commento, int voto) throws Exception {

        if (voto < 0 || voto > 5) {
            throw new Exception("Il valore deve essere compreso tra 0 a 5");
        }

        this.dataCommento = LocalDate.now();
        this.oraCommento = LocalTime.now();
        this.commento = commento;
        this.id = currentId++;
        this.voto = voto;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Recensione [id=" + id + ", dataCommento=" + dataCommento + ", oraCommento=" + oraCommento
                + ", commento=" + commento + ", voto=" + voto + "]";
    }

    public String getCommento() {
        return this.commento;
    }

    public int getVoto() {
        return this.voto;
    }

    public LocalDate getData() {
        return this.dataCommento;
    }

    public abstract Utente getUtente();
}
