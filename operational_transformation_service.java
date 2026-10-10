package com.example.docs.service;

import com.example.docs.dto.OperationMessage;
import org.springframework.stereotype.Service;

@Service
public class OTService {

    public synchronized String applyOperation(String originalContent, OperationMessage op) {
        if (originalContent == null) {
            originalContent = "";
        }

        int pos = op.getPosition() != null ? op.getPosition() : 0;
        pos = Math.max(0, Math.min(pos, originalContent.length()));

        if (op.getType() == OperationMessage.Type.INSERT) {
            String toInsert = op.getCharInserted() != null ? op.getCharInserted() : "";
            return originalContent.substring(0, pos) + toInsert + originalContent.substring(pos);
        } else if (op.getType() == OperationMessage.Type.DELETE) {
            if (pos < originalContent.length()) {
                return originalContent.substring(0, pos) + originalContent.substring(pos + 1);
            }
        }
        return originalContent;
    }
}