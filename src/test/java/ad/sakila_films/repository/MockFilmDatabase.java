package ad.sakila_films.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import ad.sakila_films.repository.impl.DriverHelper;

public class MockFilmDatabase {
	
	public static final int PRIMER_ID_JUEGO_PRUEBAS =1001;

    // Lógica para borrar toda la información de la base de datos
    public void cleanDatabase() {
        try (Connection conn = DriverHelper.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM Film where film_id>=" +PRIMER_ID_JUEGO_PRUEBAS)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        	System.out.println("Error limpiando la base de datos"+e);
        }
        System.out.println("BBDD limpia. Eliminados registros con id mayor de 1000");
    }

    // Lógica para insertar datos mock harcodeados en la base de datos
    public void insertMockData() {

        try (Connection conn = DriverHelper.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(
                        "INSERT INTO Film (film_id,title, description, release_year, language_id, rental_duration, "
                                + "rental_rate, replacement_cost, rating, last_update, special_features) "
                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?)")) {

            // Inserta datos mock harcodeados
            for (int i = 0; i <= 4; i++) {
            	int pos_param=1;
            	int newId = PRIMER_ID_JUEGO_PRUEBAS + i;
            	preparedStatement.setInt(pos_param++, newId);
                preparedStatement.setString(pos_param++, "Título de la película " + i);
                preparedStatement.setString(pos_param++, "Descripción de la película");
                preparedStatement.setShort(pos_param++, (short) 2022);
                preparedStatement.setInt(pos_param++, 1);
                preparedStatement.setInt(pos_param++, 3);
                preparedStatement.setBigDecimal(pos_param++, BigDecimal.valueOf(4.99));
                preparedStatement.setBigDecimal(pos_param++, BigDecimal.valueOf(19.99));
                preparedStatement.setString(pos_param++, "PG");
                preparedStatement.setTimestamp(pos_param++, new Timestamp(System.currentTimeMillis()));
                
                
        		Set<String> specialFeatures = new HashSet<>();
        		specialFeatures.add("Trailers");
        		specialFeatures.add("Commentaries");
      
				preparedStatement.setString(pos_param++, String.join(",", specialFeatures));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error insertando:" + e);
        }
        System.out.println("BBDD rellena con datos ficticios fijos");
    }
}
