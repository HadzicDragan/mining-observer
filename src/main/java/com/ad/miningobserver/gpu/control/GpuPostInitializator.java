package com.ad.miningobserver.gpu.control;

import com.ad.miningobserver.NameReference;
import com.ad.miningobserver.gpu.boundary.GpuService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn({
    NameReference.BOOT_INITIATOR_BEAN, 
    NameReference.SCHEDULED_FILE_BEAN
})
public class GpuPostInitializator {
    
    @Autowired
    private GpuService service;
    
    private static final boolean USE = false;
    
    @PostConstruct
    public void initiateBatch() {
        if (USE) {
            this.service.batchGpuCardsStatus();
            this.service.batchGpuTemperatureStatus();
        }
    }
}
