/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package waluty;

/**
 *
 * @author Archax
 */
public class Sample {
    private final double kupno;
    private final double sprzedaz;
    
    public Sample (double kupno, double sprzedaz){
        this.kupno = kupno;
        this.sprzedaz = sprzedaz;
    }
       
    @Override
    public String toString(){
        return "kupno: " + kupno + " sprzedaz: " + sprzedaz;
    }
    
    public double getKupno(){
        return kupno;
    }
    
    public double getSprzedaz(){
        return sprzedaz;
    }
}
