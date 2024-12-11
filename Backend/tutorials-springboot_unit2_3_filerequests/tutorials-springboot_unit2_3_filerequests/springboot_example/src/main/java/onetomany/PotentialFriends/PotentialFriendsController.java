//package onetomany.PotentialFriends;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import onetomany.ExceptionHandlers.UserNotFoundException;
//import onetomany.Interests.Interests;
//import onetomany.Users.User;
//import onetomany.Users.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//
//@RestController
//@RequestMapping("/api/potential-friends")
//public class PotentialFriendsController {
//
//    @Autowired
//    private PotentialFriendsRepository potentialFriendsRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Operation(summary = "Update the potential friends list for a user")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Potential friends updated successfully"),
//            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
//    })
//    @GetMapping("/update/{userId}")
//    public ResponseEntity<?> updatePotentialFriends(
//            @Parameter(description = "ID of the user to update potential friends for", required = true)
//            @PathVariable long userId) {
//
//        User user = userRepository.findById(userId);
//        if (user == null) {
//            throw new UserNotFoundException("User not found with ID: " + userId);
//        }
//        // Clear existing potential friends
//        potentialFriendsRepository.deleteByUserId(userId);
//
//        // Get current user's interests
//        Set<Interests> userInterests = user.getInterests();
//        List<String> userInterestsList = new ArrayList<>();
//        for (Interests interest : userInterests) {
//            userInterestsList.addAll(interest.getInterests());
//        }
//
//        // Find potential friends
//        List<User> allUsers = userRepository.findAll();
//        for (User otherUser : allUsers) {
//            if (otherUser.getId() != userId) {
//                Set<String> commonInterests = new HashSet<>();
//
//                // Find common interests
//                for (Interests otherInterest : otherUser.getInterests()) {
//                    for (String interest : otherInterest.getInterests()) {
//                        if (userInterestsList.contains(interest)) {
//                            commonInterests.add(interest);
//                        }
//                    }
//                }
//
//                // If there are common interests, create a potential friend
//                if (!commonInterests.isEmpty()) {
//                    PotentialFriends potentialFriend = new PotentialFriends(userId, otherUser.getId(), commonInterests);
//                    potentialFriend.setUser(user);
//                    potentialFriend.setPotentialFriend(otherUser);
//                    potentialFriendsRepository.save(potentialFriend);
//                }
//            }
//        }
//
//        return getPotentialFriends(userId);
//    }
//
//    @Operation(summary = "Retrieve the potential friends list for a user")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Potential friends retrieved successfully"),
//            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
//    })
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<?> getPotentialFriends(
//            @Parameter(description = "ID of the user to retrieve potential friends for", required = true)
//            @PathVariable long userId) {
//
//        List<PotentialFriends> potentialFriends = potentialFriendsRepository
//                .findByUserIdOrderByCommonInterestCountDesc(userId);
//
//        List<Map<String, Object>> response = new ArrayList<>();
//        for (PotentialFriends pf : potentialFriends) {
//            Map<String, Object> pfData = new HashMap<>();
//            User potentialFriend = pf.getPotentialFriend();
//
//            pfData.put("id", pf.getId());
//            pfData.put("userId", pf.getUserId());
//            pfData.put("potentialFriendId", pf.getPotentialFriendId());
//            pfData.put("name", potentialFriend.getName());
//            pfData.put("major", potentialFriend.getMajor());
//            pfData.put("classification", potentialFriend.getClassification());
//            pfData.put("commonInterests", pf.getCommonInterests());
//            pfData.put("commonInterestCount", pf.getCommonInterestCount());
//
//            response.add(pfData);
//        }
//
//        return ResponseEntity.ok(response);
//    }
//
//    @Operation(summary = "Delete a potential friend by ID")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Potential friend deleted successfully"),
//            @ApiResponse(responseCode = "404", description = "Potential friend not found", content = @Content)
//    })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deletePotentialFriend(
//            @Parameter(description = "ID of the potential friend record to delete", required = true)
//            @PathVariable Long id) {
//
//        potentialFriendsRepository.deleteById(id);
//        return ResponseEntity.ok().build();
//    }
//}
