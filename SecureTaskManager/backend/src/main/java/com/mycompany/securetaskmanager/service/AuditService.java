package com.mycompany.securetaskmanager.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    public void log(String message) {
        logger.info("[AUDIT] " + message);
    }
}
