package me.ilyamirin.little.hub.invasion;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import java.awt.TextArea;

/**
 *
 * @author ilamirin
 */
public class MessagePipe {

    private TextArea textArea;

    public void push(String message) {
        textArea.append("//n" + message);
    }

}
