package com.jobmatchup.jobmatchup.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.jobmatchup.jobmatchup.entities.chat.ChatRoom;
import com.jobmatchup.jobmatchup.entities.chat.ChatRoomRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(
        String sender,
        String receiver,
        boolean createNewRoomIfNotExists
    ){
        return chatRoomRepository.findBySenderIdAndRecipientId(sender,receiver)
            .map(ChatRoom::getChatId)
            .or(() -> {
                if(createNewRoomIfNotExists){
                    var chatId = createChat(sender, receiver);
                    return Optional.of(chatId);
                }
                return Optional.empty();
            });
    }
    private String createChat(String sender, String receiver) {
        var chatId = String.format("%s_%s", sender, receiver);

        ChatRoom senderRecipient = ChatRoom.builder()
                    .chatId(chatId)
                    .sender(sender)
                    .receiver(receiver)
                    .build();
        ChatRoom recipientSender = ChatRoom.builder()
                    .chatId(chatId)
                    .sender(receiver)
                    .receiver(sender)
                    .build();
        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);
        return chatId;
    }
}
