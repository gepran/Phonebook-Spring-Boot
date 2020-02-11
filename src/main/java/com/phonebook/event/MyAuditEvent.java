package com.phonebook.event;

import com.phonebook.rest.LoggingRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MyAuditEvent {
    final static Logger log = LoggerFactory.getLogger(MyAuditEvent.class);

    @EventListener
    public void onAuditEvent(AuditApplicationEvent event) {
        AuditEvent auditEvent = event.getAuditEvent();
        log.info("typeprincipal={}", auditEvent.getType(), auditEvent.getPrincipal());
    }
}