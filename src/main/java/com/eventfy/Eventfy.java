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

    // UC2-UC3
    private Map<Integer, Prenotazione> mappaPrenotazioniPendenti;
    private Map<Integer, Prenotazione> mappaPrenotazioniAccettate;

    // UC6-UC7
    private Map<Integer, Utente> mappaUtentiTemp;
    private Map<Integer, Invito> mappaInvitiPendenti;

    // UC8
    private Map<Integer, Invito> mappaInvitiTemp;
    private Map<Integer, Invito> mappaInvitiAccettati;

    // UC9
    //Recensione recensioneCorrente;
    // UC12
    //private List<Evento> listaEventi;

    // Singleton
    private Eventfy() {
        listaImpianti = new ArrayList<Impianto>();
        mappaUtenti = new HashMap<Integer, Utente>();
        mappaPrenotazioniAccettate = new HashMap<Integer, Prenotazione>();
        mappaPrenotazioniPendenti = new HashMap<Integer, Prenotazione>();
        mappaInvitiPendenti = new HashMap<Integer, Invito>();
        mappaInvitiAccettati = new HashMap<Integer, Invito>();
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
        return this.listaImpianti;
    }

    public Utente getUtenteCorrente() {
        return this.utenteCorrente;
    }

    public Prenotazione getPrenotazioneCorrente() {
        return this.prenotazioneCorrente;
    }

    public Map<Integer, Prenotazione> getPrenotazioniPendenti() {
        return mappaPrenotazioniPendenti;
    }

    public Map<Integer, Prenotazione> getPrenotazioniAccettate() {
        return mappaPrenotazioniAccettate;
    }

    public Map<Integer, Invito> getInviti() {
        return mappaInvitiPendenti;
    }

    public void logIn(int id) {
        utenteCorrente = mappaUtenti.get(id);
    }

    public Map<Integer, Prenotazione> getMapPrenotazioniTemp() {
        return mappaPrenotazioniTemp;
    }

    public Utente getUtente(int id) {
        return mappaUtenti.get(id);
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

    // Simile al metodo "mostraprenotazioniaccettate" sotto
    // UC6-----------------------------------------------------
    public List<Prenotazione> aggiungiScaletta() {

        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();

        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p = mappaPrenotazioniAccettate.get(key);
            if (utenteCorrente instanceof Artista) {

                if (p.hasArtista((Artista) utenteCorrente)) {
                    mappaPrenotazioniTemp.put(p.getId(), p);
                }
            }
        }

        return new ArrayList<>(mappaPrenotazioniTemp.values());
    }

    // ho sostituito la seconda operazione aggiungiScaletta2 con questa
    public List<Brano> recuperaBraniArtista(int codice_prenotazione) {

        prenotazioneCorrente = mappaPrenotazioniTemp.get(codice_prenotazione);

        if (utenteCorrente instanceof Artista) {
            // L'utente corrente è già quello della prenotazione corrente
            // utenteCorrente = prenotazioneCorrente.getArtista();
            return ((Artista) utenteCorrente).getListaBrani();
        }

        mappaPrenotazioniTemp = null;
        return null;

    }

    // supponiamo di visualizzare la mappa dei brani con i rispettivi codici e di
    // scegliere un codice di un brano
    public void aggiungiBrano(int codice_brano) {
        if (utenteCorrente instanceof Artista) {
            prenotazioneCorrente.addBrano(((Artista) utenteCorrente).getBrano(codice_brano));
        }
    }

    // CI SERVE UN MODO PER METTERE A NULL LA PRENOTAZIONE CORRENTE UNA VOLTA
    // CONCLUSO L'UC6
    public void setPrenotazioneCorrenteNull() {
        prenotazioneCorrente = null;
    }

    public boolean hasArtistaCorrente() {
        return utenteCorrente instanceof Artista;
    }

    public boolean hasGestoreCorrente() {
        return utenteCorrente instanceof Gestore;
    }

    // UC7-------------------------------------------------------
    // SImile al metodo "aggiungiscaletta" sopra. In astah abbiamo chiamato questo
    // metodo "invitaArtista"
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

    public List<Utente> selezionaPrenotazioneInvito(int codice_prenotazione) {

        invitoCorrente = new Invito(mappaPrenotazioniTemp.get(codice_prenotazione).getEvento(),
                (Artista) utenteCorrente);
        mappaUtentiTemp = new HashMap<Integer, Utente>();

        for (Utente utente : mappaUtenti.values()) {
            if (utente instanceof Artista) {
                mappaUtentiTemp.put(utente.getId(), utente);
            }
        }

        return new ArrayList<Utente>(mappaUtentiTemp.values());

    }

    public void invitaArtista(int codice_artista) {

        invitoCorrente.setDestinatario((Artista) mappaUtentiTemp.get(codice_artista));
        mappaInvitiPendenti.put(invitoCorrente.getId(), invitoCorrente);

        invitoCorrente = null;
        mappaUtentiTemp = null;
        mappaPrenotazioniTemp = null;

    }

    public Invito getInvitoCorrente() {
        return this.invitoCorrente;
    }

    public Map<Integer, Invito> getMappaInvitiPendenti() {
        return mappaInvitiPendenti;
    }

    // UC8 ----------------------------------------------------
    public List<Invito> gestisciInvito() {

        mappaInvitiTemp = new HashMap<Integer, Invito>();

        for (int key : mappaInvitiPendenti.keySet()) {
            Invito i = mappaInvitiPendenti.get(key);
            if (i.hasDestinatario((Artista) utenteCorrente)) {
                mappaInvitiTemp.put(i.getId(), i);
            }

        }

        return new ArrayList<Invito>(mappaInvitiTemp.values());

    }

    // seleziono un id dalla mappa inviti relativa all'artista
    public Evento selezionaInvito(int codice_invito) {

        invitoCorrente = mappaInvitiTemp.get(codice_invito);
        return invitoCorrente.getEvento();

    }

    public void accettaInvito() {

        int codice_invito = invitoCorrente.getId();
        mappaInvitiPendenti.remove(codice_invito);
        mappaInvitiAccettati.put(codice_invito, invitoCorrente);
        invitoCorrente = null;
        mappaInvitiTemp = null;

    }

    public Map<Integer, Invito> getMappaInvitiAccettati() {
        return mappaInvitiAccettati;
    }

    // POPOLA LE MAPPE/LISTE
    public void populate() {

        // Popola la mappa degli utenti
        Gestore g1 = new Gestore("Gigi"); // id 0
        mappaUtenti.put(g1.getId(), g1);
        Gestore g2 = new Gestore("Paolo"); // id 1
        mappaUtenti.put(g2.getId(), g2);
        Gestore g3 = new Gestore("Marco"); // id 2
        mappaUtenti.put(g3.getId(), g3);
        Artista a1 = new Artista("THEWEEKND"); // id 3
        mappaUtenti.put(a1.getId(), a1);
        Artista a2 = new Artista("SZA"); // id 4
        mappaUtenti.put(a2.getId(), a2);
        Artista a3 = new Artista("DojaCat"); // id 5
        mappaUtenti.put(a3.getId(), a3);

        a1.nuovoBrano("Starboy", "Starboy", 3); // id 1
        a1.nuovoBrano("Sacrifice", "Starboy", 3); // id 2
        a1.nuovoBrano("Out Of Time", "Dawn FM", 3); // id 3
        a2.nuovoBrano("Good Days", "SOS", 3); // id 4
        a2.nuovoBrano("Kill Bill", "SOS", 3); // id 5
        a3.nuovoBrano("Agora Hills", "Album D", 3); // id 6

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
        Prenotazione p1 = new Prenotazione("P1", "d1", 203, LocalDate.now(), LocalTime.now(), a1, i2);
        Prenotazione p2 = new Prenotazione("P2", "d2", 120, LocalDate.now(), LocalTime.now(), a2, i3);
        Prenotazione p3 = new Prenotazione("P3", "d3", 130, LocalDate.now(), LocalTime.now(), a1, i4);
        Prenotazione p4 = new Prenotazione("P4", "d4", 130, LocalDate.now(), LocalTime.now(), a1, i4);
        Prenotazione p5 = new Prenotazione("P5", "d5", 130, LocalDate.now(), LocalTime.now(), a1, i1);
        Prenotazione p6 = new Prenotazione("P6", "d6", 320, LocalDate.of(2024, 12, 1), LocalTime.now(), a3, i3);
        Prenotazione p7 = new Prenotazione("P7", "d7", 320, LocalDate.of(2025, 12, 1), LocalTime.now(), a3, i3);

        mappaPrenotazioniPendenti.put(p0.getId(), p0);
        mappaPrenotazioniPendenti.put(p1.getId(), p1);
        mappaPrenotazioniPendenti.put(p7.getId(), p7);
        mappaPrenotazioniAccettate.put(p2.getId(), p2);
        mappaPrenotazioniAccettate.put(p3.getId(), p3);
        mappaPrenotazioniAccettate.put(p4.getId(), p4);
        mappaPrenotazioniAccettate.put(p5.getId(), p5);
        mappaPrenotazioniAccettate.put(p6.getId(), p6);

        // Inviti (A1 è il mittente di 2 inviti e destinatario di 1 invito)
        Invito inv1 = new Invito(p3.getEvento(), p3.getArtista(), a3);
        Invito inv2 = new Invito(p4.getEvento(), p4.getArtista(), a2);
        Invito inv3 = new Invito(p0.getEvento(), p0.getArtista(), a1);
        mappaInvitiPendenti.put(inv1.getId(), inv1);
        mappaInvitiPendenti.put(inv2.getId(), inv2);
        mappaInvitiPendenti.put(inv3.getId(), inv3);

        // Popola le recensioni
        i1.creaRecensioneArtista(LocalDate.now(), LocalTime.now(), "Commento 1", 4, a1);
        i2.creaRecensioneArtista(LocalDate.now(), LocalTime.now(), "Commento 2", 5, a2);
        i3.creaRecensioneArtista(LocalDate.now(), LocalTime.now(), "Commento 3", 3, a3);

    }

    // UC9
    // Non tutte le prenotazioni sono recensibili!!!!! solo quelle per le quali
    // l'artista si è già esibito
    public List<Prenotazione> inserisciRecensione() {
        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();

        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p = mappaPrenotazioniAccettate.get(key);

            if (p.hasArtista((Artista) utenteCorrente) &&
                    p.isRecensibile()) {
                mappaPrenotazioniTemp.put(p.getId(), p);
            }
        }

        return new ArrayList<Prenotazione>(mappaPrenotazioniTemp.values());
    }

    // recuperiamo l'impianto a cui vogliamo aggiungere un commento relativo alla
    // prenotazione
    // inserisco codice prenotazione
    // ho sostituito l'operazione seleziona evento con creaRecensione
    public void creaRecensione(int codice_prenotazione, String commento, int voto) {

        Prenotazione p = mappaPrenotazioniTemp.get(codice_prenotazione);
        p.getImpianto().creaRecensioneArtista(LocalDate.now(), LocalTime.now(), commento, voto,
                (Artista) utenteCorrente);

        mappaPrenotazioniTemp = null;

        /*
         * impiantoCorrente = p.getImpianto();
         * recensioneCorrente = impiantoCorrente.creaRecensioneArtista(LocalDate.now(),
         * LocalTime.now(), commento, voto, (Artista)utenteCorrente);
         */
    }

    // Io toglierei questa operazione. Mettiamo tutto nella precedente
    /*
     * public void confermaRecensione(){
     * 
     * impiantoCorrente.addRecensione(recensioneCorrente);
     * impiantoCorrente = null;
     * recensioneCorrente = null;
     * }
     */
    
    /*
    public Recensione getRecensioneCorrente() {
        return recensioneCorrente;
    }*/

    // UC10
    public List<Prenotazione> eliminaPrenotazione() {
        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();

        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p1 = mappaPrenotazioniAccettate.get(key);
            if (p1.hasArtista((Artista) utenteCorrente)) {
                mappaPrenotazioniTemp.put(p1.getId(), p1);
            }
        }
        for (int key : mappaPrenotazioniPendenti.keySet()) {
            Prenotazione p2 = mappaPrenotazioniPendenti.get(key);
            if (p2.hasArtista((Artista) utenteCorrente)) {
                mappaPrenotazioniTemp.put(p2.getId(), p2);
            }
        }

        return new ArrayList<Prenotazione>(mappaPrenotazioniTemp.values());
    }

    // Supponiamo di aver inserito l'id di una prenotazione ricavato dalla lista
    // delle prenotazioni che ci siamo ritornati
    public void confermaEliminazione(int codice_prenotazione) {

        if (mappaPrenotazioniTemp.get(codice_prenotazione) != null) {
            if (mappaPrenotazioniPendenti.remove(codice_prenotazione) == null) {
                mappaPrenotazioniAccettate.remove(codice_prenotazione);
            }
        }

        mappaPrenotazioniTemp = null;

        /*
         * prenotazioneCorrente = mappaPrenotazioniPendenti.get(codice_prenotazione);
         * if(prenotazioneCorrente == null){
         * mappaPrenotazioniAccettate.remove(codice_prenotazione);
         * }
         * else{
         * mappaPrenotazioniPendenti.remove(codice_prenotazione);
         * }
         * 
         * prenotazioneCorrente = null;
         */
    }

    // UC12
    public List<Prenotazione> partecipaEvento(String nomeArtista) {

        // Uso la mappa Prenotazioni in modo da non creare associazioni tra evento e
        // sistema
        mappaPrenotazioniTemp = new HashMap<Integer, Prenotazione>();

        for (int key : mappaPrenotazioniAccettate.keySet()) {
            Prenotazione p1 = mappaPrenotazioniAccettate.get(key);

            if (nomeArtista.equalsIgnoreCase(p1.getNomeArtista())) {
                if (p1.isPartecipabile()) {
                    mappaPrenotazioniTemp.put(p1.getId(), p1);
                    // listaEventi.add(p1.getEvento());
                }
            }
        }

        return new ArrayList<Prenotazione>(mappaPrenotazioniTemp.values());
    }

    // supponiamo di recuperare della lista degli eventi ritornata l'id di un
    // determinato evento/prenotazione
    // prenotazione ed il relativo evento hanno stesso ID
    public void confermaPartecipazione(int id_evento) {

        Prenotazione p = mappaPrenotazioniTemp.get(id_evento);
        p.addPartecipazioneFan((Fan) utenteCorrente);
        ((Fan) utenteCorrente).addPartecipazione(p.getEvento());

        mappaPrenotazioniTemp = null;

        /*
         * for(int i =0; i<listaEventi.size(); i++){
         * 
         * if(id_evento == listaEventi.get(i).getId()){
         * 
         * listaEventi.get(i).addFan((Fan)utenteCorrente);
         * ((Fan)utenteCorrente).addEvento(listaEventi.get(i));
         * 
         * }
         * }
         */
    }
}
