package com.eventfy;

import java.util.Scanner;

public abstract class MenuStrategy {

    protected static Eventfy sistema;

    public MenuStrategy() {
        sistema = Eventfy.getIstanceEventfy();
    }

    public void menu() {
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

    abstract void processaScelta(int scelta);
}
