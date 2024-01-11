package com.eventfy;
public  class Utente {
    
    private static int currentId = 0;

    private int id;
    private String nome;

    public Utente(String nome){
        this.nome = nome;
        this.id = currentId++;
    }

    @Override
    public String toString() {
        return "Utente [id=" + id + ", nome=" + nome + "]";
    }

}
