package com.ad.miningobserver.gpu.boundary;

import com.ad.miningobserver.file.Creator;
import com.ad.miningobserver.gpu.control.GpuJsonCreator;
import com.ad.miningobserver.gpu.entity.GpuCard;
import com.ad.miningobserver.gpu.entity.GpuCriticalState;
import com.ad.miningobserver.mail.MailService;
import com.ad.miningobserver.operation.batch.BatchOperation;
import com.ad.miningobserver.gpu.entity.GpuErrorStream;
import com.ad.miningobserver.gpu.entity.GpuList;
import com.ad.miningobserver.util.FileAndObjectReference;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GpuService {
    
    @Autowired
    private GpuJsonCreator jsonCreator;
    
    // #TODO add email implementation
    // @Autowired
    // private MailService mailService;
    
    @Autowired
    private GpuClient client;

    @Autowired
    private BatchOperation batchOperation;

    @Autowired
    private GpuBatch gpuBatch;

    @PostConstruct
    private void initBatchProcessing() {
        this.batchOperation.registerBatchJob(gpuBatch);
    }
    
    /**
     * POST {@code GpuCard} list to remote server.
     * If the request was unsuccessful, save the gpu list state to a file
     * that will later on be pushed to remote server.
     * 
     * @param gpuCards {@code List} of gpu cards
     */
    public void publishGpuCards(final List<GpuCard> gpuCards) {
        final GpuList gpuList = new GpuList(gpuCards);
        final boolean isNotified = this.client.postGpuCards(gpuList);
        if (!isNotified) {
            this.jsonCreator.writeGpuListJson(gpuList);
        }
    }
    
    /**
     * POST {@code GpuList} to the remote server.
     * 
     * @param fileUUID file name without the extension
     */
    public void pushGpuCardsStatus(final String fileUUID) {
        final GpuList gpuList = this.jsonCreator.readGpuListJson(fileUUID);
        if (gpuList == null) {
            return;
        }
        
        final boolean isPosted = this.client.postGpuCards(gpuList);
        if (isPosted) {
            Creator.removeGpuListFile(fileUUID);
        }
    }
    
    /**
     * POST {@code GpuErrorStream} to remote server. 
     * If the request was unsuccessful, email the owner that an error has occurred.
     * 
     * @param fileUUID file name without the extension
     */
    public void publishGpuError(final String fileUUID) {
        final GpuErrorStream errorStream = this.jsonCreator.readErrorJson(fileUUID);
        if (errorStream == null) {
            return;
        }
        
        final boolean isPosted = this.client.postErrorStream(errorStream);
        this.removeFileById(isPosted, fileUUID);
        if (!isPosted) {
    // #TODO send mail because not able to post to remote server.
//      this.mailService.sendMail();
        }
    }
    
    /**
     * POST {@code GpuCriticalState} to remote server. 
     * If the request was unsuccessful, email the owner that an error has occurred.
     */
    public void publishCriticalTemperature(final String fileUUID) {
        final GpuCriticalState gpuState = this.jsonCreator.readCriticalStateJson(fileUUID);
        if (gpuState == null) {
            return;
        }
        final boolean isPosted = this.client.postCriticalTemperatureByUUID(gpuState);
        this.removeFileById(isPosted, fileUUID);
        if (!isPosted) {
            // #TODO send mail because not able to post to remote server.
//          this.mailService.sendMail();
        }
    }
    
    /**
     * Batch POST the gpu cards status to the remote server;
     */
    public void batchGpuCardsStatus() {
        final FileAndObjectReference<GpuList> fileReference = this.jsonCreator.readGpuListFiles();
        if (fileReference.getObjects().isEmpty()) {
            return;
        }
        
        final boolean isBatchPosted = this.client.batchGpuCards(fileReference.getObjects());
        this.cleanupAfterBatchProcess(isBatchPosted, fileReference.getFiles());
    }

    /**
     * Batch POST the gpu temperature status to remote server;
     */
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
     * @param remove {@code boolean} flag to remove files
     * @param files {@code List} of files that will be deleted
     */
    private void cleanupAfterBatchProcess(final boolean remove, final List<String> files) {
        if (remove) {
            Creator.removeFiles(files);
        }
    }

    /**
     * Delete file if it was posted to remote server.
     * 
     * @param remove {@code boolean} flag to remove files
     * @param file that will be deleted
     */
    private void removeFileById(final boolean remove, final String file) {
        if (remove) {
            Creator.removeErrorFileByID(file);
        }
    }
}
