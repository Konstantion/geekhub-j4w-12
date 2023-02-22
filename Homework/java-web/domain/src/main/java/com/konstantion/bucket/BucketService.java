package com.konstantion.bucket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
public record BucketService(Logger logger) {
   
    public BucketService() {
        this(LoggerFactory.getLogger(BucketService.class));
    }
    public void addProductToBucket() {

    }
}
