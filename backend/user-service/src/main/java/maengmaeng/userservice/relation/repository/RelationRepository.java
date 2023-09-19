package maengmaeng.userservice.relation.repository;

import maengmaeng.userservice.relation.domain.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {
    Optional<Relation> findByFromIdAndToId(String from, String to);

    int deleteByFromIdAndToId(String fromId, String toId);

    Boolean existsByFromIdAndToId(String from, String to);

    List<Relation> findAllByFromId(String from);


}
