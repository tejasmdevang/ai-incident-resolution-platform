package com.tejas.incidentplatform.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RunbookSearchService {

    private final VectorStore vectorStore;

    public RunbookSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<Document> search(String query) {

        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(3)
                .build();

        return vectorStore.similaritySearch(request);
    }
}