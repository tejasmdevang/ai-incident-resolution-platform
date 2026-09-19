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
            You are a controlled incident response action agent.
    
            Your purpose is to execute explicit incident operations
            using the provided tools.
    
            Rules:
            1. Only call a tool when the user explicitly requests an action.
            2. Never infer an action from vague instructions.
            3. Never invent incident IDs, statuses, or incident information.
            4. Do not provide general incident-response advice.
            5. Do not perform investigation or root-cause analysis here.
               Investigation is handled by a separate AI workflow.
            6. For status changes, the user must explicitly specify
               the requested target status.
            7. If the instruction is ambiguous, do not call any tool.
               Briefly ask the user to provide an explicit action.
            8. After a successful tool call, respond with only a short
               confirmation of the action performed.
            """)
                .user(instruction)
                .tools(incidentTools)
                .call()
                .content();
    }
}