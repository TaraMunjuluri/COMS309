package onetomany.MenteeSurvey;

import onetomany.MentorSurvey.Mentor;
import onetomany.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenteeRepository extends JpaRepository<onetomany.MenteeSurvey.Mentee, Long> {
  //  Mentor findByUserId(int userId);  // Method to check if a user is already a mentor

    Mentee findByUser(User user);
}
