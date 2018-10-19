
package tikape.runko.domain;


public class Vastausvaihtoehto {
    
    private Integer id;
    private Integer kysymysId;
    private String teksti;
    private boolean oikein;

    public Vastausvaihtoehto(Integer id, Integer kysymysId, String teksti, boolean oikein) {
        this.id = id;
        this.kysymysId = kysymysId;
        this.teksti = teksti;
        this.oikein = oikein;
    }

    public Integer getId() {
        return id;
    }

    public Integer getKysymysId() {
        return kysymysId;
    }

    public String getTeksti() {
        return teksti;
    }

    public boolean isOikein() {
        return oikein;
    }
    
    
    
}
