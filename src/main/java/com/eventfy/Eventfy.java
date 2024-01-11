package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class Eventfy {

    private static Eventfy sistema; // Singleton

    private Utente utenteCorrente;
    private Impianto impiantoCorrente;
    private Prenotazione prenotazioneCorrente;

    private List<Impianto> listaImpianti;
    private Map<Integer, Impianto> mappaImpiantiTemp;

    private Map<Integer, Utente> mappaUtenti;

    private Map<Integer, Prenotazione> mappaPrenotazioniPendenti;
    private Map<Integer, Prenotazione> mappaPrenotazioniAccettate;

    // Singleton
    private Eventfy() {
        listaImpianti = new ArrayList<Impianto>();
        mappaUtenti = new HashMap<Integer, Utente>();
        mappaPrenotazioniAccettate = new HashMap<Integer, Prenotazione>();
        mappaPrenotazioniPendenti = new HashMap<Integer, Prenotazione>();
    }

    public Eventfy getIstanceEventfy() {
        if (sistema == null) {
            sistema = new Eventfy();
        }
        return sistema;
    }
    // -------

    public Impianto nuovoImpianto(String nome, String luogo, int capienza, int superficie) {
        impiantoCorrente = new Impianto(nome, luogo, capienza, superficie, utenteCorrente);
        return impiantoCorrente;
    }

    public void confermaImpianto() {
        listaImpianti.add(impiantoCorrente);
        impiantoCorrente = null;
    }

    public List<Impianto> nuovaPrenotazione(String titolo, String descrizione, int durata, int capienza_min,
            LocalDate data, LocalTime ora) {

        prenotazioneCorrente = new Prenotazione(titolo, descrizione, durata, data, ora, utenteCorrente);

        for (Impianto impianto : listaImpianti) {
            if (impianto.maggioreUgualeDi(capienza_min)) {
                mappaImpiantiTemp.put(impianto.getId(), impianto);
            }
        }

        return new ArrayList<>(mappaImpiantiTemp.values());

    }

}
