package com.tejas.incidentplatform.ai;

import com.tejas.incidentplatform.dto.InvestigationResult;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class IncidentAiClient {

    private final ChatClient chatClient;

    public IncidentAiClient(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public InvestigationResult investigate(String context) {

        return chatClient.prompt()
                .system("""
                        You are an incident investigation assistant.

                        Analyze incidents only using the information supplied
                        in the investigation context.

                        Treat the probable root cause as a hypothesis, not a
                        confirmed fact.

                        Do not invent logs, metrics, events, or system behavior
                        that are not present in the supplied context.

                        Recommend concise, actionable diagnostic or remediation
                        steps.
                        """)
                .user(context)
                .call()
                .entity(
                        InvestigationResult.class,
                        spec -> spec
                                .useProviderStructuredOutput()
                                .validateSchema()
                );
    }
}