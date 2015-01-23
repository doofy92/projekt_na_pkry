package serwer;


/*
    klasa odpowiedzialana za genreacje parametrow difiego helmana
*/
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** klasa parametrow P algorytmu D-H*/
public class ParametryPIQDiffiHelman 
{
	/** zmienna parametru P */
    private int parametrP;
    /** zmiena indeksu Parametru P*/
    private int indeksParametruP;
    /** zmienna parametru Q */
    private int parametrQ;
    /** zmienna indeksu parametru Q*/
    private int indeksParametruQ;
    
    private int zakresLiczPierwszych = 4000;
    private boolean[] tablicaLiczbPierwszychBool = new boolean[zakresLiczPierwszych + 1]; 
    private List<Integer> tablicaLiczbPierwszych = new ArrayList<>();
    private Random generator = new Random();
    
    
    public ParametryPIQDiffiHelman()
    {
        genereacjaLiczbaPierwszchSitoEratostenesa();
        wypelnijTabliceLiczbPierwszych();
    }
    
    // metoda genereujaca liczby pierwsze algortymem "sito erystotelesa"
    private void genereacjaLiczbaPierwszchSitoEratostenesa()
    {
         for(int i = 2; i*i <= zakresLiczPierwszych; i++)
        {
            if (tablicaLiczbPierwszychBool[i] == true)
		continue;
            for (int j = 2 * i ; j <= zakresLiczPierwszych; j += i)
		tablicaLiczbPierwszychBool[j] = true;
 
        }
    }
      
   
   private void wypelnijTabliceLiczbPierwszych()
   {
       for (int i = 2; i <= zakresLiczPierwszych; i++)
            if (tablicaLiczbPierwszychBool[i] == false){
                if(i > 1000){
                    tablicaLiczbPierwszych.add(i);
                }
            }
   }
   
   public int zwrocLosowaLiczbePierwsza()
   {
       
        return tablicaLiczbPierwszych.get(generator.nextInt(tablicaLiczbPierwszych.size()));
   }
   
   public int zwrocLiczbeP()
   {
       this.indeksParametruP = generator.nextInt(tablicaLiczbPierwszych.size());
       return tablicaLiczbPierwszych.get(this.indeksParametruP);
   }
   
   public int zwrocLiczbeQ()
   {
      this.indeksParametruQ = generator.nextInt(this.indeksParametruP);
      return tablicaLiczbPierwszych.get(this.indeksParametruQ);
   }
   
    
}
