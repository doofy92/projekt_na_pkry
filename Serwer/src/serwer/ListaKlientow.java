

package serwer;


/*
    Klasa posiadajaca liste klinetow aktulanie obslugiwanych 
*/
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** klasa zawierajaca liste klientow */
public class ListaKlientow extends Thread{
    /** lista zawierajaca liste klientow*/
    private List<WysylanieIOdbieranieWiadSzyfrowanychOdKlienta> klienciObslugiwani = null;
    
    /** konstruktor */
     public ListaKlientow()
    {
        this.klienciObslugiwani  = new ArrayList<>();
    }
     
     // metoda dodajaca do listy kolejnego klinet
     /** metoda dodajaca nowych klientow */
     public void DodajKlienta(int port) throws IOException
     {
         System.out.println("Dodanie klineta do szyfrowania na porcie: " + port);
         WysylanieIOdbieranieWiadSzyfrowanychOdKlienta nowyKlient = new WysylanieIOdbieranieWiadSzyfrowanychOdKlienta(port);
         this.klienciObslugiwani.add(nowyKlient);
         nowyKlient.start();
     }
     
     // watek nasluchu potrzebny do tego ze jak jakis klinet wysle wiadomosc do serwera to serwer ta wiadomosc odesle kolejneu klientowi
     /** watek umozliwiajacy nasluch */
     public void run()
     {
         int indeks = -1;
         
         while(true)
         {
             indeks = indeksKlientaKtoryWyslalWiadomosc();
             
             if(indeks != -1)
                {
                    for(int i = 0; i < klienciObslugiwani.size(); i++)
                    {
                        if(i != indeks)
                        {
                            try {
                                klienciObslugiwani.get(i).wysylanieWiadomosci(klienciObslugiwani.get(indeks).zwrocWiadomoscOdKlienta());
                            } catch (IOException ex) {
                                Logger.getLogger(ListaKlientow.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    klienciObslugiwani.get(indeks).ustawWiadomoscOdKlientaNaNull();
                }
             try {
                 Thread.sleep(300);
             } catch (InterruptedException ex) {
                 Logger.getLogger(ListaKlientow.class.getName()).log(Level.SEVERE, null, ex);
             }
            }
            
         }
         
         
     
     // metoda sprawdzajaca czy jakis klinet wyslal wiadomosc do serwera
     /** metoda sprawdzajaca aktywnosc klientow */
     private int indeksKlientaKtoryWyslalWiadomosc()
    {
        int indeksDoWyslania = -1;
        
        for(int i = 0; i < klienciObslugiwani.size(); i++)
        {
            if(klienciObslugiwani.get(i).zwrocWiadomoscOdKlienta() != null)
            {
               indeksDoWyslania = i;
               break;
            }
        }
        
        return indeksDoWyslania;
    }
    
}
