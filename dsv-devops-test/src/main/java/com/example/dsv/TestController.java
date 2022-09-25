package com.example.dsv;

import Constants.Constants;
import Entities.SampleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@RestController
public class TestController {

    @Value("${org.name}")
    private String url;
    @Value("${org.type}")
    private String orgType;
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/")
    public ResponseEntity<SampleResponse> home(HttpServletRequest request) {
        LOGGER.info("Request Initiated for "+url);
        SampleResponse response = new SampleResponse(Constants.MESSAGE, Constants.OK, new Date(), request.getRequestURL().toString(), request.getMethod(),orgType.toString());
        LOGGER.info("response is {} "+response.toString());
        return ResponseEntity.ok(response);
    }
}
