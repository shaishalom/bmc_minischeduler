package com.bmc.minischduler.service;

import com.bmc.minischduler.constant.CommandType;
import com.bmc.minischduler.model.Catalog;
import com.bmc.minischduler.model.SchedulerTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Service
public class XMLReaderService {


    @Value("${xml_file}")
    private String xmlFile;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private XmlMapper xmlMapper;

    //TODO load real XML file
    //do that for json file
    public Catalog readXmlFile() throws IOException {

        Catalog catalog = null;
        try {
            ClassPathResource resource = new ClassPathResource("tasks.xml");
            InputStream is = resource.getInputStream();
            catalog= xmlMapper.readValue(is, Catalog.class);
        }catch(Exception e) {
            catalog = new Catalog(new ArrayList<>());
            SchedulerTask schedulerTask1 = new SchedulerTask("1", "Print Command", CommandType.COMMAND, "echo task 1", "18:30", "");
            SchedulerTask schedulerTask2 = new SchedulerTask("2", "Print Command", CommandType.COMMAND, "echo task 2", "10:00", "");
            catalog.schedulerTask().add(schedulerTask1);
            catalog.schedulerTask().add(schedulerTask2);
            String result = objectMapper.writeValueAsString(catalog);
            xmlMapper.writeValue(new File("target/test1.xml"), catalog);

        }
        return catalog;



//        ClassPathResource resource = new ClassPathResource(xmlFile);
//        XmlMapper xmlMapper = new XmlMapper();
//        InputStream is = resource.getInputStream();
//        Catalog c= xmlMapper.readValue(is, Catalog.class);
//        return c;
    }

}
