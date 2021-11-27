package Disney.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    @ManyToMany
    @JoinTable(name = "Character_films",
            joinColumns = @JoinColumn(name = "films_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "characters_id", referencedColumnName = "id"))
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Character> characters;
    private String title;
    private String image;
    private LocalDate year;
    private float score;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "genre_id")
    private Genre genre;

}
