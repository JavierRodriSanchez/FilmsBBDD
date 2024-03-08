package ad.sakila_films.repository;

import java.util.List;

/**
 * Esta interfaz nos obliga a implementar todos los metodos habituales de un
 * CRUD
 * 
 * @param <T>
 */
public interface GenericDao<T, ID extends Number> {

	/**
	 * Obtiene todos las peliculas.
	 *
	 * @return Lista de peliculas.
	 * @throws Exception Si ocurre un error durante la operación.
	 */
	List<T> getAll() throws Exception;

	/**
	 * Obtiene una pelicula por su ID.
	 *
	 * @param id  de la pelicula a buscar.
	 * @return La pelicula encontrada
	 * @throws Exception Si ocurre un error durante la operación. Sino encuentra la
	 *                   pelicula indicada
	 */
	T getById(ID id) throws Exception;

	/**
	 * Crea una nueva pelicula
	 *
	 * @param t Pelicula a crear.
	 * @return El id generado por la BBDD
	 * @throws Exception Si ocurre un error durante la operación
	 */
	ID create(T t) throws Exception;

	/**
	 * Actualiza la información de una pelicula existente
	 *
	 * @param t Pelicula con la información actualizada.
	 * @return La pelicula actualizada.
	 * @throws Exception Si ocurre un error durante la operación o sino se ha
	 *                   actualizado un registro
	 */
	T update(T t) throws Exception;

    /**
     * Elimina un empleado por su ID.
     *
     * @param id de la pelicula a borrar
     * @throws Exception Si ocurre un error durante la operación
     * 					 o sino se ha borrado ningun registro
     * 					 
     */
	void delete(ID id) throws Exception;

}
