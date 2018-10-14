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
            map.put("kurssit", kurssiDao.findAll());
            map.put("aiheet", aiheDao.findAll());
            map.put("kysymykset", kysymysDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/kysymys/:id", (req, res) -> {
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
    }
}
