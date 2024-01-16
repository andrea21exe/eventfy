package com.eventfy;

import java.util.List;
import java.util.Map;

public class Artista extends Utente{

    private Map<Integer, Brano> mappaBrani;

    public Artista(String nome){
        super(nome);
    }
    
    public Artista(String nome, Map<Integer, Brano> mappaBrani){
        super(nome);
        this.mappaBrani = mappaBrani;
    }

    public Map<Integer, Brano> getMappaBrani(){
        return this.mappaBrani;
    }

    @Override
    public String toString() {
        return "Artista [ nome= " + nome + "mappaBrani=" + mappaBrani + "]";
    }

    
}
