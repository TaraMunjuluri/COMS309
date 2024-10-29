package onetomany.Chatbox;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{
        List<Message> findByGroupId(Long groupId);
        List<Message> findBySenderId(Long senderId);
        List<Message> findByGroupIdOrderByTimestampDesc(Long groupId);
}
