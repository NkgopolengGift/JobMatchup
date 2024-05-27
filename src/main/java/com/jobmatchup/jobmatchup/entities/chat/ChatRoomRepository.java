package com.jobmatchup.jobmatchup.entities.chat;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{

    Optional<ChatRoom> findBySenderAndReceiver(String sender, String receiver);

}
