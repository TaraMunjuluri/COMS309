var ws;
let typingTimeout;
const typingInterval = 1000; // 1 second delay for "TYPING_STOP"

// Function to connect to the WebSocket server
function connect() {
    var username = document.getElementById("username").value;
    var wsserver = document.getElementById("wsserver").value;
    var url = wsserver + username;

    ws = new WebSocket(url);

    ws.onmessage = function(event) { // Called when client receives a message from the server
        console.log("Message from server:", event.data);

        // Display on browser
        var log = document.getElementById("log");
        log.innerHTML += "message from server: " + event.data + "\n";
    };

    ws.onopen = function(event) { // Called when connection is opened
        var log = document.getElementById("log");
        log.innerHTML += "Connected to " + event.currentTarget.url + "\n";
    };
}

// Send typing notification
function sendTypingNotification(isTyping) {
    if (isTyping) {
        console.log("Sending TYPING_START");
        ws.send("TYPING_START");
    } else {
        console.log("Sending TYPING_STOP");
        ws.send("TYPING_STOP");
    }
}

// Function to send messages
function send() {
    var content = document.getElementById("msg").value;
    if (content.trim() !== "") {
        console.log("Sending message:", content);
        ws.send(content);
        document.getElementById("msg").value = ""; // Clear input
        sendTypingNotification(false); // Stop typing notification when a message is sent
    }
}

// Detect typing in the message input field
document.getElementById("msg").addEventListener("input", () => {
    clearTimeout(typingTimeout);
    sendTypingNotification(true); // Notify typing started

    typingTimeout = setTimeout(() => {
        sendTypingNotification(false); // Notify typing stopped after delay
    }, typingInterval);
});


@Controller
public class MatchNotificationController {

    @GetMapping("/matches/notifications")
    public String getMatchNotificationsPage() {
        // This will look for src/main/resources/static/matches/index.html
        return "matches/index";
    }
}


function send() {
    var content = document.getElementById("msg").value;
    if (content.trim() !== "") {
        console.log("Sending message: ", content); // Log the message being sent
        ws.send(content);
        document.getElementById("msg").value = ""; // Clear input
    }
}

