package com.ad.miningobserver.gpu.boundary;

import com.ad.miningobserver.file.Creator;
import com.ad.miningobserver.gpu.control.GpuCardToThermalMapper;
import com.ad.miningobserver.gpu.control.GpuClientState;
import com.ad.miningobserver.gpu.control.GpuJsonCreator;
import com.ad.miningobserver.gpu.entity.GpuCard;
import com.ad.miningobserver.gpu.entity.GpuCardsUuids;
import com.ad.miningobserver.gpu.entity.GpuErrorStream;
import com.ad.miningobserver.gpu.entity.GpuThermal;
import com.ad.miningobserver.gpu.entity.GpuThermals;
import com.ad.miningobserver.util.FileAndObjectReference;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GpuService {
    
    @Autowired
    private GpuJsonCreator jsonCreator;
    
    @Autowired
    private GpuClient client;

    @Autowired
    private GpuBatchInitiator batchInitiator;

    @Autowired
    private GpuClientState gpuState;

    @PostConstruct
    private void initBatchProcessing() {
        this.batchInitiator.initBatchProcessing();
    }

    
    /**
     * POST {@code GpuCard} list to remote server.
     * If the request was unsuccessful, save the gpu list state to a file
     * that will later on be pushed to remote server.
     * 
     * @param gpuCards {@code List} of gpu cards
     */
    public void publishGpuCards(final List<GpuCard> gpuCards) {
        if (this.gpuState.areGpuUuidsPersisted()) {
            this.pushGpuCardsStatus(gpuCards);
            return;
        }

        final boolean isNotified = this.client
            .postGpuCardsUuids(this.collectGpuCardsUuids(gpuCards));
        if (!isNotified) {
            this.jsonCreator.writeGpuThermalJson(this.collectGpuThermalsToWrapper(gpuCards));
            return;
        }
        this.gpuState.gpuUuidsPersisted();
        this.pushGpuCardsStatus(gpuCards);
    }
    
    /**
     * POST {@code GpuThermal} to the remote server.
     * 
     * @param fileUUID file name without the extension
     */
    public void pushGpuCardsStatus(final List<GpuCard> gpuCards) {
        final List<GpuThermal> gpuThermals = collectGpuThermals(gpuCards);
        final boolean isPosted = this.client.postGpuThermals(gpuThermals);
        if (!isPosted) {
            this.jsonCreator.writeGpuThermalJson(this.collectGpuThermalsToWrapper(gpuCards));
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
        
        final boolean isPosted = this.client.postGpuError(errorStream);
        this.removeFileById(isPosted, fileUUID);
        if (!isPosted) {
    // #TODO send mail because not able to post to remote server.
//      this.mailService.sendMail();
        }
    }

    /**
     * Batch POST the gpu errors to the remote server;
     */
    public void batchGpuErrors() {
        final FileAndObjectReference<GpuErrorStream> fileReference = 
            this.jsonCreator.readGpuErrorStreamFiles();
        if (fileReference.getObjects().isEmpty()) {
            return;
        }

        final boolean isBatchPosted = this.client.batchErrorStream(fileReference.getObjects());
        this.cleanupAfterBatchProcess(isBatchPosted, fileReference.getFiles());
    }

    /**
     * POST {@code GpuThermal} to remote server. 
     * If the request was unsuccessful, email the owner that an error has occurred.
     */
    public void publishCriticalTemperature(final String fileUUID) {
        final GpuThermal gpuThermal = this.jsonCreator.readCriticalGpuThermalJson(fileUUID);
        if (gpuState == null) {
            return;
        }
        final boolean isPosted = this.client.postGpuCriticalThermal(gpuThermal);
        this.removeFileById(isPosted, fileUUID);
        if (!isPosted) {
            // #TODO send mail because not able to post to remote server.
//          this.mailService.sendMail();
        }
    }

    /**
     * batchGpuThermals() && batchGpuTemperatureStatus() uses the same type list
     * this should be implemented with some kind of template pattern
     * #TODO add this functionality inside v2
     */
    
    /**
     * Batch POST the gpu cards status to the remote server;
     */
    public void batchGpuThermals() {
        final FileAndObjectReference<GpuThermal> fileReference = 
            this.jsonCreator.readGpuThermalFiles();
        if (fileReference.getObjects().isEmpty()) {
            return;
        }
        
        final boolean isBatchPosted = this.client.batchGpuTemperatures(fileReference.getObjects());
        this.cleanupAfterBatchProcess(isBatchPosted, fileReference.getFiles());
    }

    /**
     * Batch POST the gpu temperature status to remote server;
     */
    public void batchGpuTemperatureStatus() {
        final FileAndObjectReference<GpuThermal> fileReference = 
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

    private GpuThermals collectGpuThermalsToWrapper(final List<GpuCard> gpuCards) {
        return new GpuThermals(this.collectGpuThermals(gpuCards)); 
    }

    private List<GpuThermal> collectGpuThermals(final List<GpuCard> gpuCards) {
        return gpuCards.stream()
            .map(GpuCardToThermalMapper::cardToThermalInformation)
            .collect(Collectors.toList());
    }

    private GpuCardsUuids collectGpuCardsUuids(final List<GpuCard> gpuCards) {
        final List<String> gpuUuidsList = gpuCards.stream()
            .map(gpu -> gpu.getUUID())
            .collect(Collectors.toList());
        final GpuCardsUuids gpuUuids = new GpuCardsUuids();
        gpuUuids.uuids = gpuUuidsList;
        return gpuUuids;
    }
}