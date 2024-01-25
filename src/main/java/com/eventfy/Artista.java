package com.eventfy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Artista extends Utente {

    private Map<Integer, Brano> mappaBrani;

    public Artista(String nome) {
        super(nome);
        this.mappaBrani = new HashMap<Integer, Brano>();
    }

    public Artista(String nome, Map<Integer, Brano> mappaBrani) {
        super(nome);
        this.mappaBrani = mappaBrani;
    }

    public List<Brano> getListaBrani() {
        return new ArrayList<Brano>(this.mappaBrani.values());
    }

    public Brano getBrano(int codice_brano) {
        return mappaBrani.get(codice_brano);
    }

    public void setMappaBrani(List<Brano> lista) {

        mappaBrani = new HashMap<Integer, Brano>();

        for (Brano b : lista) {
            mappaBrani.put(b.getId(), b);
        }
    }

    public void nuovoBrano(String titolo, String album, int durata) {
        Brano b = new Brano(titolo, album, durata);
        this.mappaBrani.put(b.getId(), b);
    }

    public Map<Integer, Brano> getMappaBrani() {
        return mappaBrani;
    }

    public String getNome() {
        return super.getNome();
    }

}
