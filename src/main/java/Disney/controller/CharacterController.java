package Disney.controller;

import Disney.PostResponse;
import Disney.model.Character;
import Disney.model.DTO.CharacterDTO;
import Disney.model.DTO.CharacterDetailDTO;
import Disney.model.DTO.MessageDTO;
import Disney.service.CharacterService;
import Disney.utils.EntityUrlBuilder;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
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
import static Disney.utils.ListMapper.listToDto;
import static Disney.utils.CheckPages.checkPages;
import static Disney.utils.PageHeaders.pageHeaders;


@RestController
@RequestMapping("/api/characters")
public class CharacterController {
    private final CharacterService characterService;
    private final ModelMapper modelMapper;

    @Autowired
    public CharacterController(CharacterService characterService, ModelMapper modelMapper) {
        this.characterService = characterService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<PostResponse> addCharacter(@RequestBody Character newCharacter) {
        newCharacter = characterService.addCharacter(newCharacter);
        PostResponse body = new PostResponse(EntityUrlBuilder.buildURL("api/characters", newCharacter.getId()), HttpStatus.CREATED);
        return ResponseEntity.created(body.getUrl()).body(body);
    }

    @GetMapping
    public ResponseEntity<List<CharacterDTO>> findAll(@And({
            @Spec(path = "name", spec = LikeIgnoreCase.class),
            @Spec(path = "age",spec = Equal.class),
            @Spec(path = "weight",spec = Equal.class),
            @Spec(path = "films.id",spec = In.class)
    }) Specification<Character> spec, Pageable pageable) {
        Page<Character> page = characterService.findAll(spec, pageable);
        checkPages(page.getTotalPages(), pageable.getPageNumber());
        List<CharacterDTO> dtoList = listToDto(modelMapper, page.getContent(), CharacterDTO.class);
        return ResponseEntity.status(page.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
                .headers(pageHeaders(page.getTotalElements(), page.getTotalPages())).body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterDetailDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(characterService.findById(id), CharacterDetailDTO.class) );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CharacterDetailDTO> editCharacter(@PathVariable Integer id,@RequestBody Character character){
        return ResponseEntity.ok(modelMapper.map(characterService.editCharacter(id,character),CharacterDetailDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> deleteCharacter(@PathVariable Integer id){
        characterService.deleteCharacter(id);
        return ResponseEntity.ok(new MessageDTO("Character has been deleted"));
    }


}
