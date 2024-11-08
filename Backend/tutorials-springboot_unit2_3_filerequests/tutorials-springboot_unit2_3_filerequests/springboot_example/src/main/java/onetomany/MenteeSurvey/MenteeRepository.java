package onetomany.MenteeSurvey;

import onetomany.MenteeSurvey.Mentee;
import onetomany.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import onetomany.MenteeSurvey.Mentee.AreaOfMenteeship;

import java.util.List;

public interface MenteeRepository extends JpaRepository<onetomany.MenteeSurvey.Mentee, Long> {
  //  Mentor findByUserId(int userId);  // Method to check if a user is already a mentor
  List<Mentee> findByAreaOfMenteeship(String areaOfMenteeship);
    // In MenteeRepository
    List<Mentee> findByAreaOfMenteeship(AreaOfMenteeship areaOfMenteeship);

    Mentee findByUser(User user);
}
