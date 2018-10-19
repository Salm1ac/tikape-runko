
package tikape.runko.domain;


public class Aihe {
    
    private Integer id;
    private Integer kurssiId;
    private String nimi;

    public Aihe(Integer id, Integer kurssiId, String nimi) {
        this.id = id;
        this.kurssiId = kurssiId;
        this.nimi = nimi;
    }

    public Integer getId() {
        return id;
    }

    public Integer getKurssiId() {
        return kurssiId;
    }

    public String getNimi() {
        return nimi;
    }
    
    
}
