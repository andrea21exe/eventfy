package com.eventfy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Eventfy {

    private static Eventfy sistema; // Singleton

    private Utente utenteCorrente;
    private Impianto impiantoCorrente;
    private Prenotazione prenotazioneCorrente;
    private Gestore gestoreCorrente;

    private List<Impianto> listaImpianti;
    private List<Prenotazione> listaPrenotazioni;
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

    public static Eventfy getIstanceEventfy() {
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

        prenotazioneCorrente = new Prenotazione(titolo, descrizione, durata, data, ora, utenteCorrente,
                impiantoCorrente);

        mappaImpiantiTemp = new HashMap<Integer, Impianto>();


        for (Impianto impianto : listaImpianti) {
            if (impianto.maggioreUgualeDi(capienza_min)) {
                mappaImpiantiTemp.put(impianto.getId(), impianto);
            }
        }

        return new ArrayList<>(mappaImpiantiTemp.values());

    }

    public void prenotaImpianto(int codice_impianto) {
        prenotazioneCorrente.setImpianto(mappaImpiantiTemp.get(codice_impianto));
    }

    public void confermaPrenotazione() {
        int codice_prenotazione = prenotazioneCorrente.getId();
        mappaPrenotazioniPendenti.put(codice_prenotazione, prenotazioneCorrente);
        mappaImpiantiTemp = null;
    }

    public List<Prenotazione> mostraPrenotazioniPendenti() {
        for (int key : mappaPrenotazioniPendenti.keySet()) {
            prenotazioneCorrente = mappaPrenotazioniPendenti.get(key);

            impiantoCorrente = prenotazioneCorrente.getImpianto();

            if (gestoreCorrente.equals(impiantoCorrente.getGestore())) {
                listaPrenotazioni.add(prenotazioneCorrente);
            }
        }

        return listaPrenotazioni;
    }

    public Prenotazione selezionaPrenotazionePendente(int codice_prenotazione) {
        prenotazioneCorrente = mappaPrenotazioniPendenti.get(codice_prenotazione);
        return prenotazioneCorrente;
    }

    public void accettaPrenotazione() {
        int codice_prenotazione = prenotazioneCorrente.getId();
        mappaPrenotazioniAccettate.put(codice_prenotazione, prenotazioneCorrente);
        mappaPrenotazioniPendenti.remove(codice_prenotazione);
    }

    public Impianto getImpiantoCorrente() {
        return impiantoCorrente;
    }

    public void setUtenteCorrente(Utente utente){
        utenteCorrente = utente; 
    }

    public List<Impianto> getListaImpianti(){
        return listaImpianti;
    }

    public void setImpiantoCorrente(Impianto impianto){
        impiantoCorrente = impianto;
    }

    public Utente getUtenteCorrente(){
        return utenteCorrente;
    }

    public Prenotazione getPrenotazioneCorrente(){
        return prenotazioneCorrente;
    }

    public void setPrenotazioneCorrente(Prenotazione prenotazione){
        prenotazioneCorrente = prenotazione;
    }
}
