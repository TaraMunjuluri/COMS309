package onetomany.Matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class MentorMenteeActivityController {

    @Autowired
    private onetomany.Matches.MentorMenteeActivityRepository activityRepository;

    @Autowired
    private MatchedPairRepository matchedPairRepository;

    @PostMapping("/log-meeting")
    public ResponseEntity<?> logMeeting(
            @RequestParam Long matchId,
            @RequestParam String notes,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime meetingDate) {
        try {
            MatchedPairMentorMentee matchedPair = matchedPairRepository.findById(matchId)
                    .orElseThrow(() -> new RuntimeException("Match not found"));

            onetomany.Matches.MentorMenteeActivity activity = new onetomany.Matches.MentorMenteeActivity();
            activity.setMatchedPair(matchedPair);
            activity.setMeetingNotes(notes);
            activity.setMeetingDate(meetingDate);
            activity.setLastInteraction(LocalDateTime.now());
            activity.setInteractionType(onetomany.Matches.MentorMenteeActivity.InteractionType.MEETING);

            activityRepository.save(activity);
            return ResponseEntity.ok("Meeting logged successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/history/{matchId}")
    public ResponseEntity<?> getActivityHistory(@PathVariable Long matchId) {
        try {
            MatchedPairMentorMentee matchedPair = matchedPairRepository.findById(matchId)
                    .orElseThrow(() -> new RuntimeException("Match not found"));

            List<onetomany.Matches.MentorMenteeActivity> activities = activityRepository
                    .findByMatchedPairOrderByMeetingDateDesc(matchedPair);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}