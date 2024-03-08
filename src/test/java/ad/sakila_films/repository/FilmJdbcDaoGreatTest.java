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

/**
 * 
 */
public class FilmJdbcDaoGreatTest {

	private FilmJdbcDao filmJdbcDao;
	private MockFilmDatabase mockFilmDatabase;

	/**
	 * Metodo que se ejecuta antes de cada caso de test
	 * 
	 * Borra todas las peliculas a partir del id 1000, para restaurar la base de datos con los datos originales de Sakila
	 * Inserta 5 peliculas con datos fijos, para poder realizar las pruebas
	 */
	@Before
	public void setUp() {
		filmJdbcDao = new FilmJdbcDao();
		mockFilmDatabase = new MockFilmDatabase();
		// Borra toda la información antes de cada prueba
		mockFilmDatabase.cleanDatabase();
		// Crea datos mock harcodeados
		mockFilmDatabase.insertMockData();
	}

	@Test
	public void testGetById() throws Exception {
		System.out.println("testGetById");
		Film retrievedFilm = filmJdbcDao.getById(MockFilmDatabase.PRIMER_ID_JUEGO_PRUEBAS);
		assertNotNull(retrievedFilm);
		assertEquals("Título de la película 0", retrievedFilm.getTitle());
	}

	@Test
	public void testGetAll() throws Exception {
		System.out.println("testGetAll");
		List<Film> films = filmJdbcDao.getAll();
		assertNotNull(films);
		//Debe haber 1000 registros + 5 creados
		assertEquals(1005, films.size());
	}

	@Test
	public void testCreate() throws Exception {
		System.out.println("testCreate");
		Film newFilm = createMockFilm("New Movie");
		int generatedId = filmJdbcDao.create(newFilm);
		assertTrue(generatedId > 0);

		Film retrievedFilm = filmJdbcDao.getById(generatedId);
		assertNotNull(retrievedFilm);

		// Aserciones adicionales para los campos del objeto Film
		// Nos aseguramos que los datos devueltos al buscar la pelicula recien creada,
		//coinciden con los que hemos indicado al creadlo
		assertEquals("New Movie", retrievedFilm.getTitle());
		assertEquals("Descripción de la película", retrievedFilm.getDescription());
		assertEquals((short) 2022, retrievedFilm.getReleaseYear());
		assertEquals(1, retrievedFilm.getLanguageId());
		assertEquals(3, retrievedFilm.getRentalDuration());
		assertEquals(BigDecimal.valueOf(4.99), retrievedFilm.getRentalRate());
		assertEquals(BigDecimal.valueOf(19.99), retrievedFilm.getReplacementCost());
		assertEquals("PG", retrievedFilm.getRating());
		assertNotNull(retrievedFilm.getLastUpdate());

	}

	@Test
	public void testUpdate() throws Exception {
		Film filmToUpdate = filmJdbcDao.getById(MockFilmDatabase.PRIMER_ID_JUEGO_PRUEBAS);
		assertNotNull(filmToUpdate);

		filmToUpdate.setTitle("Updated Title");
		Film updatedFilm = filmJdbcDao.update(filmToUpdate);

		assertEquals("Updated Title", updatedFilm.getTitle());
	}

	@Test(expected =Exception.class)
	public void testDelete() throws Exception {
		// Nos aseguramos que si buscamos el id del item borrado, nuestro código lanza
		// una excepcion
		filmJdbcDao.delete(MockFilmDatabase.PRIMER_ID_JUEGO_PRUEBAS);
		Film deletedFilm = filmJdbcDao.getById(MockFilmDatabase.PRIMER_ID_JUEGO_PRUEBAS);
	
	}

	private Film createMockFilm(String title) {
		Film film = new Film();
		film.setTitle(title);
		film.setDescription("Descripción de la película");
		film.setReleaseYear((short) 2022);
		film.setLanguageId(1);
		film.setRentalDuration(3);
		film.setRentalRate(BigDecimal.valueOf(4.99));
		film.setReplacementCost(BigDecimal.valueOf(19.99));
		film.setRating("PG");
		film.setLastUpdate(new Timestamp(System.currentTimeMillis()));
		Set<String> specialFeatures = new HashSet<>();
		specialFeatures.add("Trailers");
		specialFeatures.add("Commentaries");
		film.setSpecialFeatures(specialFeatures);
		return film;
	}
}
