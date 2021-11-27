package Disney.controller;

import Disney.PostResponse;
import Disney.model.Character;
import Disney.model.DTO.FilmDTO;
import Disney.model.DTO.FilmDetailDTO;
import Disney.model.DTO.FilmInputDTO;
import Disney.model.DTO.MessageDTO;
import Disney.model.Film;
import Disney.model.Genre;
import Disney.service.CharacterService;
import Disney.service.FilmService;
import Disney.utils.EntityUrlBuilder;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static Disney.utils.CheckPages.checkPages;
import static Disney.utils.ListMapper.listToDto;
import static Disney.utils.PageHeaders.pageHeaders;

@RestController
@RequestMapping("/api/films")
public class FilmController {
    private final FilmService filmService;
    private final CharacterService characterService;
    private final ModelMapper modelMapper;

    @Autowired
    public FilmController(FilmService filmService, CharacterService characterService, ModelMapper modelMapper) {
        this.filmService = filmService;
        this.characterService = characterService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<PostResponse> addFilm(@RequestBody FilmInputDTO filmInputDTO) {
        Genre genre = filmService.findGenreById(filmInputDTO.getGenreId());
        Film newFilm = modelMapper.map(filmInputDTO, Film.class);
        newFilm.setGenre(genre);
        newFilm = filmService.addFilm(newFilm);
        PostResponse body = new PostResponse(EntityUrlBuilder.buildURL("api/films", newFilm.getId()), HttpStatus.CREATED);
        return ResponseEntity.created(body.getUrl()).body(body);
    }

    @GetMapping
    public ResponseEntity<List<FilmDTO>> findAllFilms(@And({
            @Spec(path = "title", spec = LikeIgnoreCase.class),
            @Spec(path = "genre.id", params = "genre", spec = Equal.class)
    }) Specification<Film> spec, Pageable pageable) {
        Page<Film> page = filmService.findAll(spec, pageable);
        checkPages(page.getTotalPages(), pageable.getPageNumber());
        List<FilmDTO> dtoList = listToDto(modelMapper, page.getContent(), FilmDTO.class);
        return ResponseEntity.status(page.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
                .headers(pageHeaders(page.getTotalElements(), page.getTotalPages())).body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDetailDTO> findFilmById(@PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(filmService.findFilmById(id), FilmDetailDTO.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilmDetailDTO> editFilm(@PathVariable Integer id,@RequestBody Film film){
        return ResponseEntity.ok(modelMapper.map(filmService.editFilm(id,film), FilmDetailDTO.class));
    }

    @PutMapping("/{filmId}/genre/{genreId}")
    public ResponseEntity<FilmDetailDTO> changeFilmGenre(@PathVariable Integer filmId,@PathVariable Integer genreId){
        Genre genre=filmService.findGenreById(genreId);
        Film film = filmService.findFilmById(filmId);
        return ResponseEntity.ok(modelMapper.map(filmService.changeFilmGenre(film,genre),FilmDetailDTO.class));
    }

    @PutMapping("/{filmId}/characters/{characterId}")
    public ResponseEntity<FilmDetailDTO> addCharacterToFilm(@PathVariable Integer filmId,@PathVariable Integer characterId){
        Character character=characterService.findById(characterId);
        Film film = filmService.findFilmById(filmId);
        return ResponseEntity.ok(modelMapper.map(filmService.addCharacterToFilm(film,character),FilmDetailDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> deleteFilm(@PathVariable Integer id) {
        filmService.deleteFilmById(id);
        return ResponseEntity.ok(new MessageDTO("Film has been deleted"));
    }

    @PostMapping("/genres")
    public ResponseEntity<PostResponse> addGenre(@RequestBody Genre genre) {
        genre = filmService.addGenre(genre);
        PostResponse body = new PostResponse(EntityUrlBuilder.buildURL("api/films/genres", genre.getId()), HttpStatus.CREATED);
        return ResponseEntity.created(body.getUrl()).body(body);
    }

    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> findAllGenres() {
        List<Genre> genres = filmService.findAllGenres();
        return ResponseEntity.status(genres.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(genres);
    }
}
