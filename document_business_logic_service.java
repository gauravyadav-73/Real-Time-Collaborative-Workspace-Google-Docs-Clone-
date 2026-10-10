package com.example.docs.service;

import com.example.docs.dto.OperationMessage;
import com.example.docs.model.Document;
import com.example.docs.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private OTService otService;

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document createDocument(String title) {
        Document doc = new Document(title, "");
        return documentRepository.save(doc);
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public synchronized Document processOperation(OperationMessage op) {
        Optional<Document> optionalDoc = documentRepository.findById(op.getDocId());
        if (optionalDoc.isPresent()) {
            Document doc = optionalDoc.get();
            String updatedContent = otService.applyOperation(doc.getContent(), op);
            doc.setContent(updatedContent);
            doc.setLastModified(LocalDateTime.now());
            return documentRepository.save(doc);
        }
        throw new RuntimeException("Document with ID " + op.getDocId() + " not found.");
    }
}