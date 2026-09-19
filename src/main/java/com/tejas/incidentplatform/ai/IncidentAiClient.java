package com.tejas.incidentplatform.ai;

import com.tejas.incidentplatform.dto.InvestigationResult;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import com.tejas.incidentplatform.ai.tools.IncidentTools;

@Component
public class IncidentAiClient {

    private final ChatClient chatClient;
    private final IncidentTools incidentTools;

    public IncidentAiClient(ChatClient.Builder builder, IncidentTools incidentTools) {
        this.chatClient = builder.build();
        this.incidentTools = incidentTools;
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
    
    public String runAgent(String instruction) {

        return chatClient.prompt()
                .system("""
                        You are an incident response agent.
    
                        You may use the provided tools when an incident
                        operation is required.
    
                        Never invent incident IDs or incident states.
                        Only perform actions explicitly requested by the user.
                        """)
                .user(instruction)
                .tools(incidentTools)
                .call()
                .content();
    }
}