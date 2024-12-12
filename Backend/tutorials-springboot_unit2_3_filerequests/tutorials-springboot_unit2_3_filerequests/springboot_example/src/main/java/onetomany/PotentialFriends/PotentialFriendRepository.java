package onetomany.PotentialFriends;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PotentialFriendRepository extends JpaRepository<PotentialFriend, Long> {
    List<PotentialFriend> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    Optional<PotentialFriend> findByUserIdAndPotentialFriendId(Long userId, Long potentialFriendId);
}