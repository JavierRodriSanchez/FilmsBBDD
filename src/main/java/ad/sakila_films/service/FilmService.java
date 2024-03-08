package ad.sakila_films.service;

import java.util.List;

import ad.sakila_films.repository.model.Film;

/**
 * Capa de servicio/negocio, donde se realiza la algoritmia de nuestra
 * aplicacion
 */
public interface FilmService {

	List<Film> getAll() throws Exception;

	Film getById(int id) throws Exception;

	int create(Film film) throws Exception;

	Film update(Film film) throws Exception;

	void delete(int id) throws Exception;

	String getAllAsJson() throws Exception;

}
