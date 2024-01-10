package com.eventfy;
import java.util.HashMap;
import java.util.Map;

public class Eventfy {

    private static Eventfy sistema; //Singleton

    private Utente utenteCorrente; 
    private Impianto impiantoCorrente;

    private Map<Integer, Impianto> mappaImpianti;
    private Map<Integer, Utente> mappaUtenti;
    

    //Singleton
    private Eventfy(){
        mappaImpianti = new HashMap<Integer, Impianto>();
        mappaUtenti = new HashMap<Integer, Utente>();
    }

    public Eventfy getIstanceEventfy(){
        if(sistema == null){
            sistema = new Eventfy();
        }
        return sistema;
    }
    //-------

    public Impianto nuovoImpianto(String nome, String luogo, int capienza, int superficie){
        impiantoCorrente = new Impianto(nome, luogo, capienza, superficie, utenteCorrente);
        return impiantoCorrente;
    }

    public void confermaImpianto(){
        mappaImpianti.put(impiantoCorrente.getId(), impiantoCorrente);
        impiantoCorrente = null;
    }

}
