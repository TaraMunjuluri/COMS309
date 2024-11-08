package onetomany.Matches;

import onetomany.MenteeSurvey.Mentee;
import onetomany.MentorSurvey.Mentor;
import onetomany.Users.User;
import onetomany.MenteeSurvey.MenteeRepository;
import onetomany.MentorSurvey.MentorRepository;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matches")
public class MatchRestController {

    @Autowired
    private onetomany.Matches.MatchedPairRepository matchedPairRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MenteeRepository menteeRepository;

    @GetMapping("/{userId}")
    public List<onetomany.Matches.MatchedPersonDTO> getMatchedPersons(@PathVariable("userId") Long userId) {
        List<onetomany.Matches.MatchedPersonDTO> matchedPersonDTOList = new ArrayList<>();
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return matchedPersonDTOList; // or throw an error
        }

        User user = userOpt.get();

        // Find matches where the user is a mentor
        List<onetomany.Matches.MatchedPairMentorMentee> matchesAsMentor = matchedPairRepository.findByMentor(user);
        for (onetomany.Matches.MatchedPairMentorMentee match : matchesAsMentor) {
            User matchedMenteeUser = match.getMentee();
            Optional<Mentee> matchedMenteeOpt = Optional.ofNullable(menteeRepository.findByUser(matchedMenteeUser));

            matchedMenteeOpt.ifPresent(matchedMentee -> matchedPersonDTOList.add(new onetomany.Matches.MatchedPersonDTO(
                    matchedMenteeUser.getUsername(),
                    matchedMentee.getClassification().toString(),
                    matchedMentee.getMajor()
            )));
        }

        // Find matches where the user is a mentee
        List<onetomany.Matches.MatchedPairMentorMentee> matchesAsMentee = matchedPairRepository.findByMentee(user);
        for (onetomany.Matches.MatchedPairMentorMentee match : matchesAsMentee) {
            User matchedMentorUser = match.getMentor();
            Optional<Mentor> matchedMentorOpt = Optional.ofNullable(mentorRepository.findByUser(matchedMentorUser));

            matchedMentorOpt.ifPresent(matchedMentor -> matchedPersonDTOList.add(new onetomany.Matches.MatchedPersonDTO(
                    matchedMentorUser.getUsername(),
                    matchedMentor.getClassification().toString(),
                    matchedMentor.getMajor()
            )));
        }

        return matchedPersonDTOList;
    }
}
