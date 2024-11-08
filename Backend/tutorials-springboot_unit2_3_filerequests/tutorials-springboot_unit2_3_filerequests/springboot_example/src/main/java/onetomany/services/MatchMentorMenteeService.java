//package onetomany.services;
//
//import onetomany.MenteeSurvey.Mentee;
//import onetomany.MentorSurvey.Mentor;
//import onetomany.Users.User;
//import onetomany.matches.MatchedPairRepository;
//import onetomany.MenteeSurvey.MenteeRepository;
//import onetomany.MentorSurvey.MentorRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class MatchMentorMenteeService {
//
//    @Autowired
//    private MentorRepository mentorRepository;
//
//    @Autowired
//    private MenteeRepository menteeRepository;
//
//    @Autowired
//    private MatchedPairRepository matchedPairRepository;
//
//
//    public List<onetomany.matches.MatchedPairMentorMentee> findNewMatches() {
//        List<Mentor> mentors = mentorRepository.findAll();
//        List<Mentee> mentees = menteeRepository.findAll();
//        List<onetomany.matches.MatchedPairMentorMentee> newMatches = new ArrayList<>();
//
//        for (Mentor mentor : mentors) {
//            for (Mentee mentee : mentees) {
//                if (mentor.getAreaOfMentorship().toString().equals(mentee.getAreaOfMenteeship().toString())) {
//                    onetomany.matches.MatchedPairMentorMentee.Area matchedArea = onetomany.matches.MatchedPairMentorMentee.Area.valueOf(mentor.getAreaOfMentorship().toString());
//
//                    User mentorUser = mentor.getUser();
//                    User menteeUser = mentee.getUser();
//
//                    if (!matchedPairRepository.existsByMentorAndMentee(mentorUser, menteeUser)) {
//                        onetomany.matches.MatchedPairMentorMentee newMatch = new onetomany.matches.MatchedPairMentorMentee(mentorUser, menteeUser, matchedArea);
//                        matchedPairRepository.save(newMatch);
//                        newMatches.add(newMatch);
//                    }
//                }
//            }
//        }
//        return newMatches;
//    }
//
//}
package onetomany.services;

import onetomany.MenteeSurvey.Mentee;
import onetomany.MentorSurvey.Mentor;
import onetomany.Users.User;
import onetomany.Matches.MatchedPairRepository;
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

    public List<onetomany.Matches.MatchedPairMentorMentee> findNewMatches() {
        List<Mentor> mentors = mentorRepository.findAll();
        List<Mentee> mentees = menteeRepository.findAll();
        List<onetomany.Matches.MatchedPairMentorMentee> newMatches = new ArrayList<>();

        System.out.println("Found " + mentors.size() + " mentors and " + mentees.size() + " mentees");

        for (Mentor mentor : mentors) {
            for (Mentee mentee : mentees) {
                System.out.println("Checking mentor area: " + mentor.getAreaOfMentorship() +
                        " with mentee area: " + mentee.getAreaOfMenteeship());

                if (mentor.getAreaOfMentorship().toString().equals(mentee.getAreaOfMenteeship().toString())) {
                    System.out.println("Found matching area for mentor: " + mentor.getUser().getUsername() +
                            " and mentee: " + mentee.getUser().getUsername());

                    onetomany.Matches.MatchedPairMentorMentee.Area matchedArea =
                            onetomany.Matches.MatchedPairMentorMentee.Area.valueOf(mentor.getAreaOfMentorship().toString());

                    User mentorUser = mentor.getUser();
                    User menteeUser = mentee.getUser();

                    if (!matchedPairRepository.existsByMentorAndMentee(mentorUser, menteeUser)) {
                        System.out.println("Creating new match between mentor: " + mentorUser.getUsername() +
                                " and mentee: " + menteeUser.getUsername());

                        onetomany.Matches.MatchedPairMentorMentee newMatch =
                                new onetomany.Matches.MatchedPairMentorMentee(mentorUser, menteeUser, matchedArea);
                        matchedPairRepository.save(newMatch);
                        newMatches.add(newMatch);
                    } else {
                        System.out.println("Match already exists between mentor: " + mentorUser.getUsername() +
                                " and mentee: " + menteeUser.getUsername());
                    }
                }
            }
        }
        return newMatches;
    }
}