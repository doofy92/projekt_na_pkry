package serwer;
/*
    Klasa odpowidzielna za wysylanie wiadomsci zwiazanej z difi helmanem
*/
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/** Klasa odpowiadajaca za wysylanie wiaodmosci do klineta */
public class WysylanieWiadomosci extends Thread
{
    
    /** Zmiena klasy Socket przechowujaca (gniazdo sciciowe) */
    private Socket polaczenie = null;
    
    /** zmienna przechowujaca wiadomosc do wyslania */
    private String wiadomoscDoWysania = null;
    
    /** Zmiena klasy PrntWriter sluzaca do wysylania wiadmosci */
    private PrintWriter out = null;
    
    /** Konstruktor klasy */
    public WysylanieWiadomosci(Socket polaczenie)
    {
        this.polaczenie = polaczenie;
        try {
            // obiekt potrzebny do wysyalnia widomosci
          out = new PrintWriter(
                  polaczenie.getOutputStream(), true);
      } catch (IOException ex) {
          Logger.getLogger(WysylanieWiadomosci.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    /** Konstruktor klasy */
    public WysylanieWiadomosci(Socket polaczenie, String wiadomoscDoWyslania)
    {
        this.polaczenie = polaczenie;
        this.wiadomoscDoWysania = wiadomoscDoWyslania;
        try {
          out = new PrintWriter(
                  polaczenie.getOutputStream(), true);
      } catch (IOException ex) {
          Logger.getLogger(WysylanieWiadomosci.class.getName()).log(Level.SEVERE, null, ex);
      }
       
    }
    
    /** metoda wysylajaca wiadomosc */
    public void run()
    {   
        System.out.println("wiadomosc wyslana do klineta to:" + wiadomoscDoWysania);
        out.println(wiadomoscDoWysania);     
    }
    
    /** Metoda ustawiajaca zmiena wiadomosc do wyslania  */
    public void ustawTrescWiadomosci(String wiadomosc)
    {
        this.wiadomoscDoWysania = wiadomosc;
    }
}
