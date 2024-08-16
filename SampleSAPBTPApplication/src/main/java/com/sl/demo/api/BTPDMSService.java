package com.sl.demo.api;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpDestination;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class BTPDMSService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Attaches a generic file to an existing work.
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/uploadFile", produces = "application/json", consumes = "multipart/form-data")
	@ApiOperation(value = "Attaches a generic file to an existing work.")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK", responseContainer = "Map", response = Long.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized, basic auth required"),
			@ApiResponse(code = 403, message = "Forbidden") })
	public String uploadFile(@RequestParam("attachment") MultipartFile file) throws IOException {

		log.info("Upload file started");

		String DESTINATION_NAME = "MyDestinationForDMS";
		byte[] content = file.getBytes();
		String filename = file.getOriginalFilename();
		String contentType = file.getContentType();

		HttpDestination destination = DestinationAccessor.getDestination(DESTINATION_NAME).asHttp();
		HttpClient httpclient = HttpClientAccessor.getHttpClient(destination);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		log.info("Uploading file : {}", filename);
		builder.addTextBody("cmisaction", "createDocument");
		builder.addTextBody("propertyId[0]", "cmis:name");
		builder.addTextBody("propertyValue[0]", "testSudhir01.json");
		builder.addTextBody("propertyId[1]", "cmis:objectTypeId");
		builder.addTextBody("propertyValue[1]", "cmis:document");
		builder.addTextBody("filename", "testSudhir01");
		builder.addTextBody("_charset_", "UTF-8");
		builder.addTextBody("includeAllowableAction", "true");
		builder.addTextBody("succinct", "true");
		builder.addBinaryBody("media", content, ContentType.create(contentType), filename);

		HttpEntity postData = builder.build();
		HttpPost httpPost = new HttpPost();
		httpPost.setEntity(postData);
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		log.info("Response from post File: {}", response.getStatusLine().getStatusCode());
		log.info("******** Response Body **********: {}", EntityUtils.toString(entity));

		return "Executed with status Code:" + response.getStatusLine().getStatusCode();
	}

}