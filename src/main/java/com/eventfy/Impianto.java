package com.eventfy;

public class Impianto {

    private static int currentId = 0;

    private final int id;
    private String nome;
    private String luogo;
    private int capienza;
    private int superficie;
    private Gestore gestore;

    public Impianto(String nome, String luogo, int capienza, int superficie, Utente gestore) {

        if (gestore instanceof Gestore) {
            this.nome = nome;
            this.luogo = luogo;
            this.capienza = capienza;
            this.superficie = superficie;
            this.gestore = (Gestore) gestore;
            this.id = currentId++;
        } else {
            throw new IllegalArgumentException("Il gestore inserito non è di tipo Gestore.");
        }
    }

    public int getId() {
        return this.id;
    }

    public int getCapienza() {
        return this.capienza;
    }

    public boolean maggioreUgualeDi(int capienza) {
        if (capienza >= this.capienza) {
            return true;
        }
        return false;
    }

    public Gestore getGestore(){
        return this.gestore;
    }

    @Override
    public String toString() {
        return "Impianto [id=" + id + ", nome=" + nome + ", luogo=" + luogo + ", capienza=" + capienza + ", superficie="
                + superficie + ", gestore=" + gestore.toString() + "]";
    }

}
