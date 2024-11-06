package onetomany.services;

import onetomany.MenteeSurvey.Mentee;
import onetomany.MentorSurvey.Mentor;
import onetomany.Users.User;
import onetomany.matches.MatchedPair;
import onetomany.matches.MatchedPairRepository;
import onetomany.MenteeSurvey.MenteeRepository;
import onetomany.MentorSurvey.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MenteeRepository menteeRepository;

    @Autowired
    private MatchedPairRepository matchedPairRepository;

    public List<MatchedPair> findMatches() {
        List<Mentor> mentors = mentorRepository.findAll();
        List<Mentee> mentees = menteeRepository.findAll();
        List<MatchedPair> matches = new ArrayList<>();

        for (Mentor mentor : mentors) {
            for (Mentee mentee : mentees) {
                if (mentor.getAreaOfMentorship().toString().equals(mentee.getAreaOfMenteeship().toString())) {
                    MatchedPair.Area matchedArea = MatchedPair.Area.valueOf(mentor.getAreaOfMentorship().toString());

                    User mentorUser = mentor.getUser(); // Get the User from Mentor
                    User menteeUser = mentee.getUser(); // Get the User from Mentee

                    // Check if this match already exists by User ID
                    if (!matchedPairRepository.existsByMentorAndMentee(mentorUser, menteeUser)) {
                        MatchedPair match = new MatchedPair(mentorUser, menteeUser, matchedArea);
                        matchedPairRepository.save(match);
                        matches.add(match);
                    }
                }
            }
        }
        return matches;
    }
}
