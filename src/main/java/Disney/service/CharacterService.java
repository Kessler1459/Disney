package Disney.service;

import Disney.exception.NotFoundException;
import Disney.model.Character;
import Disney.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {
    private final CharacterRepository characterRepository;

    @Autowired
    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    public Character addCharacter(Character newCharacter) {
        return characterRepository.save(newCharacter);
    }

    public Character findById(Integer id) {
        return characterRepository.findById(id).orElseThrow(() -> new NotFoundException("Character id: "+id+" not found"));
    }

    public Page<Character> findAll(Specification<Character> spec, Pageable pageable) {
        return characterRepository.findAll(spec,pageable);
    }

    public Character editCharacter(Integer id,Character character) {
        findById(id);
        character.setId(id);
        return characterRepository.save(character);
    }

    public void deleteCharacter(Integer id) {
        try{
            characterRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Character id: "+id+" not found");
        }
    }
}
