package com.example.docs.dto;

public class OperationMessage {

    public enum Type {
        INSERT, DELETE, JOIN, SYNC
    }

    private Long docId;
    private String sender;
    private Type type;
    private String charInserted;
    private Integer position;
    private Integer version;

    public OperationMessage() {}

    public Long getDocId() { return docId; }
    public void setDocId(Long docId) { this.docId = docId; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public String getCharInserted() { return charInserted; }
    public void setCharInserted(String charInserted) { this.charInserted = charInserted; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
}