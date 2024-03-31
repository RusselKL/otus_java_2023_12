package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.List;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final List<Message> messages;

    public HistoryListener(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void onUpdated(Message msg) {
        messages.add(msg.clone());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return messages.stream().filter(message -> message.getId() == id).findFirst();
    }
}
