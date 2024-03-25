package com.bmc.minischduler.service;

import com.bmc.minischduler.model.Catalog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class JSONReaderService {


    @Value("${json_file}")
    private String jsonFile;

    @Autowired
    private ObjectMapper objectMapper;


    public Catalog readXmlFile() throws IOException {

        Catalog catalog = null;
        try {
            ClassPathResource resource = new ClassPathResource(jsonFile);
            InputStream is = resource.getInputStream();
            catalog= objectMapper.readValue(is, Catalog.class);
        }catch(Exception e) {

        }
        return catalog;
    }

}
