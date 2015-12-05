package core.messaging;

public enum MessageStatusType {

    NEW, // 0
    SENT, // 1
    QUEUED, // 2
    DELIVERED, // 3
    FAILED, // 4
    DEAD, // 5
    REJECTED, // 6        
    SOFT_ERROR, // 7
    HARD_ERROR          // 8;
}
