package klient;





import java.math.BigInteger;
import java.util.Random;


/** 
 Klasa odpowiadająca za ustalanie Klucza Diffiego-Helmana*/
public class UstalanieKluczaDiffiHelman 
{
        /** zmienna odpowidaja za parametrA w difim helmanie */
    
        private int parametrA;
        
        /** Obiekt klasy random odbpowiadajacy za generator liczb */
    
        private Random generator = new Random();
        
        /** zmienna odpowiadająca za licznik nowych klientow */
        private int licznikNowychKlientow;
        
        /** zmienna typu String[] przehcowujaca parametry diffiego helmana i port */
        private String[] daneZParametramiIPortem;
        
        /** zmienna typu int przehcowujaca parametrP */
        private int parametrP;
        
        /** zmienna typu int przehcowujaca parametrQ */
        private int parametrQ;
        
        
        /** konstruktor klasy  */
        public UstalanieKluczaDiffiHelman()
        {
            
            
        }
        
        
        /** metoda zwracjaca losowa liczbe  */
        private int zwrocLosowaLiczbe()
        {
            return (generator.nextInt(20) + 2);
        }
          
        
        /** metoda analizująca otrzymane dane  */
        public int analizujDane(String dane, boolean czyPojedynczy)
        {
           int liczbaZwracana;
            
            
            if(!czyPojedynczy){
               
                this.daneZParametramiIPortem = dane.split(" ");
                
                if(this.daneZParametramiIPortem.length  > 1)
                {
                    
                    this.parametrP = Integer.parseInt(this.daneZParametramiIPortem[0]);
                    this.parametrQ = Integer.parseInt(this.daneZParametramiIPortem[1]);
        
                    liczbaZwracana = zwrocLiczbe(this.parametrP, this.parametrQ, this.parametrA);
                }
                else 
                {
                    
                    liczbaZwracana = zwrocLiczbeKolejna(this.parametrP,  this.parametrA, dane);
                }
            
            }
            else
            {
                 this.daneZParametramiIPortem = dane.split(" ");
                 this.parametrP = Integer.parseInt(this.daneZParametramiIPortem[1]);
                 this.parametrQ = Integer.parseInt(this.daneZParametramiIPortem[2]); 
                 System.out.println("Poczatkowe ustalanie p i q");
                 System.out.println("p = " + this.parametrP);
                 System.out.println("q = " + this.parametrQ);
                 System.out.println("a = " + this.parametrA);
                 liczbaZwracana = zwrocLiczbe(this.parametrP, this.parametrQ, this.parametrA);
                 
            }
            
            
            return liczbaZwracana;
        }

        
        /** metoda zwracajaca liczbe(klucz) w zalezności od pobranych paramterów wejściowych */
        private int zwrocLiczbe(int p, int q, int a)
        {
             System.out.println(" w metodzie zwroc liczbe p: " + p);
             System.out.println(" w metodzie zwroc liczbe q: " + q);
             System.out.println(" w metodzie zwroc liczbe a: " + a);
             BigInteger b1 = new BigInteger(String.valueOf(q));
             BigInteger b2 = b1.pow(a);
             BigInteger b3 = new BigInteger(String.valueOf(p));
             System.out.println("b2.pow(parametr1) " + b2);
             BigInteger b4 = b2.mod(b3);
             System.out.println("b4 = q^a mod(p): " + b4);
             System.out.println("Rzeczywista liczba zwracana to: " + b4);
            return Integer.parseInt(String.valueOf(b4));
        }
        
        /** metoda zwracajaca kolejną liczbę */
        private int zwrocLiczbeKolejna(int p, int a, String dane)
        {
            
             BigInteger b1 = new BigInteger(dane);
             BigInteger b2 = b1.pow(a);
             BigInteger b3 = new BigInteger(String.valueOf(p));
           
             BigInteger b4 = b2.mod(b3);
            
             System.out.println("Rzeczywista liczba zwracana to: " + b4);
            return Integer.parseInt(String.valueOf(b4));
        }
        
        
        /** metoda zwracajaca parametrP */
        public int zwrocParametrP()
        {
            return this.parametrP;
        }
        
        /** metoda zwracajaca parametrQ */
        public int zwrocParamterQ()
        {
            return this.parametrQ;
        }
        
        /** metoda zwracajaca parametrA */
        public int zwrocParametrA()
        {
            return this.parametrA;
        }
        
        /** metoda ustawiająca parametrP */
        public void ustawParamterP(int p)
        {
            this.parametrP = p;
        }
        
        /** metoda ustawiająca parametrQ */
        public void ustawParametrQ(int q)
        {
            this.parametrQ = q;
        }
        
        /** metoda ustawiająca parametrA */
        public void ustawParamterA(int a)
        {
            this.parametrA = a;
            System.out.println(a);
        }
        
        
        // metoda zwracajaca klucz sesyjny
        
        /** metoda  zwracająca klucz sesyjny */
        public int zwrocKluczSesyjny(String dane)
        {
            
            BigInteger b1 = new BigInteger(dane);
            
            BigInteger b2 = b1.pow(this.parametrA);
            
            BigInteger b3 = new BigInteger(String.valueOf(this.parametrP));
           
            BigInteger b4 = b2.mod(b3);
            
            System.out.println("klucz sesyjny: " + b4);
            return Integer.parseInt(String.valueOf(b4));
        }
        
        
        /** metoda  generujaca ParamtrA */
        public void wygenerujParametrA()
        {
            this.parametrA = zwrocLosowaLiczbe();
        }
        
             
}
