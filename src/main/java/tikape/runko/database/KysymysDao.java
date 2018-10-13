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
import tikape.runko.domain.Kysymys;

public class KysymysDao implements Dao<Kysymys, Integer> {

    private Database database;

    public KysymysDao(Database database) {
        this.database = database;
    }

    @Override
    public Kysymys findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Integer aiheId = rs.getInt("aihe_id");
        String teksti = rs.getString("teksti");

        Kysymys k = new Kysymys(id, aiheId, teksti);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }

    @Override
    public List<Kysymys> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kysymys");

        ResultSet rs = stmt.executeQuery();
        List<Kysymys> kysymykset = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer aiheId = rs.getInt("aihe_id");
            String teksti = rs.getString("teksti");

            kysymykset.add(new Kysymys(id, aiheId, teksti));
        }

        rs.close();
        stmt.close();
        connection.close();

        return kysymykset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Kysymys WHERE id=?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
    
    @Override
    public Kysymys save(Kysymys object) throws SQLException {
        Kysymys byText = findByName(object.getTeksti());

        if (byText != null) {
            return byText;
        } 

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys (aihe_id, teksti) VALUES (?, ?)");
            stmt.setInt(1, object.getAiheId());
            stmt.setString(2, object.getTeksti());
            stmt.executeUpdate();
        }

        return findByName(object.getTeksti());
    }
    
    private Kysymys findByName(String teksti) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, aihe_id, teksti FROM Kysymys WHERE teksti = ?");
            stmt.setString(1, teksti);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Kysymys(result.getInt("id"), result.getInt("aihe_id"), result.getString("nimi"));
        }
    }

}
