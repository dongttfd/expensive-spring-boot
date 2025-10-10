package expensive.Expensive.service;

import org.springframework.stereotype.Service;

import expensive.Expensive.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public String getHelloMessage() {
        return messageRepository.getMessage();
    }
}
