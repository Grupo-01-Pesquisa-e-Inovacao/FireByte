import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;

import java.io.IOException;

public class SlackIntegration {
    static void publishMessage(String id, String text) {
        var client = Slack.getInstance().methods();
        try {
            var result = client.chatPostMessage(r -> r
                .token("infelizmente não posso colocar aqui")
                .channel(id)
                .text(text)
            );
            //System.out.println(result);
            System.out.println(String.format("Mensagem enviada ao Slack: %s", text));
        } catch (IOException | SlackApiException e) {
            System.out.println("Algo deu errado ao mandar mensagem no Slack!");
        }
    }
}