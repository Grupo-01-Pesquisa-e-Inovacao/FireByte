import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SlackIntegration {
    static void publishMessage(String id, String text) {
        var client = Slack.getInstance().methods();
        try {
            var result = client.chatPostMessage(r -> r
                .token("xoxb-6182782987507-6182807909619-1XIQXd2P61XmtIhyMbFigTZI")
                .channel(id)
                .text(text)
            );
            System.out.println(String.format("Mensagem enviada ao Slack: %s", text));
        } catch (IOException | SlackApiException e) {
            System.out.println("Algo deu errado ao mandar mensagem no Slack!");
        }
    }
}