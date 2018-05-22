/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package waluty;


import java.io.IOException;
import java.net.MalformedURLException;
/**
 *
 * @author Archax
 */
public class Waluty {
    
    public static void main(String[] args) throws MalformedURLException, IOException {
        
        
        NBPApiConnect n = new NBPApiConnect();
        n.loadParameters();
        n.sendRequest();
        n.showResults();
    }
}
