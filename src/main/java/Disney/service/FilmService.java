package Disney.service;

import Disney.exception.NotFoundException;
import Disney.model.Character;
import Disney.model.Film;
import Disney.model.Genre;
import Disney.repository.FilmRepository;
import Disney.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository, GenreRepository genreRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
    }

    public Genre addGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
    }

    public Genre findGenreById(Integer id) {
        return genreRepository.findById(id).orElseThrow(() -> new NotFoundException("Genre id: " + id + " not found"));
    }

    public Film addFilm(Film film) {
        return filmRepository.save(film);
    }

    public Page<Film> findAll(Specification<Film> spec, Pageable pageable) {
        return filmRepository.findAll(spec, pageable);
    }

    public Film findFilmById(Integer id) {
        return filmRepository.findById(id).orElseThrow(() -> new NotFoundException("Film id: " + id + " not found"));
    }

    public void deleteFilmById(Integer id) {
        try {
            filmRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Film id: " + id + " not found");
        }
    }

    public Film addCharacterToFilm(Film film, Character character) {
        film.getCharacters().add(character);
        return filmRepository.save(film);
    }

    public Film editFilm(Integer id, Film film) {
        findFilmById(id);
        film.setId(id);
        return filmRepository.save(film);
    }

    public Film changeFilmGenre(Film film, Genre genre) {
        film.setGenre(genre);
        return filmRepository.save(film);
    }
}
