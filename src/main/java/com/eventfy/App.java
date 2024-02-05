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

}