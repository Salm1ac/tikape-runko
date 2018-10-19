/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Aihe;

public class AiheDao implements Dao<Aihe, Integer> {

    private Database database;

    public AiheDao(Database database) {
        this.database = database;
    }

    @Override
    public Aihe findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihe WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Integer kurssiId = rs.getInt("kurssi_id");
        String nimi = rs.getString("nimi");

        Aihe a = new Aihe(id, kurssiId, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    // ei tällä hetkellä käytössä; haetaan vain kysymykselliset
    @Override
    public List<Aihe> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihe");

        ResultSet rs = stmt.executeQuery();
        List<Aihe> aiheet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer kurssiId = rs.getInt("kurssi_id");
            String nimi = rs.getString("nimi");

            aiheet.add(new Aihe(id, kurssiId, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aiheet;
    }
    
    //Haetaan vain aiheet, joihin liittyy kysymyksiä
    public List<Aihe> findAllNonEmpty() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT DISTINCT Aihe.id, Aihe.kurssi_id, Aihe.nimi "
                + "FROM Aihe, Kysymys WHERE Kysymys.aihe_id = Aihe.id");

        ResultSet rs = stmt.executeQuery();
        List<Aihe> aiheet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer kurssiId = rs.getInt("kurssi_id");
            String nimi = rs.getString("nimi");

            aiheet.add(new Aihe(id, kurssiId, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aiheet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Aihe WHERE id=?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
    
    @Override
    public Aihe save(Aihe object) throws SQLException {
        Aihe byName = findByName(object.getNimi());

        if (byName != null) {
            return byName;
        } 

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Aihe (kurssi_id, nimi) VALUES (?, ?)");
            stmt.setInt(1, object.getKurssiId());
            stmt.setString(2, object.getNimi());
            stmt.executeUpdate();
        }

        return findByName(object.getNimi());
    }
    
    private Aihe findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, kurssi_id, nimi FROM Aihe WHERE nimi = ?");
            stmt.setString(1, nimi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Aihe(result.getInt("id"), result.getInt("kurssi_id"), result.getString("nimi"));
        }
    }

}
