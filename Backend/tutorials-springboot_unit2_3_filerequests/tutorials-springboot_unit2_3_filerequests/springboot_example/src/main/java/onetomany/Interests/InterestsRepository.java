package onetomany.Interests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterestsRepository extends JpaRepository<Interests, Long> {

    Optional<Interests> findByName(String name);

    @Query(value = "SELECT ui2.user_id as friend_id, COUNT(DISTINCT ui2.interest_id) as shared_count " +
            "FROM DB309.user_interests ui1 " +
            "JOIN DB309.user_interests ui2 ON ui1.interest_id = ui2.interest_id " +
            "WHERE ui1.user_id = :userId AND ui2.user_id != :userId " +
            "GROUP BY ui2.user_id " +
            "HAVING COUNT(DISTINCT ui2.interest_id) >= 1",
            nativeQuery = true)
    List<Object[]> findUsersWithSharedInterests(@Param("userId") Long userId);
}