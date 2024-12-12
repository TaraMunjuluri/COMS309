package onetomany.Friends;  // Change package to match entity

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PotentialFriendRepository extends JpaRepository<onetomany.Friends.PotentialFriend, Long> {
    List<onetomany.Friends.PotentialFriend> findByUserId(Long userId);

    @Transactional
    void deleteByUserId(Long userId);

    Optional<onetomany.Friends.PotentialFriend> findByUserIdAndPotentialFriendId(Long userId, Long friendId);
}