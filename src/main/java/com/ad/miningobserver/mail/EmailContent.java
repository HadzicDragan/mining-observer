package com.ad.miningobserver.mail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmailContent {

    private String workerName;
    private LocalDateTime dateTime;
    private List<String> list;

    public EmailContent() {
    }

    public EmailContent(String workerName, LocalDateTime dateTime, List<String> list) {
        this.workerName = workerName;
        this.dateTime = dateTime;
        this.list = list;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<String> getList() {
        if (this.list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public class Builder {

        private String workerName;
        private LocalDateTime dateTime;
        private List<String> list;

        public Builder workerName(final String workerName) {
            this.workerName = workerName;
            return this;
        }

        public Builder dateTime(final LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Builder list(final List<String> list) {
            this.list = list;
            return this;
        }

        public EmailContent build() {
            return new EmailContent(this.workerName, this.dateTime, this.list);
        }
    }
}
