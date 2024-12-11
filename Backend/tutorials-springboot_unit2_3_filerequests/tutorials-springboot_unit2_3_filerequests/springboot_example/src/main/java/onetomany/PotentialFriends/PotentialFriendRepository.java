package onetomany.Friends;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PotentialFriendRepository extends JpaRepository<onetomany.Friends.PotentialFriend, Long> {
    List<onetomany.Friends.PotentialFriend> findByUserId(Long userId);
    @Transactional
    void deleteByUserId(Long userId);
}