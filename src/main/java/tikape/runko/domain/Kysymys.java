
package tikape.runko.domain;


public class Kysymys {
    
    private Integer id;
    private Integer aiheId;
    private String teksti;

    public Kysymys(Integer id, Integer aiheId, String teksti) {
        this.id = id;
        this.aiheId = aiheId;
        this.teksti = teksti;
    }

    public Integer getId() {
        return id;
    }

    public Integer getAiheId() {
        return aiheId;
    }

    public String getTeksti() {
        return teksti;
    }
    
    
}
