package ad.sakila_films;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import ad.sakila_films.repository.FilmDao;
import ad.sakila_films.repository.MockFilms;
import ad.sakila_films.repository.impl.FilmJdbcDao;
import ad.sakila_films.repository.model.Film;
import ad.sakila_films.service.FilmService;
import ad.sakila_films.service.impl.FilmServiceImpl;

public class App {

	// capa de servicio que nos permite trabajar con peliculas
	private FilmService filmService;

	private static boolean appActiva = true;

	public static void main(String[] args) {

		App app = new App();
		app.cfgComponentes();

		Scanner scanner = new Scanner(System.in);

		while (appActiva) {
			try {
				int opcion = 0;
				System.out.println("\nSeleccione una opción:");
				System.out.println("1. Crear pelicula");
				System.out.println("2. Leer peliculas");
				System.out.println("3. Actualizar pelicula");
				System.out.println("4. Eliminar pelicula");
				System.out.println("5. Leer peliculas como Json");
				System.out.println("6. Salir");

				System.out.print("Ingrese el número de la opción: ");
				opcion = scanner.nextInt();

				scanner.nextLine();

				app.procesar(opcion, scanner);
			} catch (InputMismatchException ex) {
				appActiva = false;
				System.out.println("No ha elegido una opcion correcta");
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		System.out.println("App cerrada");

	}

	/**
	 * Configuramos los componentes que utilizaremos en nuestra app:
	 * 
	 * DAOs y Services con DAOs inyectados
	 * 
	 */
	private void cfgComponentes() {
		FilmDao filmDao = new FilmJdbcDao();
		filmService = new FilmServiceImpl(filmDao);
	}

	/**
	 * Operativa de la aplicación dónde se realiza el CRUD de peliculas
	 * 
	 * @param opcion  que permite escoger la opción a realizar en el CRUD
	 * @param scanner
	 * @throws Exception
	 */
	private void procesar(int opcion, Scanner scanner) throws Exception {
		int filmId = 0;
		switch (opcion) {
		case 1:
			int id = filmService.create(MockFilms.getMockFilm());
			System.out.println("Pelicula ingresada con id: " + id);
			break;
		case 2:
			System.out.println("Lista de peliculas");
			List<Film> allFilms = filmService.getAll();
			for (Film film : allFilms) {
				System.out.println("film:" + film);
			}
			break;
		case 3:
			System.out.print("Ingrese el ID de la película a actualizar: ");
			filmId = scanner.nextInt();
			scanner.nextLine();

			Film filmUpdate = null;

			filmUpdate = filmService.getById(filmId);

			// Actualizamos el titulo de la pelicula
			filmUpdate.setTitle("title updated");

			filmService.update(filmUpdate);

			System.out.println("Pelicula actualizada con id: " + filmId);

			break;
		case 4:

			System.out.print("Ingrese el ID de la película a eliminar: ");
			filmId = scanner.nextInt();
			filmService.delete(filmId);
			System.out.println("Peliculas eliminada con id: " + filmId);

			break;
		case 5:
			System.out.println("Peliculas como json: ");
			String jsonFilms = filmService.getAllAsJson();
			System.out.println(jsonFilms);
			break;
		case 6:
			appActiva = false;
			break;

		default:
			throw new RuntimeException("Opción no válida");

		}

	}

}
