package onetomany.MentorSurvey;

import onetomany.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
  //  Mentor findByUserId(int userId);  // Method to check if a user is already a mentor
// In MenteeRepository
  List<Mentor> findByAreaOfMentorship(Mentor.AreaOfMentorship areaOfMentorship);

    Mentor findByUser(User user);
}
