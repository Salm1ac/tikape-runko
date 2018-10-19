
package tikape.runko.domain;


public class Kurssi {
    
    private Integer id;
    private String nimi;

    public Kurssi(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }

    public Integer getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }
    
    
    
}
