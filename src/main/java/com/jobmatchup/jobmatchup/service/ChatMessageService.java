package com.jobmatchup.jobmatchup.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jobmatchup.jobmatchup.entities.chat.ChatMessage;
import com.jobmatchup.jobmatchup.entities.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage){
        var chatId = chatRoomService.getChatRoomId(
                            chatMessage.getSender(), 
                            chatMessage.getReceiver(), 
                            true)
            .orElseThrow();
            chatMessage.setChatId(chatId);
            
            return repository.save(chatMessage);
    }

    public List<ChatMessage> finChatMessages(
        String senderId, String recepientID
    ){
        var chatId = chatRoomService.getChatRoomId(senderId, recepientID, false);

        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
}
