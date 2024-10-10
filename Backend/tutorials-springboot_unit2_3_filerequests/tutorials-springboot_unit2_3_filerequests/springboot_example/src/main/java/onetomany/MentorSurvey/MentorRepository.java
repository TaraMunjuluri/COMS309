package onetomany.MentorSurvey;

import onetomany.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
  //  Mentor findByUserId(int userId);  // Method to check if a user is already a mentor

    Mentor findByUser(User user);
}
