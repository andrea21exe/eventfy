package com.eventfy.UI;

import java.util.Scanner;

import com.eventfy.Eventfy;
import com.eventfy.Exceptions.LogException;
import com.eventfy.Exceptions.LogoutException;

public abstract class MenuStrategy {

    protected static Eventfy sistema;

    public MenuStrategy() {
        sistema = Eventfy.getIstanceEventfy();
    }

    public void menu() throws Exception {
        displayMenu();
        int scelta = getOperazioneUtente();
        processaScelta(scelta);
    }

    abstract void displayMenu();

    protected int getOperazioneUtente() {
        Scanner input = new Scanner(System.in);
        System.out.print("Inserisci il numero dell'opzione desiderata: ");
        while (!input.hasNextInt()) {
            System.out.println("Inserisci un numero valido.");
            input.next();
        }

        int i = input.nextInt();
        return i;

    }

    abstract void processaScelta(int scelta) throws LogException, Exception;

    protected void logout() throws LogoutException {
        sistema.logout();
        throw new LogoutException();
    }
}
