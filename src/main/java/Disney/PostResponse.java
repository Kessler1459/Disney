package Disney;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import java.net.URI;

@Data
@AllArgsConstructor
public class PostResponse {
    private URI url;
    private HttpStatus status;
}
