package Disney.model.DTO;

import Disney.model.Character;
import Disney.model.Genre;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilmDetailDTO {
    private Integer id;
    private String image;
    private String title;
    private LocalDate year;
    private Genre genre;
    private List<CharacterDTO> characters;
}
