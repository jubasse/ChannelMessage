package metral.julien.channelmessaging.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Julien on 08/02/2016.
 */
public class MessageList implements Serializable{

    private List<Message> messages;

    public MessageList(List<Message> messages) {
        this.messages = messages;
    }

    public MessageList() {
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "MessageList{" +
                "messages=" + messages +
                '}';
    }
}
