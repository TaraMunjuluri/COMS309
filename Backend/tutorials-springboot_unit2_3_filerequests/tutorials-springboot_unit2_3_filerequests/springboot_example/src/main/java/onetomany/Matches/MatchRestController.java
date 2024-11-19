package onetomany.Matches;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import onetomany.MenteeSurvey.Mentee;
import onetomany.MenteeSurvey.MenteeRepository;
import onetomany.MentorSurvey.Mentor;
import onetomany.MentorSurvey.MentorRepository;
import onetomany.Users.User;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchRestController {

    @Autowired
    private MatchedPairRepository matchedPairRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MenteeRepository menteeRepository;

    @Operation(summary = "Retrieve matches for a given user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matches retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{userId}")
    public List<MatchedPersonDTO> getMatchedPersons(
            @Parameter(description = "ID of the user to retrieve matches for", required = true)
            @PathVariable("userId") Long userId) {

        List<MatchedPersonDTO> matchedPersonDTOList = new ArrayList<>();

        // Retrieve the user or throw a custom exception if not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Find matches where the user is a mentor
        List<MatchedPairMentorMentee> matchesAsMentor = matchedPairRepository.findByMentor(user);
        for (MatchedPairMentorMentee match : matchesAsMentor) {
            User matchedMenteeUser = match.getMentee();
            Mentee matchedMentee = menteeRepository.findByUser(matchedMenteeUser);

            if (matchedMentee != null) {
                matchedPersonDTOList.add(new MatchedPersonDTO(
                        matchedMenteeUser.getUsername(),
                        matchedMentee.getClassification().toString(),
                        matchedMentee.getMajor()
                ));
            }
        }

        // Find matches where the user is a mentee
        List<MatchedPairMentorMentee> matchesAsMentee = matchedPairRepository.findByMentee(user);
        for (MatchedPairMentorMentee match : matchesAsMentee) {
            User matchedMentorUser = match.getMentor();
            Mentor matchedMentor = mentorRepository.findByUser(matchedMentorUser);

            if (matchedMentor != null) {
                matchedPersonDTOList.add(new MatchedPersonDTO(
                        matchedMentorUser.getUsername(),
                        matchedMentor.getClassification().toString(),
                        matchedMentor.getMajor()
                ));
            }
        }

        return matchedPersonDTOList;
    }
}
