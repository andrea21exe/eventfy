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
    private Invito invitoCorrente;

    private List<Impianto> listaImpianti;
    private Map<Integer, Impianto> mappaImpiantiTemp;
    private Map<Integer, Prenotazione> mappaPrenotazioniTemp;

    private Map<Integer, Utente> mappaUtenti;

    //UC2-UC3
    private Map<Integer, Prenotazione> mappaPrenotazioniPendenti;
    private Map<Integer, Prenotazione> mappaPrenotazioniAccettate;

    //UC6-UC7
    private Map<Integer, Prenotazione> mappaPrenotazioniArtistaAccettateTemp;
    private Map<Integer, Utente> mappaUtentiTemp;
    private Map<Integer, Invito> mappaInviti;

    // Singleton
    private Eventfy() {
        listaImpianti = new ArrayList<Impianto>();
        mappaUtenti = new HashMap<Integer, Utente>();
        mappaPrenotazioniAccettate = new HashMap<Integer, Prenotazione>();
        mappaPrenotazioniPendenti = new HashMap<Integer, Prenotazione>();
        mappaInviti = new HashMap<Integer, Invito>();
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
                impiantoCorrente); // inizialmente impianto corrente è null

        mappaImpiantiTemp = new HashMap<Integer, Impianto>();

        for (Impianto impianto : listaImpianti) {
            if (impianto.maggioreUgualeDi(capienza_min)) {
                mappaImpiantiTemp.put(impianto.getId(), impianto);
            }
        }

        return new ArrayList<Impianto>(mappaImpiantiTemp.values());

    }

    public void prenotaImpianto(int codice_impianto) {
        Impianto impianto = mappaImpiantiTemp.get(codice_impianto);
        prenotazioneCorrente.setImpianto(impianto);
    }

    public Prenotazione confermaPrenotazione() {
        int codice_prenotazione = prenotazioneCorrente.getId();
        mappaPrenotazioniPendenti.put(codice_prenotazione, prenotazioneCorrente);
        mappaImpiantiTemp = null;
        prenotazioneCorrente = null;
        return mappaPrenotazioniPendenti.get(codice_prenotazione);
    }

    public List<Prenotazione> mostraPrenotazioniPendenti() {

        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();
        for (int key : mappaPrenotazioniPendenti.keySet()) {
            Prenotazione p = mappaPrenotazioniPendenti.get(key);
            if (p.hasGestore((Gestore) utenteCorrente)) {
                mappaPrenotazioniTemp.put(p.getId(), p);
            }
        }

        return new ArrayList<Prenotazione>(mappaPrenotazioniTemp.values());
    }

    public Prenotazione selezionaPrenotazionePendente(int codice_prenotazione) {
        prenotazioneCorrente = mappaPrenotazioniTemp.get(codice_prenotazione);
        return prenotazioneCorrente;
    }

    public void accettaPrenotazione() {
        int codice_prenotazione = prenotazioneCorrente.getId();
        mappaPrenotazioniAccettate.put(codice_prenotazione, prenotazioneCorrente);
        mappaPrenotazioniPendenti.remove(codice_prenotazione);
        mappaPrenotazioniTemp = null;
        prenotazioneCorrente = null;
    }

    public Impianto getImpiantoCorrente() {
        return impiantoCorrente;
    }

    public void setUtenteCorrente(Utente utente) {
        utenteCorrente = utente;
    }

    public List<Impianto> getListaImpianti() {
        return listaImpianti;
    }

    public void setImpiantoCorrente(Impianto impianto) {
        impiantoCorrente = impianto;
    }

    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    public Prenotazione getPrenotazioneCorrente() {
        return prenotazioneCorrente;
    }

    public void setPrenotazioneCorrente(Prenotazione prenotazione) {
        prenotazioneCorrente = prenotazione;
    }

    public Map<Integer, Prenotazione> getPrenotazioniPendenti() {
        return mappaPrenotazioniPendenti;
    }

    public Map<Integer, Prenotazione> getPrenotazioniAccettate() {
        return mappaPrenotazioniAccettate;
    }

    public Map<Integer, Invito> getInviti() {
        return mappaInviti;
    }

    public void logIn(int id) {
        utenteCorrente = mappaUtenti.get(id);
    }

    public Map<Integer, Prenotazione> getMapPrenotazioniTemp() {
        return mappaPrenotazioniTemp;
    }

    // EFFETTUA LA REGISTRAZIONE ED IL "LOG-IN"
    public void signUpLogIn(Utente utente) {
        mappaUtenti.put(utente.getId(), utente);
        utenteCorrente = utente;
    }

    public Impianto getImpianto(int id) {
        for (Impianto i : listaImpianti) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    // POPOLA LE MAPPE/LISTE
    public void populate() {

        // Popola la mappa degli utenti
        Gestore g1 = new Gestore("Gigi");
        mappaUtenti.put(g1.getId(), g1);
        Gestore g2 = new Gestore("Paolo");
        mappaUtenti.put(g2.getId(), g2);
        Gestore g3 = new Gestore("Marco");
        mappaUtenti.put(g3.getId(), g3);
        Artista a1 = new Artista("THEWEEKND");
        mappaUtenti.put(a1.getId(), a1);
        Artista a2 = new Artista("WillIAm");
        mappaUtenti.put(a2.getId(), a2);
        Artista a3 = new Artista("DojaCat");
        mappaUtenti.put(a3.getId(), a3);

        // Popola la lista impianti
        Impianto i1 = new Impianto("San Siro", "Milano", 75000, 700, g1);
        listaImpianti.add(i1);
        Impianto i2 = new Impianto("Pal1", "Catania", 3000, 200, g3);
        listaImpianti.add(i2);
        Impianto i3 = new Impianto("Stadio Arena", "Roma", 20000, 500, g2);
        listaImpianti.add(i3);
        Impianto i4 = new Impianto("Ippodromo C", "Bari", 45000, 1000, g1);
        listaImpianti.add(i4);

        // Popola le prenotazioni
        Prenotazione p0 = new Prenotazione("P0", "d0", 20, LocalDate.now(), LocalTime.now(), a3, i1);
        Prenotazione p1 = new Prenotazione("P1", "d0", 203, LocalDate.now(), LocalTime.now(), a1, i2);
        Prenotazione p2 = new Prenotazione("P2", "d2", 120, LocalDate.now(), LocalTime.now(), a2, i3);
        Prenotazione p3 = new Prenotazione("P3", "d3", 130, LocalDate.now(), LocalTime.now(), a1, i4);
        mappaPrenotazioniPendenti.put(p0.getId(), p0);
        mappaPrenotazioniPendenti.put(p1.getId(), p1);
        mappaPrenotazioniAccettate.put(p2.getId(), p2);
        mappaPrenotazioniAccettate.put(p3.getId(), p3);

    }

    //Uguale al metodo "mostraprenotazioniaccettate" sotto
    public List<Prenotazione> aggiungiScaletta() {

        mappaPrenotazioniAccettate = new HashMap<Integer, Prenotazione>();

        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p = mappaPrenotazioniAccettate.get(key);
            if (utenteCorrente instanceof Artista) {

                if (p.hasArtista((Artista) utenteCorrente)) {
                    mappaPrenotazioniArtistaAccettateTemp.put(p.getId(), p);
                }
            }
        }

        return new ArrayList<>(mappaPrenotazioniArtistaAccettateTemp.values());
    }

    // ho sostituito la seconda operazione aggiungiScaletta2 con questa
    public List<Brano> recuperaBraniArtista(int codice_prenotazione) {

        prenotazioneCorrente = mappaPrenotazioniArtistaAccettateTemp.get(codice_prenotazione);

        if (utenteCorrente instanceof Artista) {
            //L'utente corrente è già quello della prenotazione corrente 
            //utenteCorrente = prenotazioneCorrente.getArtista();
            return ((Artista) utenteCorrente).getListaBrani();
        }

        mappaPrenotazioniArtistaAccettateTemp = null;

        return null;
    }

    // supponiamo di visualizzare la mappa dei brani con i rispettivi codici e di
    // scegliere un codice di un brano
    public void aggiungiBrano(int codice_brano) {

        if (utenteCorrente instanceof Artista) {

            prenotazioneCorrente.addBrano(((Artista)utenteCorrente).getBrano(codice_brano));
            /*
            Brano b = ((Artista) utenteCorrente).getMappaBrani().get(codice_brano);
            Evento e = prenotazioneCorrente.getEvento();
            e.addBrano(b);
            */
        }

    }

    //CI SERVE UN MODO PER METTERE A NULL LA PRENOTAZIONE CORRENTE UNA VOLTA CONCLUSO L'UC6
    public void setPrenotazioneCorrenteNull(){
        prenotazioneCorrente = null;
    }



    //Uguale al metodo "aggiungiscaletta" sopra. In astah abbiamo chiamato questo metodo "invitaArtista"
    public List<Prenotazione> mostraPrenotazioniAccettate() {

        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();

        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p = mappaPrenotazioniAccettate.get(key);
            if (p.hasArtista((Artista) utenteCorrente)) {
                mappaPrenotazioniTemp.put(p.getId(), p);
            }
        }

        return new ArrayList<Prenotazione>(mappaPrenotazioniTemp.values());

    }

    public List<Utente> SelezionaPrenotazioneInvito(int codice_prenotazione) {

        //Non dobbiamo creare l'associazione tra evento e sistema. La prenotazione ci fa da tramite.
        //Costruisco l'oggetto invito direttamente qui, nella successiva funzione gli associerò -->
        // --> l'artista invitato e lo inserirò nella mappaInviti

        invitoCorrente = new Invito(mappaPrenotazioniTemp.get(codice_prenotazione).getEvento(), (Artista)utenteCorrente);
        mappaUtentiTemp = new HashMap<Integer, Utente>(); 

        for (Utente utente : mappaUtenti.values()) {
            if (utente instanceof Artista) {
                mappaUtentiTemp.put(utente.getId(), utente);
            }
        }

        mappaPrenotazioniTemp = null;
        return new ArrayList<Utente>(mappaUtentiTemp.values());

    }

    public void invitaArtista(int codice_artista) {

        invitoCorrente.setDestinatario((Artista)mappaUtentiTemp.get(codice_artista));
        mappaInviti.put(invitoCorrente.getId(), invitoCorrente);

        invitoCorrente = null;
        mappaUtentiTemp = null;

    }

}
