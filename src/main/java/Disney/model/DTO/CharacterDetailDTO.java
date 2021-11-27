package Disney.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CharacterDetailDTO {
    private Integer id;
    private String image;
    private String name;
    private Integer age;
    private String history;
    private float weight;
    private List<FilmDTO> films;
}
