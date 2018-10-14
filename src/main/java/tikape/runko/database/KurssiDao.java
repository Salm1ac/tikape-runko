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
import tikape.runko.domain.Kurssi;

public class KurssiDao implements Dao<Kurssi, Integer> {

    private Database database;

    public KurssiDao(Database database) {
        this.database = database;
    }

    @Override
    public Kurssi findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kurssi WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Kurssi k = new Kurssi(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }

    @Override
    public List<Kurssi> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kurssi");

        ResultSet rs = stmt.executeQuery();
        List<Kurssi> kurssit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            kurssit.add(new Kurssi(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return kurssit;
    }
    
    //Haetaan vain kurssit, joihin liittyy kysymyksiä
    public List<Kurssi> findAllNonEmpty() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT Kurssi.id, Kurssi.nimi "
                + "FROM Kurssi, Aihe, Kysymys WHERE Aihe.kurssi_id = Kurssi.id "
                + "AND Kysymys.aihe_id = Aihe.id");

        ResultSet rs = stmt.executeQuery();
        List<Kurssi> kurssit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            kurssit.add(new Kurssi(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return kurssit;
    }

    @Override
    public Kurssi save(Kurssi object) throws SQLException {
        Kurssi byName = findByName(object.getNimi());

        if (byName != null) {
            return byName;
        } 

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kurssi (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }

        return findByName(object.getNimi());
    }
    
    private Kurssi findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Kurssi WHERE nimi = ?");
            stmt.setString(1, nimi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Kurssi(result.getInt("id"), result.getString("nimi"));
        }
    }
    

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Kurssi WHERE id=?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

}
