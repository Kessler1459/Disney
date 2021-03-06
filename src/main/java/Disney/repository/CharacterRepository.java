package Disney.repository;

import Disney.model.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface CharacterRepository extends JpaRepository<Character,Integer>, JpaSpecificationExecutor<Character> {
}
