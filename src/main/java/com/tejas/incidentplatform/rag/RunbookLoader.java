package com.tejas.incidentplatform.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RunbookLoader implements CommandLineRunner {

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    public RunbookLoader(VectorStore vectorStore, JdbcTemplate jdbcTemplate) {
        this.vectorStore = vectorStore;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM vector_store",
                Integer.class
        );

        if (count != null && count > 0) {
            System.out.println("Runbooks already loaded. Skipping seed.");
            return;
        }

        List<Document> runbooks = List.of(

                new Document(
                        """
                        Database Connection Pool Exhaustion Runbook

                        Symptoms:
                        - HikariCP reports connection acquisition timeouts.
                        - Active connections reach the configured maximum.
                        - Idle connections remain at zero.
                        - API requests experience increased latency or timeouts.

                        Investigation:
                        - Check for long-running database queries and transactions.
                        - Inspect HikariCP active, idle, and pending connection metrics.
                        - Enable leak detection to identify connections that are not returned.
                        - Review application code for improperly closed connections.

                        Remediation:
                        - Resolve slow queries or long-running transactions.
                        - Fix connection leaks before increasing pool size.
                        - Increase maxPoolSize only after confirming database capacity.
                        - Use controlled restarts only as temporary mitigation.
                        """,
                        Map.of(
                                "title", "Database Connection Pool Exhaustion",
                                "category", "database",
                                "technology", "HikariCP"
                        )
                ),

                new Document(
                        """
                        JVM High CPU Runbook

                        Symptoms:
                        - Application CPU usage remains consistently high.
                        - Request latency increases.
                        - JVM threads consume excessive CPU.

                        Investigation:
                        - Capture thread dumps during the CPU spike.
                        - Identify threads consuming the most CPU.
                        - Inspect garbage collection activity.
                        - Look for infinite loops or expensive computations.

                        Remediation:
                        - Optimize CPU-intensive code paths.
                        - Fix runaway threads.
                        - Tune JVM or garbage collection settings when appropriate.
                        - Scale service instances temporarily if required.
                        """,
                        Map.of(
                                "title", "JVM High CPU",
                                "category", "runtime",
                                "technology", "JVM"
                        )
                ),

                new Document(
                        """
                        JVM Memory Pressure and OutOfMemory Runbook

                        Symptoms:
                        - OutOfMemoryError appears in application logs.
                        - Heap usage remains near maximum.
                        - Garbage collection becomes frequent or lengthy.
                        - Application instances restart unexpectedly.

                        Investigation:
                        - Capture and analyze a heap dump.
                        - Review garbage collection logs and heap metrics.
                        - Identify objects retaining excessive memory.
                        - Check for unbounded caches or collections.

                        Remediation:
                        - Fix memory leaks and unbounded data structures.
                        - Configure cache limits.
                        - Tune heap size only after understanding memory usage.
                        - Restart affected instances only as temporary mitigation.
                        """,
                        Map.of(
                                "title", "JVM Memory Pressure",
                                "category", "runtime",
                                "technology", "JVM"
                        )
                ),

                new Document(
                        """
                        Downstream Service Timeout Runbook

                        Symptoms:
                        - Requests fail while calling another service.
                        - Connect or read timeout errors appear in logs.
                        - Upstream API latency increases.

                        Investigation:
                        - Check downstream service health and latency.
                        - Review network connectivity and timeout metrics.
                        - Inspect recent downstream deployments.
                        - Check retry behavior and dependency error rates.

                        Remediation:
                        - Restore or scale the unhealthy downstream service.
                        - Configure appropriate connection and read timeouts.
                        - Use bounded retries with backoff.
                        - Apply circuit breaking where appropriate.
                        """,
                        Map.of(
                                "title", "Downstream Service Timeout",
                                "category", "network",
                                "technology", "HTTP"
                        )
                ),

                new Document(
                        """
                        Kafka Consumer Lag Runbook

                        Symptoms:
                        - Kafka consumer lag continuously increases.
                        - Events are processed significantly later than produced.
                        - Consumer throughput is lower than producer throughput.

                        Investigation:
                        - Inspect consumer-group lag by partition.
                        - Check consumer errors and processing latency.
                        - Verify partition distribution across consumers.
                        - Inspect downstream dependencies used during event processing.

                        Remediation:
                        - Fix slow event-processing logic.
                        - Scale consumers when partition count allows.
                        - Resolve failing downstream dependencies.
                        - Review batch size and consumer configuration.
                        """,
                        Map.of(
                                "title", "Kafka Consumer Lag",
                                "category", "messaging",
                                "technology", "Kafka"
                        )
                )
        );

        vectorStore.add(runbooks);

        System.out.println("Seeded " + runbooks.size() + " incident runbooks.");
    }
}