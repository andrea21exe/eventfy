package com.eventfy;

import java.util.Scanner;

import com.eventfy.Exceptions.LogException;
import com.eventfy.Exceptions.LoginArtistaException;
import com.eventfy.Exceptions.LoginFanException;
import com.eventfy.Exceptions.LoginGestoreException;
import com.eventfy.Exceptions.LogoutException;
import com.eventfy.UI.MenuArtista;
import com.eventfy.UI.MenuFan;
import com.eventfy.UI.MenuGestore;
import com.eventfy.UI.MenuLogin;
import com.eventfy.UI.MenuStrategy;

public class App {

    private static Eventfy sistema;
    private static MenuStrategy menuStrategy;

    public static void main(String[] args) {

        sistema = Eventfy.getIstanceEventfy();
        sistema.populate();
        menuStrategy = new MenuLogin();
        
        while (true) {
            try {
                menuStrategy.menu();
            } catch (LogoutException e) {
                menuStrategy = new MenuLogin();
            } catch(LoginArtistaException e){
                menuStrategy = new MenuArtista();
            } catch (LoginFanException e){
                menuStrategy = new MenuFan();
            } catch (LoginGestoreException e){
                menuStrategy = new MenuGestore();
            } catch (LogException e){
                return;
            }
        }

    }

   
    /*
    private static void accedi() {

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
            menuStrategy = new MenuGestore();
        } else if (user instanceof Artista) {
            menuStrategy = new MenuArtista();
        } else {
            menuStrategy = new MenuFan();
        }
    }

    private static void registrati() {

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
                menuStrategy = new MenuArtista();
                System.out.println("Il tuo identificativo e': " + iden);
                break;
            case 2:
                iden = sistema.signUpLogIn(new Fan(nome));
                menuStrategy = new MenuFan();
                System.out.println("Il tuo identificativo e': " + iden);
                break;
            case 3:
                iden = sistema.signUpLogIn(new Gestore(nome));
                menuStrategy = new MenuGestore();
                System.out.println("Il tuo identificativo e': " + iden);
                break;

            default:
                System.out.println("Tipo di utente non valido.");
                return;
        }

        System.out.println("Registrazione avvenuta con successo!");

    }
    */
}