package main.java.kafka;

import lombok.Getter;

@Getter
public enum MessageType {
    LOCAL_TOPIC("test-topic");

    private final String topicName;

    MessageType(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
