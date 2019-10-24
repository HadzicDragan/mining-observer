package com.ad.miningobserver.gpu.boundary;

import com.ad.miningobserver.file.Creator;
import com.ad.miningobserver.gpu.control.GpuJsonCreator;
import com.ad.miningobserver.gpu.entity.GpuCard;
import com.ad.miningobserver.gpu.entity.GpuCriticalState;
import com.ad.miningobserver.mail.MailService;
import com.ad.miningobserver.gpu.entity.GpuErrorStream;
import com.ad.miningobserver.gpu.entity.GpuList;
import com.ad.miningobserver.util.FileAndObjectReference;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GpuService {
    
    @Autowired
    private GpuJsonCreator jsonCreator;
    
    @Autowired
    private MailService mailService;
    
    @Autowired
    private GpuClient client;
    
    public boolean updateGpuTemperatures() {
        final boolean areUpdated = this.client.postGPUTemperatures();
        if (!areUpdated) {
            // send a new GPUOperation to log the temperatures for that time
            // until the internet connection is back up
            // this should change the behaviour of logging to not be so active
            // meaning you should log every time, but when posting to the client
            // only on a interval that is set should be updated
            // meaning. Update the 1 and then 3 or 5 in the row, this can be
            // configured remotetly and set on the Container side. Client should
            // have a default one if it can not reach the Container at startup,
            // but if the Observer is able to reach the Container GET the GPU
            // update interval.
        }
        return false;
    }
    
    public void notifyGpuCards(final List<GpuCard> gpuCards) {
        final boolean isNotified = this.client.postGPUTemperatures();
        if (!isNotified) {
            final GpuList gpuList = new GpuList();
            gpuList.setGPUCards(gpuCards);
            this.jsonCreator.writeGpuListJson(gpuList);
        }
    }
    
    public void pushGpuCardsStatus(final String fileUUID) {
        final GpuList gpuList = this.jsonCreator.readGpuListJson(fileUUID);
        if (gpuList == null) {
            return;
        }
        
        final boolean isPosted = this.client.postGPUCards(gpuList);
        if (isPosted) {
            Creator.removeGPUFileById(fileUUID);
        }
    }
    
    public void gpuErrorOperationActions(final String fileUUID) {
        final GpuErrorStream errorStream = this.jsonCreator.readErrorJson(fileUUID);
        if (errorStream == null) {
            return;
        }
        
        final boolean isPosted = this.client.postErrorStream(errorStream);
        if (!isPosted) {
            return;
        }
        
        final boolean isFileRemoved = Creator.removeErrorFileByID(fileUUID);
        if (!isFileRemoved) {
            // do not send the email to the owner
            return;
        }
        
//        this.mailService.sendMail();
    }
    
    public void notifyCriticalTemperature(final String gpuUUID) {
        final GpuCriticalState gpuState = new GpuCriticalState(gpuUUID);
        final boolean isPosted = this.client.postCriticalTemperatureByUUID(gpuState);
        if (isPosted) {
            return;
        }
        
        final String uuid = this.jsonCreator.writeCriticalTemperatureJson(gpuState);
        // need to save file to storage
        // push to schedular to pick it up later
    }
    
    public void batchGpuCardsStatus() {
        final FileAndObjectReference<GpuList> fileReference = this.jsonCreator.readGpuListFiles();
        if (fileReference.getObjects().isEmpty()) {
            return;
        }
        
        final boolean isBatchPosted = this.client.batchGpuCards(fileReference.getObjects());
        this.cleanupAfterBatchProcess(isBatchPosted, fileReference.getFiles());
    }

    public void batchGpuTemperatureStatus() {
        final FileAndObjectReference<GpuCriticalState> fileReference = 
                this.jsonCreator.readTemperatureFiles();
        if (fileReference.getObjects().isEmpty()) {
            return;
        }
        
        final boolean isBatchPosted = this.client.batchGpuTemperatures(fileReference.getObjects());
        this.cleanupAfterBatchProcess(isBatchPosted, fileReference.getFiles());
    }
    
    /**
     * Delete the directory files if they have been posted to the remote server.
     * 
     * @param isPosted {@code boolean} flag to remove files
     * @param files {@code List} of files that will be deleted
     */
    private void cleanupAfterBatchProcess(final boolean isPosted, final List<String> files) {
        if (isPosted) {
            Creator.removeFiles(files);
        }
    }
}
