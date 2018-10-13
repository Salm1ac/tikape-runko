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
