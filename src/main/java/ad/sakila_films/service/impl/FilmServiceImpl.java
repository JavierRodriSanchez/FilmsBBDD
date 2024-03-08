package ad.sakila_films.service.impl;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ad.sakila_films.repository.FilmDao;
import ad.sakila_films.repository.model.Film;
import ad.sakila_films.service.FilmService;


public class FilmServiceImpl implements FilmService {

	
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    

	private FilmDao filmDao;
	
	public FilmServiceImpl(FilmDao filmDao) {
		this.filmDao = filmDao;
	}
	
	@Override
	public List<Film> getAll() throws Exception {
		return filmDao.getAll();	
	}

	@Override
	public Film getById(int id) throws Exception {
		return filmDao.getById(id);
	}

	@Override
	public int create(Film film) throws Exception {
		 int id = filmDao.create(film);
		 return id;
	}

	@Override
	public Film update(Film film) throws Exception {
		Film filmUpdated = filmDao.update(film);
		return filmUpdated;
	}

	@Override
	public void delete(int id) throws Exception {
		filmDao.delete(id);
	}

	
	@Override
	public String getAllAsJson() throws Exception {
		List<Film> films = filmDao.getAll();
		String jsonFilms = gson.toJson(films);
		return jsonFilms;
	}

}
