package klient;




import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;


/** Klasa odpowiadajaca za "trzymanie" obiektu zwiazanego z wysyalniem i odbieraniem wiadomosci w jendnym miejscu.
    Widac to w run-watku */
public class WysylanieIOdbieranie extends Thread
{
    
    /** zmiena przechowująca port dostępu */
     private String portDostepu = null;
     
     /** zObiekt klasy socekt odpowiadający za połączenie */
     private Socket polaczenie = null;
     
     /** Obiekt klasy OdbieranieWiadomosci */
     private OdbieranieWiadomosci odbieranieWiadomosci = null;
     
     /** zmienna przechowująca informacje czy dalej sluchac */
     private boolean czySluchac = true;
     
     /** zmienna przechowująca informacje o danych */
     private String dane = null;
     
     /** Obiekt klasy WysylanieWiadomosci */
     private WysylanieWiadomosci wysylanie = null;
     
     /** zmiana przechowujaca pocztkowa wiadmosc */
     private String poczatkowaWiadomosc = null;
     
     
      /** konstruktor klasy */
     public WysylanieIOdbieranie(String port, String poczatkowaWiadomosc, int p, int q, int a) throws IOException
     {
         this.portDostepu = port;
         this.poczatkowaWiadomosc = poczatkowaWiadomosc;
         this.odbieranieWiadomosci = new OdbieranieWiadomosci(port, false);
         this.odbieranieWiadomosci.zwrocreferencjeDiffiHelman().ustawParamterP(p);
         this.odbieranieWiadomosci.zwrocreferencjeDiffiHelman().ustawParametrQ(q);
         this.odbieranieWiadomosci.zwrocreferencjeDiffiHelman().ustawParamterA(a);
         this.odbieranieWiadomosci.start();
         
     }
     
     /*
        Jesli otrzyamy wiadomosc na porcie to wysylamy na tym samym porcie, oczywiscie najpierw te dane analizujemy , ale to w klasie ustalanie klucza diffi helman
     */
     
      /** Metoda wątku. Jesli otrzyamy wiadomosc na porcie to wysylamy na tym samym porcie, oczywiscie najpierw te dane analizujemy , ale to w klasie ustalanie klucza diffi helman */
     public void run()
     {
         wysylanie = new WysylanieWiadomosci(this.odbieranieWiadomosci.zwrocPolacznie(), this.poczatkowaWiadomosc);
         while(wysylanie.zwrocWiadomoscDoWyslania() == null)
         {
             
         }
         wysylanie.start();
         odbieranieWiadomosci.ustawDaneDoWyslaniaDHNaNull();
         while(czySluchac)
         {
             try {
                 
                 if(odbieranieWiadomosci.daneDoWyslaniaDiffiHelman() != 0)
                 {
                     WysylanieWiadomosci wys = new WysylanieWiadomosci(this.odbieranieWiadomosci.zwrocPolacznie(), String.valueOf(odbieranieWiadomosci.daneDoWyslaniaDiffiHelman()));
                     wys.start();
                     odbieranieWiadomosci.ustawDaneDoWyslaniaDHNaNull();
                 }
                 
                 
                 Thread.sleep(1000);
             } catch (InterruptedException ex) {
                 Logger.getLogger(WysylanieIOdbieranie.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
     }
     
     
     /** metoda zwracajaca kluczsesyjny */
     public int zwrocKlucz()
     {
                
               return odbieranieWiadomosci.zwrocKluczSesyjny();
     }
     
     
     /** metoda ustawiajaca textareatowrite */
     public void ustawTextArea(JTextArea textAreaToRead)
     {
         odbieranieWiadomosci.ustawTextArea(textAreaToRead);
     }
     
}
