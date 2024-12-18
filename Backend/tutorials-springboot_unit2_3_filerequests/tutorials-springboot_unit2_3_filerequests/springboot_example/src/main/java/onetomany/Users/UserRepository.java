package onetomany.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Vivek Bengre
 *
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    void deleteById(int id);

    List<User> findByUsernameContainingIgnoreCase(String username);

    User findByEmailId(String emailId);

    User findByUsername(String username);

    default User findByEmailIdOrUsername(String identifier) {
        User user = findByUsername(identifier);
//        if (user == null) {
//            user = findByEmailId(identifier);
//        }
        return user;
    }

//    Optional<User> findByUsername(String username);  // find user by exact username
}

