package com.eventfy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Artista extends Utente{

    private Map<Integer, Brano> mappaBrani;
    
    public Artista(String nome){
        super(nome);
        this.mappaBrani = new HashMap<Integer, Brano>();
    }

    public Artista(String nome, Map<Integer, Brano> mappaBrani){
        super(nome);
        this.mappaBrani = mappaBrani;
    }

    public List<Brano> getListaBrani(){
        return new ArrayList<Brano>(this.mappaBrani.values());
    }

    public Brano getBrano(int codice_brano){
        return mappaBrani.get(codice_brano);
    }

    @Override
    public String toString() {
        return "Artista [ nome= " + nome + "]";
    }

    
}
