package com.example.docs.controller;

import com.example.docs.dto.OperationMessage;
import com.example.docs.model.Document;
import com.example.docs.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EditorWebSocketController {

    @Autowired
    private DocumentService documentService;

    @MessageMapping("/edit/{docId}")
    @SendTo("/topic/document/{docId}")
    public OperationMessage handleEditorOperation(@DestinationVariable Long docId, OperationMessage operation) {
        operation.setDocId(docId);
        
        if (operation.getType() == OperationMessage.Type.INSERT || operation.getType() == OperationMessage.Type.DELETE) {
            documentService.processOperation(operation);
        }
        
        return operation;
    }
}