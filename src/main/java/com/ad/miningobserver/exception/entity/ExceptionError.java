package com.ad.miningobserver.exception.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import com.ad.miningobserver.util.CurrentTime;

public class ExceptionError {

    private String errorMessage;
    private String className;
    private String methodName;
    private Object objValue;
    private LocalDateTime currentDateTime;

    public ExceptionError() {
        this.currentDateTime = CurrentTime.currentTimeUTC();
    }

    public ExceptionError(
            String errorMessage, 
            String className, 
            String methodName,
            LocalDateTime reported) {
        this.currentDateTime = reported;
        this.errorMessage = errorMessage;
        this.className = className;
        this.methodName = methodName;
    }

    public ExceptionError(
            String errorMessage, 
            String className, 
            String methodName, 
            Object objValue,
            LocalDateTime reported) {
        this.currentDateTime = reported;
        this.errorMessage = errorMessage;
        this.className = className;
        this.methodName = methodName;
        this.objValue = objValue;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getObjValue() {
        return objValue;
    }

    public void setObjValue(Object objValue) {
        this.objValue = objValue;
    }

    @JsonProperty(value = "reportedDate")
    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public static class Builder {
        
        private String errorMessage;
        private String className;
        private String methodName;
        private Object objValue;
        // private LocalDateTime currentDateTime;

        public Builder errorMessage(final String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder className(final String className) {
            this.className = className;
            return this;
        }

        public Builder methodName(final String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder objValue(final Object objValue) {
            this.objValue = objValue;
            return this;
        }
        
        public ExceptionError build() {
            return new ExceptionError(
                    this.errorMessage, 
                    this.className, 
                    this.methodName, 
                    this.objValue,
                    CurrentTime.currentTimeUTC()
            );
        }
    }
}
