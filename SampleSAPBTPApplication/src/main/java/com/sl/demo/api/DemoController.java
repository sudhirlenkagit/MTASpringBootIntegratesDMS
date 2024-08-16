package com.sl.demo.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpDestination;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationAccessException;
import com.sap.cloud.sdk.cloudplatform.connectivity.exception.DestinationNotFoundException;

@RestController
public class DemoController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/getProducts")
    public String getProducts() {
        String DESTINATION_NAME = "Northwind_Products"; //in productive code, use constants file to 
                                                        //retrieve destination name
        try {

            /* get the destination information from a facade provided by cloud platform */
            HttpDestination destination =
                    DestinationAccessor.getDestination(DESTINATION_NAME).asHttp();
            HttpClient client = HttpClientAccessor.getHttpClient(destination);
            HttpResponse httpResponse = null;
            try {
                httpResponse = client.execute(new HttpGet());
                /* Retrieve response data once the request is successful */
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(httpResponse.getEntity().getContent()));
                    StringBuilder result = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                    return result.toString();
                }
            } catch (IOException e) {
                log.info("IOException: " + e.getMessage());
                return "IOException: " + e.getMessage();
            }
        } catch (DestinationNotFoundException e) {
            /* Destination is not found with specified name */
            log.info("DestinationNotFoundException: " + e.getMessage());
            return "DestinationNotFoundException: " + e.getMessage();
        } catch (DestinationAccessException e) {
            /* Destination cannot be accessed */
            log.info("DestinationAccessException: " + e.getMessage());
            return "DestinationAccessException: " + e.getMessage();
        }
        return null;
    }
}