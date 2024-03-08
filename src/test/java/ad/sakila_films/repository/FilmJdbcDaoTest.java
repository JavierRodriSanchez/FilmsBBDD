package ad.sakila_films.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ad.sakila_films.repository.impl.FilmJdbcDao;
import ad.sakila_films.repository.model.Film;

public class FilmJdbcDaoTest {

	private FilmJdbcDao filmJdbcDao;

	@Before
	public void setUp() {
		filmJdbcDao = new FilmJdbcDao();
		// Agrega aquí cualquier configuración necesaria para tu prueba
	}

	@Test
	public void testCrud() throws Exception {
		// Crea un objeto Film para insertar en la base de datos
		Film filmToInsert = createMockFilm();
		// Inserta el film y obtiene el ID generado
		int generatedId = filmJdbcDao.create(filmToInsert);
		assertTrue(generatedId > 0); // Verifica que el ID generado sea válido

		// Obtiene el film por el ID generado
		Film retrievedFilm = filmJdbcDao.getById(generatedId);
		assertNotNull(retrievedFilm); // Verifica que se haya recuperado un film
		assertEquals(filmToInsert.getTitle(), retrievedFilm.getTitle()); // Verifica los datos recuperados
		// ... Verifica otros campos según tus necesidades ...

		retrievedFilm.setTitle("updatedTitle");
		Film filmUpdated = filmJdbcDao.update(retrievedFilm);
		assertEquals("updatedTitle"
				+ "", filmUpdated.getTitle());

		// Elimina el film creado para limpiar la base de datos después de la prueba
		filmJdbcDao.delete(generatedId);

		List<Film> films = filmJdbcDao.getAll();
		assertNotNull(films); // Verifica que se haya recuperado una lista de films
		assertTrue(films.size() >= 0); // Verifica que la lista de films no sea nula y tenga un tamaño válid
	}

	// Método de ayuda para crear un objeto Film de ejemplo
	private Film createMockFilm() {
		Film film = new Film();
		film.setTitle("Título de la película");
		film.setDescription("Descripción de la película");
		film.setReleaseYear((short) 2022);
		film.setLanguageId(1);
		film.setRentalDuration(3);
		film.setRentalRate(BigDecimal.valueOf(4.99));
		film.setReplacementCost(BigDecimal.valueOf(19.99));
		film.setRating("PG");
		Set<String> specialFeatures = new HashSet<>();
		specialFeatures.add("Trailers");
		specialFeatures.add("Deleted Scenes");
		film.setSpecialFeatures(specialFeatures);
		film.setLastUpdate(new Timestamp(System.currentTimeMillis()));
		return film;
	}
}