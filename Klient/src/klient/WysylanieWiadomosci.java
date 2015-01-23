package klient;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/** Klasa odpowiadajaca za wysylanie wiaodmosci do klineta */

public class WysylanieWiadomosci extends Thread
{
    /** Obiekt klasy socket odpowiedzialny za gniazdko polacznia */
    private Socket polaczenie = null;
    
    /** Obiekt PrintWriter odpowiedzialny za pisanie */
    private PrintWriter out = null;
    
    /** zmienna przechowujaca wiadomosc do wyslania */
    private String wiadomoscDoWyslania = null;
    
    
    /** konstruktor klasy */
    public WysylanieWiadomosci(Socket polaczenie, String wiadomoscDoWyslania) 
    {
        this.polaczenie = polaczenie;
        this.wiadomoscDoWyslania = wiadomoscDoWyslania;
        
        try {
            // inicjalizacja obiketu potrzebenego do wyslania wiadomosci
            out = new PrintWriter(
                    polaczenie.getOutputStream(), true);
            
        } catch (IOException ex) {
            Logger.getLogger(WysylanieWiadomosci.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      }
    
    
    /** watek klasy */
    public void run()
    {
            System.out.println("Wyslana wiadomosc do serwera:" + wiadomoscDoWyslania);
            out.println(this.wiadomoscDoWyslania);
           
    }
    
    /** metoda zwracajaca wiadomosc do wyslania */
    public String zwrocWiadomoscDoWyslania()
    {
        return this.wiadomoscDoWyslania;
    }
    
    
    /** metoda zwyylajaca wiadomosc*/
    public void wyslijWiadomosc(Object wiadomosc)
    {
        System.out.println("Wyslana wiadomosc do serwera:" + wiadomoscDoWyslania);
        out.println(wiadomosc);
    }
   
    
}
