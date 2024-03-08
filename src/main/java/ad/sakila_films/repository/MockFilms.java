package ad.sakila_films.repository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import ad.sakila_films.repository.model.Film;

public class MockFilms {

	public static Film getMockFilm() {
		Film newFilm = new Film();
		newFilm.setTitle("Nuevo Film");
		newFilm.setDescription("Descripción del nuevo film");
		newFilm.setReleaseYear((short) 2023);
		newFilm.setLanguageId(1);
		newFilm.setRentalDuration(5);
		newFilm.setRentalRate(new BigDecimal("3.99"));
		newFilm.setReplacementCost(new BigDecimal("14.99"));
		newFilm.setRating("PG");
		Set<String> specialFeatures = new HashSet<>();
		specialFeatures.add("Trailers");
		specialFeatures.add("Commentaries");
		newFilm.setSpecialFeatures(specialFeatures);
		return newFilm;
	}
	
	public static Film getMockFilmUpdate() {
		Film newFilm = new Film();
		newFilm.setTitle("Nuevo Film Actualizado");
		newFilm.setDescription("Descripción del nuevo film");
		newFilm.setReleaseYear((short) 2023);
		newFilm.setLanguageId(1);
		newFilm.setRentalDuration(5);
		newFilm.setRentalRate(new BigDecimal("3.99"));
		newFilm.setReplacementCost(new BigDecimal("14.99"));
		newFilm.setRating("PG");
		Set<String> specialFeatures = new HashSet<>();
		specialFeatures.add("Trailers");
		specialFeatures.add("Commentaries");
		newFilm.setSpecialFeatures(specialFeatures);
		return newFilm;
	}

}
