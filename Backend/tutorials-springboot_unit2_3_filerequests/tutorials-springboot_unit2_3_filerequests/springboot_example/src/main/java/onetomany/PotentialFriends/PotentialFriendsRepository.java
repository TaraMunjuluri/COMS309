//package onetomany.PotentialFriends;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//
//@Repository
//public interface PotentialFriendsRepository extends JpaRepository<PotentialFriends, Long> {
//    List<PotentialFriends> findByUserIdOrderByCommonInterestCountDesc(long userId);
//    void deleteByUserId(long userId);
//}