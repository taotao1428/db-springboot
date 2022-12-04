package com.hewutao.db.controller;

import com.hewutao.db.dao.base.mapper.InstancePOMapper;
import com.hewutao.db.dao.base.model.InstancePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class TestTransactionalEventController {
    private Service1 service1;
    private InstancePOMapper instancePOMapper;


    @GetMapping("/test/event/{error}")
    public String test(@PathVariable Integer error) {
        InstancePO notExistedInstance = new InstancePO();
        notExistedInstance.setId(UUID.randomUUID().toString());
        notExistedInstance.setMode("ha");
        notExistedInstance.setName("sdfsf");
        notExistedInstance.setEngineId("sdfsdf");
        notExistedInstance.setStatus("normal");

        Entity notExistedEntity = new Entity(notExistedInstance, false, false);

        InstancePO existedInstance = instancePOMapper.selectByPrimaryKey("180339a2-6b23-43c9-981b-033a61860969");

        existedInstance.setEngineId(UUID.randomUUID().toString());

        Entity existedEntity= new Entity(existedInstance, true, false);


        try {
            log.info("start to save entity");
            service1.insert(error, notExistedEntity, existedEntity);
            log.info("save entity success, {}, {}", notExistedEntity.committed, existedEntity.committed);
        } catch (Exception e) {
            log.error("save entity failed, {}, {}", notExistedEntity.committed, existedEntity.committed, e);
        }




        return "success";
    }


    @Component
    @AllArgsConstructor
    public static class Service1 {
        private InstancePOMapper instancePOMapper;

        private Service2 service2;

        private ApplicationEventPublisher applicationEventPublisher;

        @Transactional
        public void insert(int error, Entity notExistedEntity, Entity existedEntity) {

            InstancePO instancePO = notExistedEntity.instancePO;

            log.info("begin insert data, {}", instancePO.getId());
            instancePOMapper.insert(instancePO);
            log.info("end insert data, {}", instancePO.getId());
            notExistedEntity.committed = true;
            applicationEventPublisher.publishEvent(new InsertInstanceEvent(notExistedEntity));

            service2.insert(error, existedEntity);
        }
    }

    @Component
    @AllArgsConstructor
    public static class Service2 {
        private InstancePOMapper instancePOMapper;
        private ApplicationEventPublisher applicationEventPublisher;
        @Transactional
        public void insert(int error, Entity existedEntity) {
            InstancePO instancePO = existedEntity.instancePO;

            log.info("begin update data, {}", instancePO.getId());
            instancePOMapper.updateByPrimaryKey(instancePO);
            log.info("end update data, {}", instancePO.getId());
            existedEntity.committed = true;
            applicationEventPublisher.publishEvent(new InsertInstanceEvent(existedEntity));
            if (error > 1) {
                throw new IllegalStateException("sdf");
            }

        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void eventListener(InsertInstanceEvent event) {
        log.info("[commit] trigger insert instance event. id: {}", event.getEntity().getInstancePO().getId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void eventListener2(InsertInstanceEvent event) {
        log.info("[rollback] trigger insert instance event. id: {}", event.getEntity().getInstancePO().getId());
        event.getEntity().committed = false;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InsertInstanceEvent {
        private Entity entity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EntityState{
        private Object object;
        private boolean committed;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Entity {
        private InstancePO instancePO;

        private boolean existed;

        private boolean committed;
    }
}
