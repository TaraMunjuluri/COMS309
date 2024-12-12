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

    @Query("SELECT DISTINCT i.userId FROM Interests i WHERE i.name IN (SELECT i2.name FROM Interests i2 WHERE i2.userId = :userId) AND i.userId <> :userId")
    List<Long> findPotentialFriendIds(@Param("userId") Long userId);

    @Query("SELECT i.userId as friendId, COUNT(i.name) as sharedCount " +
            "FROM Interests i " +
            "WHERE i.name IN (SELECT i2.name FROM Interests i2 WHERE i2.userId = :userId) " +
            "AND i.userId <> :userId " +
            "GROUP BY i.userId " +
            "HAVING COUNT(i.name) >= 1")
    List<Object[]> findUsersWithSharedInterests(@Param("userId") Long userId);
}
