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
import tikape.runko.domain.Vastausvaihtoehto;

public class VastausvaihtoehtoDao implements Dao<Vastausvaihtoehto, Integer> {

    private Database database;

    public VastausvaihtoehtoDao(Database database) {
        this.database = database;
    }

    @Override
    public Vastausvaihtoehto findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastausvaihtoehto WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        Boolean oikein = rs.getBoolean("oikein");

        Vastausvaihtoehto v = new Vastausvaihtoehto(id, nimi, oikein);

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    @Override
    public List<Vastausvaihtoehto> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastausvaihtoehto");

        ResultSet rs = stmt.executeQuery();
        List<Vastausvaihtoehto> vastausvaihtoehdot = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            Boolean oikein = rs.getBoolean("oikein");

            vastausvaihtoehdot.add(new Vastausvaihtoehto(id, nimi, oikein));
        }

        rs.close();
        stmt.close();
        connection.close();

        return vastausvaihtoehdot;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Vastausvaihtoehto WHERE id=?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

}