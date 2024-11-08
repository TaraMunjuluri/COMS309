package onetomany.websockets;

import onetomany.Users.UserRepository;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import javax.websocket.Session;


public abstract class WebSocketBase {
    protected final String name;
    protected final boolean print_messages;

    public WebSocketBase(String name, boolean print_messages) {
        this.name = name;
        this.print_messages = print_messages;
    }

    protected abstract UserRepository getUserRepo();
    protected abstract Map<Session, Long> getSessionUserIds();
    protected abstract Map<Long, SessionInfo> getUserIdSessions();

    protected void onOpenBase(Session session, Long id) throws IOException {
        if(session == null) {
            System.out.println("[" + name + "]: Invalid session on open!");
            return;
        }
        if(id == null) {
            System.out.println("[" + name + "]: Invalid id on open!");
            return;
        }
        if(getUserRepo().findById(id).isEmpty()) {
            System.out.println("[" + name + "]: Invalid user id on open!");
            return;
        }
        getSessionUserIds().put(session, id);
        getUserIdSessions().put(id, new SessionInfo(session));
        if(print_messages) System.out.println("[" + name + "]: Opened session for user id " + id);
    }

    protected void onCloseBase(Session session) throws IOException {
        if(session == null) return;
        Long id = getSessionUserIds().get(session);
        if(id != null) getUserIdSessions().remove(id);
        getSessionUserIds().remove(session);
        if(print_messages) System.out.println("[" + name + "]: Closed session for user id " + id);
    }

    protected void onErrorBase(Session session, Throwable throwable) {
        System.out.println("[" + name + "]: Error occurred in session!");
        System.out.println(throwable.getMessage());
        if(session == null) return;
        Long id = getSessionUserIds().get(session);
        if(id != null) getUserIdSessions().remove(id);
        getSessionUserIds().remove(session);
    }

    protected static class SessionInfo {
        public final Session session;
        public SessionInfo(Session s) { this.session = s; }
    }
}