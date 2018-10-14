package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.*;
import tikape.runko.domain.*;

public class Main {

    public static void main(String[] args) throws Exception {
        
        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database database = new Database("jdbc:sqlite:kysymyskontti.db");
        database.init();

        KurssiDao kurssiDao = new KurssiDao(database);
        AiheDao aiheDao = new AiheDao(database);
        KysymysDao kysymysDao = new KysymysDao(database);
        VastausvaihtoehtoDao vastausvaihtoehtoDao = new VastausvaihtoehtoDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kurssit", kurssiDao.findAllNonEmpty());
            map.put("aiheet", aiheDao.findAllNonEmpty());
            map.put("kysymykset", kysymysDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/kysymykset", (req, res) -> {
           res.redirect("/");
           return "";
        });
        
        post("/kysymykset", (req, res) -> {
            Kurssi kurssi = kurssiDao.save(new Kurssi(-1, req.queryParams("kurssi")));
            Aihe aihe = aiheDao.save(new Aihe(-1, kurssi.getId(), req.queryParams("aihe")));
            Kysymys kysymys = kysymysDao.save(new Kysymys(-1, aihe.getId(), req.queryParams("kysymysteksti")));
            
            res.redirect("/");
            return "";
        });

        get("/kysymykset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Kysymys kysymys = kysymysDao.findOne(Integer.parseInt(req.params("id")));
            map.put("kysymys", kysymys);
            Aihe aihe = aiheDao.findOne(kysymys.getAiheId());
            map.put("aihe", aihe.getNimi());
            Kurssi kurssi = kurssiDao.findOne(aihe.getKurssiId());
            map.put("kurssi", kurssi.getNimi());
            
            map.put("vastausvaihtoehdot", vastausvaihtoehtoDao
                    .findAllMatchingKysymys(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "kysymys");
        }, new ThymeleafTemplateEngine());
        
        post("/kysymykset/:id/delete", (req, res) -> {
            kysymysDao.delete(Integer.parseInt(req.params("id")));
            
            res.redirect("/");
            return "";
        });
        
        post("/kysymykset/:id/vastaukset", (req, res) -> {
            vastausvaihtoehtoDao.save(new Vastausvaihtoehto(-1, Integer.parseInt(req.params("id")),
                req.queryParams("teksti"), Boolean.getBoolean(req.queryParams("oikein"))));
            
            res.redirect("/kysymykset/:id");
            return ""; 
        });
        
    }
}
