package Disney.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class FilmInputDTO {
    private String title;
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate year;
    private float score;
    private String image;
    private Integer genreId;
}
