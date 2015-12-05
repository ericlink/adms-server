package core.messaging;

// refeactor protocol/device/content types as below, when used (find usages)
// destination protocol type             : SMS, SMTP, WCTP, VOIP, IVR
// destination device type               : PAGER, EMAIL, CELL, PHONE
// destination preferred content type    : HTML, TXT, VOICE, VIDEO, MMSG, etc. 
public enum DestinationType {

    EMAIL_HTML_ADDRESS, // 0
    EMAIL_TEXT_ADDRESS, // 1
    EMAIL_SMS_ADDRESS, // 2
    EMAIL_PAGER_ADDRESS, // 3
    VOICE_LAND_LINE, // 4
    VOICE_VOIP, // 5
    VOICE_CELL // 6            
    ;
//    public boolean isAttachmentSupported() {
//        return false;
//    }
}
