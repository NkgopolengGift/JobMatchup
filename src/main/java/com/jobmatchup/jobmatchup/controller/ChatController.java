package com.jobmatchup.jobmatchup.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.jobmatchup.jobmatchup.entities.chat.ChatMessage;
import com.jobmatchup.jobmatchup.entities.chat.ChatNotification;
import com.jobmatchup.jobmatchup.service.ChatMessageService;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessages(@Payload ChatMessage chatMessage){
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(chatMessage.getReceiver(),
                       "queue/messages",
                       ChatNotification.builder()
                       .chatId(savedMsg.getChatId())
                       .sender(savedMsg.getSender())
                       .receiver(savedMsg.getReceiver())
                       .content(savedMsg.getContent())
                       .build()
                       );
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
        @PathVariable("senderId") String senderId,
        @PathVariable("recipientId") String recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.finChatMessages(senderId, recipientId));
    }
    
    
}
