package ad.sakila_films.repository;

import java.util.List;
import java.util.Set;

import ad.sakila_films.repository.model.Film;

/**
 * Capa de acceso a la base de datos
 */
public interface FilmDao extends GenericDao<Film, Integer>{

	/**
	 * Devuelve el numero total de peliculas
	 * 
	 * @return
	 * @throws Exception Si ocurre un error durante la operacion
	 */
	int countAllFilms() throws Exception;

	/**
	 * Buscar las peliculas que contenga la palabra actor recibida como argumento
	 * 
	 * @param actor palabra a buscar
	 * @param order asc / desc
	 * @return Lista de peliculas encontradas 
	 *         Una lista vacia si no encuentra ninguno
	 * @throws Exception Si ocurre un error durante la operacion
	 */
	Set<Film> getAllFilmsByActor(String actor, boolean order) throws Exception;
	

	/**
	 * Busca la peliculas que cumplan los siguientes criterios.
	 * 
	 * Estos criterios son opcionales, es decir, el usuario puede indicar rating, length o rentalDuration
	 * o cualquier combinacion de ellos.
	 * 
	 * @param rating
	 * @param length
	 * @param rentalDuration
	 * @return Lista de peliculas encontradas 
	 *         Una lista vacia si no encuentra ninguno
	 */
	List<Film> searchFilms(String rating, Integer length, Integer rentalDuration) throws Exception;
	
	
}
