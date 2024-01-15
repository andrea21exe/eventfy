package com.eventfy;

import java.util.List;

public class Artista extends Utente{

    private List<Brano> listaBrani;
    
    public Artista(String nome, List<Brano> listaBrani){
        super(nome);
        this.listaBrani = listaBrani;
    }

    public List<Brano> getListaBrani(){
        return this.listaBrani;
    }

    @Override
    public String toString() {
        return "Artista [ nome= " + nome + "listaBrani=" + listaBrani + "]";
    }

    
}
