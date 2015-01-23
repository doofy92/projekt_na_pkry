package klient;




import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JTextArea;
import javax.xml.bind.DatatypeConverter;


/** Klasa odpowiadajaca za odbieranie wiadomosci */
public class OdbieranieWiadomosci extends Thread
{
       
        /** Obiekt klasy socket odpowiadajacy za gnizdo polacznia */
       private Socket polaczenie = null;
       
        /** Obiekt BufferReader służacy do odczytywani wiadomosci*/
       private BufferedReader in = null;
       
       /** zmiena przechowujaca dana*/
       private String bufforNaDane = null;
       
       /** tablica byte[] przechowująca drugi buffor */        
       private byte[] drugiBuffor = null;
       
       /** zmienna przechowujaca informacje czy sluchac */    
       private volatile boolean czySluchac = true;
       
       /** tablica przechowujaca dane zparametrami i portem */
       private String[] daneZParametramiIPortem = new String[3];
       
       /** Obiekt klasy UstalanieKluczaDiffiHelman*/
       private UstalanieKluczaDiffiHelman difiHelman = new UstalanieKluczaDiffiHelman();
       
       /** zmienna przechowujaca informacje czy odbior jet pojedynczy*/
       private boolean czyPojedynczyOdbior; 
       
       /** zmienna przechowujaca dane do wysłania*/
       private int daneDoWyslaniaDH;
       
       /** zmienna przechowujaca klucz sesyjny */
       private int kluczSesyjny;
       
       /** zmienna przechowujaca klucz sesyjny */
       private int liczbaWiadomosciZnowymPIQ;    // licznik ktory liczby ile przyszlo wiadomosci "Nowy klient"
       
       /** zmienna okreslajaca ilosc wiadomosci dh */
       private int liczbaWiadomosciDH;           //  licznik wiadomosci dla DH
       
       /** zmienna okreslajaca liczbe ktorym klientem jest */
       private int liczbaOkreslajacaKtorymKlientemJestem;
       
       /** zmienna okreslajaca czy odbierac wiadmosc */
       private int czyOdbierac;                 // licznik okreslajacy czy odbierac czy nie
       
       /** Obiekt Klasy licznik */
       private Licznik licznik;
       
       /** Obiekt Klasy licznik */
       private Licznik licznik1;
       
       /** Obiekt Klasy licznik */
       private Licznik licznik2;
       
       /** Obiekt Klasy Buffor */
       private Buffor buffor = new Buffor();
       
       /** Obiekt Klasy JTextArea */
       private JTextArea textAreaToRead;
       
       /** Obiekt Klasy JDataInputStrea */
       private DataInputStream dIn = null;
       
       
         /** Konstruktor klasy */
        public OdbieranieWiadomosci(String Port, boolean czyPojednczy) throws IOException
        {
            
            this.liczbaWiadomosciDH = 0;
            this.liczbaWiadomosciZnowymPIQ = 0;
            this.czyOdbierac = 0;
            this.czyPojedynczyOdbior = czyPojednczy;
            licznik = new Licznik();
            licznik1 = new Licznik();
            licznik2 = new Licznik();
            
            
            this.polaczenie = new Socket("localhost", Integer.valueOf(Port));
            try {
                // inicjalizajca obiektu potrzebengo do odbierania wiadomsoci
              in = new BufferedReader(
                       new InputStreamReader(
                           polaczenie.getInputStream()));
              
            } catch (Exception exc) {
                exc.printStackTrace();
                try { polaczenie.close(); } catch(Exception e) {}
                return;
            }
        }
        
        // metoda odpowiadajaca za nasluch(watek)
        
        /** metoda odpowiadajaca za nasluch(watek) */
      public void run()
      {
          while(!Thread.interrupted())
          {
          while(czySluchac){
           try {
               if(czySluchac){
                    
                    bufforNaDane = in.readLine();
                    if(bufforNaDane.equals("null"))
                    {
                        
                    }else{
                        System.out.println("wiadomosc odebrana to " + bufforNaDane);
                    }
                    if(czyAnalizowacDane(bufforNaDane))
                    {
                        this.daneDoWyslaniaDH = 0;
                        this.daneDoWyslaniaDH = difiHelman.analizujDane(bufforNaDane, czyPojedynczyOdbior);
                       
                        
                    }
                    else
                    {
                        try{
                            this.kluczSesyjny = difiHelman.zwrocKluczSesyjny(String.valueOf(bufforNaDane));
                            
                        }catch(Exception ex)
                        {
                            
                        }
                   }
                    
                    
                    
               }
               else{
                   System.out.println("return");
                   return;
               }
           } catch (IOException ex) {
               Logger.getLogger(OdbieranieWiadomosci.class.getName()).log(Level.SEVERE, null, ex); 
          }catch(NullPointerException ex1){
              
          }
      }
      
   }
  }   
          
          
          
      /** metoda zwracajaca otrzymane dane */
      public String zwrocOtrzymaneDane()
      {
          return bufforNaDane;
      }
      
      
      /** metoda ustawiajaca zmienna czySluchac na false */
      public void zakonczWatek()
      {
          czySluchac = false;
      }
      
      
      /** metoda zwracajaca gnizdo polacznia */
      public Socket zwrocPolacznie()
      {
          return this.polaczenie;
      }
        
      
      /** metoda zwracajaca port nasluchu */
      public String zworcOtrzymanyPort()
      {
          daneZParametramiIPortem = bufforNaDane.split(" ");
          return daneZParametramiIPortem[0];
      }
      
      
      /** metoda zwracajaca parametrP */
      public int zwrocParametrP()
      {
          return this.difiHelman.zwrocParametrP();
      }
      
      /** metoda zwracajaca parametrQ */
      
      public int zwrocParametrQ()
      {
          return this.difiHelman.zwrocParamterQ();
      }
     
      
      /** metoda zwracajaca parametrA */
      public int zwrocParamterA()
      {
          return this.difiHelman.zwrocParametrA();
      }
      
      
       /** metoda zwracajaca dane do wysłania obliczone algorytmem difihelmanem */
      public int daneDoWyslaniaDiffiHelman()
      {
         
          return this.daneDoWyslaniaDH;
      }
      
      
       /** metoda zwracajaca obiekt klasy UstalanieKluczaDiffiHelman */
      public UstalanieKluczaDiffiHelman zwrocreferencjeDiffiHelman()
      {
          return difiHelman;
      }
      
      /** metoda ustawiajaca zmienna  daneDoWyslaniaDH na 0*/
      public void ustawDaneDoWyslaniaDHNaNull()
      {
          this.daneDoWyslaniaDH = 0;
      }
      
      
      /** metoda zwracajaca czy analizowac dalej dan*/
      public boolean czyAnalizowacDane(String dane)
      {
          boolean zwracanaWartosc = true;
          
          String[] daneLiczba = dane.split(" ");
          System.out.println("funkcja czy analizowac dane liczba danych: " + daneLiczba.length);
          System.out.println("this.czyOdbierac na samym poczatku= " + this.czyOdbierac);
          
          System.out.println("this.liczbaOkreslajacaKtorymKlientemJestem przed wzytskim: " + this.liczbaOkreslajacaKtorymKlientemJestem);
          
             if(daneLiczba.length  == 4)
                {
                    this.liczbaOkreslajacaKtorymKlientemJestem = Integer.parseInt(daneLiczba[3]);
                    System.out.println("this.liczbaOkreslajacaKtorymKlientemJestem w sroku: " + this.liczbaOkreslajacaKtorymKlientemJestem);
                    if(this.liczbaOkreslajacaKtorymKlientemJestem == 0 || this.liczbaOkreslajacaKtorymKlientemJestem == 1)
                    {
                        this.czyOdbierac = 1;
                    }
                    else
                    {
                        
                        this.czyOdbierac = 3;
                        this.buffor.ustawDana(this.liczbaOkreslajacaKtorymKlientemJestem);
                        System.out.println("jestem w czy czy analizowac dane w liczbaokreslajaca != 1 lub 0 cy odbierac: " + this.czyOdbierac);
                        System.out.println("buffor na dane dana: " + this.buffor.zwrocBuffor());
                        
                    }

                }
                
             else if(daneLiczba.length  == 2)
                {
                    System.out.println("czy analizowac dane: daneLiczba.length  == 2 i liczba liczbaOkreslajacaKtorymKlientemJestem: " + this.liczbaOkreslajacaKtorymKlientemJestem);
                    
                    if(this.liczbaOkreslajacaKtorymKlientemJestem == 0 || this.liczbaOkreslajacaKtorymKlientemJestem == 1)
                    {
                        
                        this.czyOdbierac = (2 + licznik1.zwrocLicznik());
                        System.out.println("czy analizowac dane po liczniku: " + this.czyOdbierac );
                        
                    }
                    else
                    {
                        this.czyOdbierac = (this.liczbaOkreslajacaKtorymKlientemJestem + licznik.zwrocLicznik());
                        System.out.println("czy analizowac dane po liczniku1: " + this.czyOdbierac );
                    }
                }
             
             else if(daneLiczba.length  == 1)
             {
                 czyOdbierac = -1;
                 
                             
             }

          
          if(this.czyOdbierac > 0)
          {
              zwracanaWartosc = true;
          }
          else
          {
              zwracanaWartosc = false;
          }
          
          System.out.println("Buffor -> liczba: " + buffor.zwrocBuffor());
          System.out.println("Czy dalej analizowac dane wartosc zwraca przed odjeciem: " + this.czyOdbierac);
          this.czyOdbierac--;
          System.out.println("Czy dalej analizowac dane wartosc zwraca: " + this.czyOdbierac);
          System.out.println("CZy dalej analizowac dane: " + zwracanaWartosc);
          
          
          return zwracanaWartosc;
      }
 
      
       /** metoda generujaca ParametryA */
      public void wygenereujParametrADiffiHelman()
      {
          difiHelman.wygenerujParametrA();
      }
      
       /** metoda zwracajaca dobra liczbe */
      private int zwrocDobraLiczbe(int liczba)
      {
          int zwracanaLiczba = 0;
          
          while(!(liczba == this.licznik2.zwrocLicznik()))
          {
              
          }
          
          zwracanaLiczba = this.licznik2.zwrocAktualnyLicznik();
          System.out.println("Liczba zwrocona z funkcji zwroc dobra liczbe: " + zwracanaLiczba);
          return zwracanaLiczba;
      }
      
       /** metoda wracajaca klucz sesyjny*/
      public int zwrocKluczSesyjny()
      {
          
          return this.kluczSesyjny;
      }
      
      
      /** metoda ustawiajaca obiket klasy textarea*/
      public void ustawTextArea(JTextArea textAreaToRead)
      {
          this.textAreaToRead = textAreaToRead;
      }

}
