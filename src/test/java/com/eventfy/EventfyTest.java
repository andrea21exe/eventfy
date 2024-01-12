package com.eventfy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EventfyTest {

    static Eventfy eventfy;

    @BeforeAll
    public static void initTest() {

        eventfy = Eventfy.getIstanceEventfy();
        eventfy.setUtenteCorrente(new Gestore("andrea"));
    }

    @Test
    void testNuovoImpianto() {

        /*try{
            eventfy.nuovoImpianto("null", "null", 0, 0);
            fail("Non hai messo niente");
        } catch(Exception e){
            assertEquals(e.getMessage(), "Non va");
        }*/

        try {
            assertNull(eventfy.getImpiantoCorrente());
            eventfy.nuovoImpianto("San Siro", "Milano", 80000, 100);
            assertNotNull(eventfy.getImpiantoCorrente());
        } catch (Exception e) {
            fail("Errore");
        }

    }

    @Test
    void testConfermaImpianto() {

        assertNull(eventfy.getImpiantoCorrente()); 

        Impianto nuovoImpianto = eventfy.nuovoImpianto("San Siro", "Milano", 80000, 100);
        eventfy.confermaImpianto();

        assertNull(eventfy.getImpiantoCorrente()); 
        assertEquals(1, eventfy.getListaImpianti().size());
        assertTrue(eventfy.getListaImpianti().contains(nuovoImpianto)); 
        
    }  


}
