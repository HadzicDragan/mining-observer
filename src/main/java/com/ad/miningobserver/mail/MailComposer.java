package com.ad.miningobserver.mail;

import com.ad.miningobserver.gpu.control.GpuError;
import com.ad.miningobserver.network.control.LocalNetwork;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class MailComposer {
    
    public static String buildEmailContent() {
        final EmailContent content = new EmailContent();
        content.setDateTime(LocalDateTime.now(ZoneOffset.UTC));
        content.setWorkerName(LocalNetwork.getHostName());
        content.setList(GpuError.getErrorList());
        
        final String emailHeader = MailComposer.builderHeader(
                content.getWorkerName(), 
                content.getDateTime().toString());
        final String list = MailComposer.buildList(content.getList());
        
        final StringBuilder builder = new StringBuilder();
        builder.append(emailHeader)
                .append(HTMLElement.BLANK_PARAGRAPH)
                .append(HTMLElement.BLANK_PARAGRAPH)
                .append(list);
        
        return builder.toString();
    }
    
    private static String builderHeader(final String worker, final String dateTime) {
        final StringBuilder builder = new StringBuilder();
        return builder.append(HTMLElement.H1_BOLD)
                .append(worker)
                .append(" -- ")
                .append(dateTime)
                .append(HTMLElement.H1_CLOSING)
                .toString();
    }
    
    private static String buildList(final List<String> list) {
        final StringBuilder builder = new StringBuilder();
        builder.append(HTMLElement.LIST_CONTAINER_DISC);
        for (String element : list) {
            builder.append(HTMLElement.listItem(element));
        }
        return builder.append(HTMLElement.LIST_CONTAINER_CLOSING).toString();
    }
}
