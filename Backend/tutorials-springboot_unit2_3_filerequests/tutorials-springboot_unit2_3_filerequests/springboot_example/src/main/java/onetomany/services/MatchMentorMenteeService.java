package onetomany.services;

import onetomany.MenteeSurvey.Mentee;
import onetomany.MentorSurvey.Mentor;
import onetomany.Users.User;
import onetomany.matches.MatchedPairRepository;
import onetomany.MenteeSurvey.MenteeRepository;
import onetomany.MentorSurvey.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchMentorMenteeService {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MenteeRepository menteeRepository;

    @Autowired
    private MatchedPairRepository matchedPairRepository;

    public List<onetomany.matches.MatchedPairMentorMentee> findMatches() {
        List<Mentor> mentors = mentorRepository.findAll();
        List<Mentee> mentees = menteeRepository.findAll();
        List<onetomany.matches.MatchedPairMentorMentee> matches = new ArrayList<>();

        for (Mentor mentor : mentors) {
            for (Mentee mentee : mentees) {
                if (mentor.getAreaOfMentorship().toString().equals(mentee.getAreaOfMenteeship().toString())) {
                    onetomany.matches.MatchedPairMentorMentee.Area matchedArea = onetomany.matches.MatchedPairMentorMentee.Area.valueOf(mentor.getAreaOfMentorship().toString());

                    User mentorUser = mentor.getUser(); // Get the User from Mentor
                    User menteeUser = mentee.getUser(); // Get the User from Mentee

                    // Check if this match already exists by User ID
                    if (!matchedPairRepository.existsByMentorAndMentee(mentorUser, menteeUser)) {
                        onetomany.matches.MatchedPairMentorMentee match = new onetomany.matches.MatchedPairMentorMentee(mentorUser, menteeUser, matchedArea);
                        matchedPairRepository.save(match);
                        matches.add(match);
                    }
                }
            }
        }
        return matches;
    }
}
