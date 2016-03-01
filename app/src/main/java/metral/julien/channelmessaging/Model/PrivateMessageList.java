package metral.julien.channelmessaging.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Julien on 01/03/2016.
 */
public class PrivateMessageList implements Serializable {

    private List<PrivateMessage> messages;

    public PrivateMessageList(List<PrivateMessage> messages) {
        this.messages = messages;
    }

    public PrivateMessageList() {
    }

    public List<PrivateMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<PrivateMessage> messages) {
        this.messages = messages;
    }
}
