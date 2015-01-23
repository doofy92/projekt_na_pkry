package serwer;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/** Klasa Odpowiedzialana za wysylanie i odbieranie wiadomosci od klineta i trzmania tych dwoch obiektow klas w jednym miejscu */
public class UstalenieKluczaGrupowego extends Thread
{
    /** Kolejka obiektow klasy WysylanieIOdbieranieWiadomosciodKlienta*/
    private Queue<WysylanieIOdbieranieWiadomosciodKlienta> klienciObslugiwani = null;
    
    /**  zmiena przechowujaca informajce czywysylac informacje*/
    private boolean czyWysylac = true;
    
    
    /**  konstruktor*/
    public UstalenieKluczaGrupowego()
    {
        klienciObslugiwani  = new LinkedBlockingQueue<>();
    }
    
    
    /**  metoda dodajaca klienta do kolejki*/
    public void dodajKlienta(WysylanieIOdbieranieWiadomosciodKlienta odbieranieIWysylanie)
    {
        klienciObslugiwani.add(odbieranieIWysylanie);
        klienciObslugiwani.element().start();
        this.czyWysylac = true;
    }
    
    
    /**  metoda watku nasluchujaca na wiadomosci od klinetow */
    public void run()
    {
        WysylanieIOdbieranieWiadomosciodKlienta temp = null;
        WysylanieIOdbieranieWiadomosciodKlienta temp1 = null;
        String wiadomoscDoWyslania = null;
        while(czyWysylac)
        {
                if(czyWysylac)
                {
                    temp = klienciObslugiwani.poll();
                    if (temp.wiadomoscOdKlienta() != null)
                        {
                             wiadomoscDoWyslania = temp.wiadomoscOdKlienta();
                             System.out.println("Wiadomosc do wyslania do klienta drugiego od pierwszego to: " + wiadomoscDoWyslania);
                             
                             if(klienciObslugiwani.peek().equals(null))
                             {
                                 System.out.println("Klient null");
                             }
                             else{
                                 System.out.println("Klient != null");
                             }
               
                             klienciObslugiwani.element().WyslijWiadomosc((wiadomoscDoWyslania));
                             temp.ustawKontenrNaNull();
                             klienciObslugiwani.add(temp);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(UstalenieKluczaGrupowego.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        }
                    else
                    {
                        
                    }
                }else
                {
                    return;
                }
        }
    }
    
    /**  metoda zwracajaca liczbe klientow*/
    public int zwrocLiczbeklientow()
    {
        return klienciObslugiwani.size();
    }
    
    /**   meoda przerywajca watek*/
    public void przerwijWatek()
    {
        this.czyWysylac = false;
    }
}
