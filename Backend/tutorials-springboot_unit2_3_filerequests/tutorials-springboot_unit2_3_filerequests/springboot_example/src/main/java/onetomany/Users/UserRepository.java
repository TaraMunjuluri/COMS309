package onetomany.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Remove the conflicting findById method since it's already provided by JpaRepository
    // Instead use the Optional<User> return type if you need a custom query

    void deleteById(Long id);
    List<User> findByUsernameContainingIgnoreCase(String username);
    User findByEmailId(String emailId);
    User findByUsername(String username);

    default User findByEmailIdOrUsername(String identifier) {
        User user = findByUsername(identifier);
        if (user == null) {
            user = findByEmailId(identifier);
        }
        return user;
    }
}

//package onetomany.Users;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
///**
// *
// * @author Vivek Bengre
// *
// */
//
//public interface UserRepository extends JpaRepository<User, Long> {
//    User findById(int id);
//    void deleteById(int id);
//
//    List<User> findByUsernameContainingIgnoreCase(String username);
//
//    User findByEmailId(String emailId);
//
//    User findByUsername(String username);
//
//    default User findByEmailIdOrUsername(String identifier) {
//        User user = findByUsername(identifier);
////        if (user == null) {
////            user = findByEmailId(identifier);
////        }
//        return user;
//    }
//
//}
//
