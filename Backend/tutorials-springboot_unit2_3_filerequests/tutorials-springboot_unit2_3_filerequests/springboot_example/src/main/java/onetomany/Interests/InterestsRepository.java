package onetomany.Interests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestsRepository extends JpaRepository<Interests, Long> {
    List<Interests> findByMajor(String major);
    List<Interests> findByClassification(String classification);
}