package onetomany.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);
    void deleteById(int id);

    List<User> findByUsernameContainingIgnoreCase(String username);
}
