/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author risto
 */
public class Vastausvaihtoehto {
    
    private Integer id;
    private String teksti;
    private boolean oikein;

    public Vastausvaihtoehto(Integer id, String teksti, boolean oikein) {
        this.id = id;
        this.teksti = teksti;
        this.oikein = oikein;
    }
    
    
    
}