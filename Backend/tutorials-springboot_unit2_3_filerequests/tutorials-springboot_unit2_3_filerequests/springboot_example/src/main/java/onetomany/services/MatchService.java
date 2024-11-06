package onetomany.services;

import onetomany.MenteeSurvey.Mentee;
import onetomany.MentorSurvey.Mentor;
import onetomany.matches.MatchedPair;
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

    public List<MatchedPair> findMatches() {
        List<Mentor> mentors = mentorRepository.findAll();
        List<Mentee> mentees = menteeRepository.findAll();
        List<MatchedPair> matches = new ArrayList<>();

        for (Mentor mentor : mentors) {
            for (Mentee mentee : mentees) {
                // Convert enums to String for comparison
                if (mentor.getAreaOfMentorship().toString().equals(mentee.getAreaOfMenteeship().toString())) {
                    // Convert mentor's area to MatchedPair.Area enum when creating a new MatchedPair
                    MatchedPair.Area matchedArea = MatchedPair.Area.valueOf(mentor.getAreaOfMentorship().toString());
                    MatchedPair match = new MatchedPair(mentor, mentee, matchedArea);
                    matches.add(match);
                }
            }
        }
        return matches;
    }

}
