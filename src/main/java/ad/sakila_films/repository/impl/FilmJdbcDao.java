package ad.sakila_films.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ad.sakila_films.repository.FilmDao;
import ad.sakila_films.repository.model.Film;

public class FilmJdbcDao implements FilmDao {

	private static final String FILM_BY_AUTOR = "SELECT f.* FROM actor a "
			+ "inner join sakila.film_actor fa on a.actor_id = fa.actor_id "
			+ "inner join sakila.film f on fa.film_id = f.film_id " + "where first_name like ? "
			+ "order by release_year";

	private static final String CREATE_FILM = "INSERT INTO film (title, description, release_year, language_id, "
			+ " rental_duration, rental_rate, replacement_cost, rating, special_features) "
			+ "	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SELECT_ALL = "SELECT * FROM film order by film_id asc";

	private static final String COUNT_ALL_FILMS = "SELECT count(*) from film";

	private static final String GET_BY_ID = "SELECT * FROM film WHERE film_id = ?";

	private static final String UPDATE = "UPDATE film SET title = ?, description = ?, release_year = ?, "
			+ "language_id = ?, rental_duration = ?, rental_rate = ?, replacement_cost = ?, "
			+ "rating = ?, special_features = ? WHERE film_id = ?";

	@Override
	public int countAllFilms() throws Exception {
		try (Connection connection = DriverHelper.getConnection(); Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(COUNT_ALL_FILMS);
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new SQLException("Error al contar los films, no se obtuvo el resultado.");
			}

		} catch (SQLException e) {
			throw new Exception("Error al contar los films.", e);
		}
	}

	@Override
	public Set<Film> getAllFilmsByActor(String autor, boolean order) throws Exception {
		Set<Film> films = new HashSet<>();
		String query = FILM_BY_AUTOR;
		if (order) {
			query = query + " asc";
		} else {
			query = query + " desc";
		}

		try (Connection connection = DriverHelper.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, "%" + autor + "%");

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					Film film = mapResultSetToFilm(resultSet);
					films.add(film);
				}
			}
		} catch (SQLException e) {
			throw new Exception("Error al obtener el film por ID.", e);
		}
		return films;
	}

	@Override
	public Integer create(Film film) throws Exception {
		String insertQuery = CREATE_FILM;
		try (Connection connection = DriverHelper.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,
						Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setString(1, film.getTitle());
			preparedStatement.setString(2, film.getDescription());
			preparedStatement.setShort(3, film.getReleaseYear());
			preparedStatement.setInt(4, film.getLanguageId());
			preparedStatement.setInt(5, film.getRentalDuration());
			preparedStatement.setBigDecimal(6, film.getRentalRate());
			preparedStatement.setBigDecimal(7, film.getReplacementCost());
			preparedStatement.setString(8, film.getRating());
			preparedStatement.setString(9, String.join(",", film.getSpecialFeatures()));

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creación del film fallida, no se crearon filas.");
			}
			int idGenerado = 0;
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					idGenerado = generatedKeys.getInt(1);
					// Aquí puedes usar el ID generado como lo necesites
				} else {
					throw new SQLException("La inserción falló, no se obtuvo el ID generado.");
				}
			}
			return idGenerado;
		} catch (SQLException e) {
			throw new Exception("Error al crear el film.", e);
		}
	}

	@Override
	public Film getById(Integer filmId) throws Exception {
		Film film = null;
		try (Connection connection = DriverHelper.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {
			preparedStatement.setInt(1, filmId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					film = mapResultSetToFilm(resultSet);
				} else {
					throw new Exception("No se encontró el film con ID: " + filmId);
				}
			}
			return film;
		} catch (SQLException e) {
			throw new Exception("Error consultando el film: ",e);
		}
	}

	@Override
	public List<Film> getAll() throws Exception {
		List<Film> films = new ArrayList<>();
		try (Connection connection = DriverHelper.getConnection(); Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(SELECT_ALL);
			while (resultSet.next()) {
				Film film = mapResultSetToFilm(resultSet);
				films.add(film);
			}
		} catch (SQLException e) {
			throw new Exception("Error al obtener todos los films.", e);
		}
		return films;
	}

	@Override
	public Film update(Film filmUpdated) throws Exception {
		try (Connection connection = DriverHelper.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {

			preparedStatement.setString(1, filmUpdated.getTitle());
			preparedStatement.setString(2, filmUpdated.getDescription());
			preparedStatement.setShort(3, filmUpdated.getReleaseYear());
			preparedStatement.setInt(4, filmUpdated.getLanguageId());
			preparedStatement.setInt(5, filmUpdated.getRentalDuration());
			preparedStatement.setBigDecimal(6, filmUpdated.getRentalRate());
			preparedStatement.setBigDecimal(7, filmUpdated.getReplacementCost());
			preparedStatement.setString(8, filmUpdated.getRating());
			preparedStatement.setString(9, String.join(",", filmUpdated.getSpecialFeatures()));
			preparedStatement.setInt(10, filmUpdated.getFilmId());

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("No se pudo actualizar la pelicula:" + filmUpdated.getFilmId());
			}

			return filmUpdated;
		} catch (SQLException e) {
			throw new Exception("Error al actualizar el film.", e);
		}
	}

	@Override
	public void delete(Integer filmId) throws Exception {
		String deleteQuery = "DELETE FROM film WHERE film_id = ?";
		try (Connection conn = DriverHelper.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
			preparedStatement.setInt(1, filmId);

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new IllegalStateException("Registro no existente. No se ha podido borrar: " + filmId);
			}

		} catch (SQLException e) {
			throw new Exception("Error al eliminar el film.", e);
		}
	}

	private Film mapResultSetToFilm(ResultSet resultSet) throws SQLException {
		Film film = new Film();
		film.setFilmId(resultSet.getInt("film_id"));
		film.setTitle(resultSet.getString("title"));
		film.setDescription(resultSet.getString("description"));
		film.setReleaseYear(resultSet.getShort("release_year"));
		film.setLanguageId(resultSet.getInt("language_id"));
		film.setOriginalLanguageId(resultSet.getInt("original_language_id"));
		film.setRentalDuration(resultSet.getInt("rental_duration"));
		film.setRentalRate(resultSet.getBigDecimal("rental_rate"));
		film.setLength(resultSet.getInt("length"));
		film.setReplacementCost(resultSet.getBigDecimal("replacement_cost"));
		film.setRating(resultSet.getString("rating"));
		Set<String> specialFeatures = new HashSet<>();
		String featuresString = resultSet.getString("special_features");
		if (featuresString != null) {
			String[] featuresArray = featuresString.split(",");
			for (String feature : featuresArray) {
				specialFeatures.add(feature);
			}
			film.setSpecialFeatures(specialFeatures);
		}
		film.setLastUpdate(resultSet.getTimestamp("last_update"));

		return film;
	}

	@Override
	public List<Film> searchFilms(String rating, Integer length, Integer rentalDuration) throws Exception {

		List<Film> matchingFilms = new ArrayList<>();
		StringBuilder searchQuery = new StringBuilder("SELECT * FROM film WHERE 1=1");
		if (rating != null) {
			searchQuery.append(" AND rating = ?");
		}

		if (length != null) {
			searchQuery.append(" AND length = ?");
		}

		if (rentalDuration != null) {
			searchQuery.append(" AND rental_duration = ?");
		}

		try (Connection connection = DriverHelper.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(searchQuery.toString())) {
			int parameterIndex = 1;

			if (rating != null) {
				preparedStatement.setString(parameterIndex++, rating);
			}

			if (length != null) {
				preparedStatement.setInt(parameterIndex++, length);
			}

			if (rentalDuration != null) {
				preparedStatement.setInt(parameterIndex++, rentalDuration);
			}

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Film film = mapResultSetToFilm(resultSet);
				matchingFilms.add(film);
			}

		} catch (SQLException e) {
			throw new Exception("Error al buscar películas.", e);
		}

		return matchingFilms;
	}

}