package serwer;


/*
    Klasa odpowiedzialna za wysylanie i odbieranie wiadomsoci od klientow 
    Wazna metoda to run(watek) 
*/
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/** Klasa odpowiedzialna za wysylanie i odbieranie wiadomsoci od klientow 
    Wazna metoda to run(watek)  */
public class WysylanieIodbieranieWiadomoscOdKlientow extends Thread
{
    
   /** Lista obiektow klasy  WysylanieIOdbieranieWiadomosciodKlienta */
    private List<WysylanieIOdbieranieWiadomosciodKlienta> klienciObslugiwani = null;
    /** obiekt klasy  UstalenieKluczaGrupowego*/
    private UstalenieKluczaGrupowego ustalanieKlucza = null;
    /** zmiana czy przechowujaca informache czy sluchac */
    private volatile boolean czySluchac = true;
    /** zmiana przechowuja liczbe klinetow*/
    private int liczbaKlientow = -1;          // indeks elemntow w liscie
    /** zmiana czy przechowujaca informache czy stopowac*/
    private boolean stop = false;
    /** zmiana czy przechowujaca parametrPDiffHelman*/
    private int parametrPDiffHelman;
     /** zmiana czy przechowujaca parametrQDiffHelman*/
    private int parametrQDiffHelman;
   
    
    /** konstruktor klasy*/
    public WysylanieIodbieranieWiadomoscOdKlientow()
    {
        this.ustalanieKlucza = new UstalenieKluczaGrupowego();
        this.klienciObslugiwani  = new ArrayList<>();
        
    }
    // metoda dodajca klinetow do listy
    /** metoda dodajca klinetow do listy*/
    public void dodajKlienta(String port) throws IOException, InterruptedException
    {
        if(klienciObslugiwani.size() > 1 )
        {
          
          wyslanieAktualnychParametrowPIQDiffHelman();
          
          this.stop = true;
        }
        
        this.liczbaKlientow++;
        WysylanieIOdbieranieWiadomosciodKlienta wysIOdb  = new WysylanieIOdbieranieWiadomosciodKlienta(port);
        klienciObslugiwani.add(wysIOdb);
        klienciObslugiwani.get(klienciObslugiwani.size()-1).start(); 
        Thread.sleep(500);
        this.stop = false;
        
    }
      
    // watek nasluchu jesli klinet jakis wyslal wiadomosc to wysylamy widomosc do kolejnego klineta
    
    /** watek nasluchu jesli klinet jakis wyslal wiadomosc to wysylamy widomosc do kolejnego klineta*/
    public void run()
    {
       int indeks = -1;
       while(!Thread.currentThread().isInterrupted()){
           
           while(czySluchac){
                       
                   try{
                     
                            indeks = indeksKlientaKtoryWyslalWiadomosc();
                            
                           if(indeks != -1)
                           {

                                 if( indeks + 1 == klienciObslugiwani.size())
                                 {
                                     klienciObslugiwani.get(0).WyslijWiadomosc(klienciObslugiwani.get(indeks).wiadomoscOdKlienta());
                                     klienciObslugiwani.get(indeks).ustawKontenrNaNull();
                                 }
                                 else
                                 {
                                     klienciObslugiwani.get(indeks + 1).WyslijWiadomosc(klienciObslugiwani.get(indeks).wiadomoscOdKlienta());
                                     klienciObslugiwani.get(indeks).ustawKontenrNaNull();
                                 }

                           }
                           
                                 
                   }catch(NullPointerException e){
                       
                      
                   }
                   
               }
               
           System.out.println("czy sluchac = false");
           
           
       }
 }
           
    /** metoda zwracajaca liczbe klinetow*/
    public int zwrocLiczbeKlientow()
    {
        return klienciObslugiwani.size();
    }
    
    /** metoda wysylajaca wiadomosc nowy klinet*/
    private void wyslijNowyKlient()
    {
        for(int i = 0; i < klienciObslugiwani.size(); i++)
            {
                klienciObslugiwani.get(i).WyslijWiadomosc("Nowy klient");
            }
    }
    
    /** metoda zwracajaa indeks klienta ktory wyslal wiadmosc*/
    private int indeksKlientaKtoryWyslalWiadomosc()
    {
        int indeksDoWyslania = -1;
        
        for(int i = 0; i < klienciObslugiwani.size(); i++)
        {
            if(klienciObslugiwani.get(i).wiadomoscOdKlienta() != null)
            {
               indeksDoWyslania = i;
               break;
            }
        }
        
        return indeksDoWyslania;
    }
    
    
    /** meotda ustawiajaca parametry p i q diffiego helmana*/
    public void uaktualnienieParametrowPIQdifiHelman(int p , int q)
    {
        this.parametrPDiffHelman = p;
        this.parametrQDiffHelman = q;
    }
    
    
    /** metoda wysylajaca aktualne parametry diffiego helmana*/
    private void wyslanieAktualnychParametrowPIQDiffHelman()
    {
        for(int i = 0; i < klienciObslugiwani.size(); i++)
            {
                klienciObslugiwani.get(i).WyslijWiadomosc(this.parametrPDiffHelman + " " + this.parametrQDiffHelman);
            }
    }
    

}
