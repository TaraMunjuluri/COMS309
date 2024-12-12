package onetomany.PotentialFriends;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PotentialFriendRepository extends JpaRepository<PotentialFriend, Long> {
    List<PotentialFriend> findByUserId(Long userId);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);

    // Add this method to find by both userId and potentialFriendId
    Optional<PotentialFriend> findByUserIdAndPotentialFriendId(Long userId, Long potentialFriendId);
}