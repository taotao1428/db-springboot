package com.hewutao.db.model.support;

import com.hewutao.db.model.Entity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class RollbackEntityListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void listenForRollback(SaveEntityEvent event) {
        Entity entity = event.getEntity();
        log.info("rollback entity [{}:{}]", entity.getClass().getName(), entity.getId());
        event.getEntity().innerRollback();
    }
}
