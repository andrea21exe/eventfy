package com.eventfy.UI;

import java.util.List;
import java.util.Scanner;

import com.eventfy.Artista;
import com.eventfy.Fan;
import com.eventfy.Gestore;
import com.eventfy.Utente;
import com.eventfy.Exceptions.LogException;
import com.eventfy.Exceptions.LoginArtistaException;
import com.eventfy.Exceptions.LoginFanException;
import com.eventfy.Exceptions.LoginGestoreException;

public class MenuLogin extends MenuStrategy {

    @Override
    protected void displayMenu() {
        System.out.println("Benvenuto! Seleziona un'opzione:");
        System.out.println("1. Accedi");
        System.out.println("2. Registrati");
        System.out.println("3. Chiudi");
    }

    @Override
    protected void processaScelta(int scelta) throws LogException {
        switch (scelta) {
            case 1:
                accedi();
                break;
            case 2:
                registrati();
                break;
            case 3:
                chiudi();
                break;
            default:
                System.out.println("Scelta non valida.");
                return;
        }
    }

    private static void accedi() throws LogException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci il tuo identificativo:");
        int id = scanner.nextInt();
        // Cerca l'utente con l'identificativo fornito
        Utente user = sistema.getUtente(id);
        if (user == null) {
            System.out.println("Utente non trovato.");
            return;
        }

        // Se l'utente esiste, si effettua il login...
        sistema.logIn(id);
        // ... ed si associa il corretto menuStrategy
        if (user instanceof Gestore) {
            throw new LoginGestoreException();
        } else if (user instanceof Artista) {
            throw new LoginArtistaException();
        } else {
            throw new LoginFanException();
        }
    }

    private static void registrati() throws LogException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserisci il tuo nome:");
        String nome = scanner.nextLine();

        System.out.println("Seleziona il tipo di utente:");
        System.out.println("1. Artista");
        System.out.println("2. Fan");
        System.out.println("3. Gestore");

        int iden;

        int tipoUtente = scanner.nextInt();
        switch (tipoUtente) {
            case 1:
                iden = sistema.signUpLogIn(new Artista(nome));
                System.out.println("Il tuo identificativo e': " + iden);
                throw new LoginArtistaException();
            case 2:
                iden = sistema.signUpLogIn(new Fan(nome));
                System.out.println("Il tuo identificativo e': " + iden);
                throw new LoginFanException();
            case 3:
                iden = sistema.signUpLogIn(new Gestore(nome));
                System.out.println("Il tuo identificativo e': " + iden);
                throw new LoginGestoreException();
            default:
                System.out.println("Tipo di utente non valido.");
                return;
        }

    }

    public void chiudi() throws LogException {
        throw new LogException();
    }

}
