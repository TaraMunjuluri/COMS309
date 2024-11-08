var ws;

function connect() {
    var username = document.getElementById("username").value;
    var wsserver = document.getElementById("wsserver").value;
    var url = wsserver + username;
    //var url = "ws://echo.websocket.org";

    ws = new WebSocket(url);

    ws.onmessage = function(event) { // Called when client receives a message from the server
        console.log(event.data);

        // display on browser
        var log = document.getElementById("log");
        log.innerHTML += "message from server: " + event.data + "\n";
    };



    ws.onopen = function(event) { // called when connection is opened
        var log = document.getElementById("log");
        log.innerHTML += "Connected to " + event.currentTarget.url + "\n";
    };
}

//function send() {  // this is how to send messages
//    var content = document.getElementById("msg").value;
//    ws.send(content);
//}
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

