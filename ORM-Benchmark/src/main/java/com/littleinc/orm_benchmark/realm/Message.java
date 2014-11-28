package com.littleinc.orm_benchmark.realm;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class Message extends RealmObject {
    private int commandId;
    private long sortedBy;
    private String content;
    private long clientId;
    private long senderId;
    private long channelId;
    private int createdAt;

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public long getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(long sortedBy) {
        this.sortedBy = sortedBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }
}
