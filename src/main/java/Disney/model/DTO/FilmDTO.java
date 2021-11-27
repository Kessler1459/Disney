package Disney.model.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FilmDTO {
    private Integer id;
    private String image;
    private String title;
    private LocalDate year;
}
