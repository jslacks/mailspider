package com.gumirov.shamil.partsib.endpoints;

import org.apache.camel.Consume;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Header;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * (c) 2017 by Shamil Gumirov (shamil@gumirov.com).<br/>
 * Date: 4/1/2017 Time: 23:20<br/>
 */
public class OutputSender {

  protected Logger log = LoggerFactory.getLogger(getClass()); 
  
  private String url;

  public OutputSender(String url){
    this.url = url;
  }
  
  @Consume(uri = "direct:output")
  public void onOutput(@Header("filename") String filename) throws IOException {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    
    try {
      HttpPost httppost = new HttpPost(url);
      File f = new File(filename);
      InputStreamEntity reqEntity = new InputStreamEntity(
          new FileInputStream(f), -1, ContentType.APPLICATION_OCTET_STREAM);
      reqEntity.setChunked(true);
      httppost.setEntity(reqEntity);

      System.out.println("Executing request: " + httppost.getRequestLine());
      CloseableHttpResponse response = httpclient.execute(httppost);
      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
      } finally {
        response.close();
      }
    } catch (FileNotFoundException e) {
      log.info("[OutputSenderEndpoint] Error: cannot find file to send: "+filename, e);
    } catch (IOException e) {
      log.info("[OutputSenderEndpoint] IOError: cannot send file: "+filename, e);
    } finally {
      httpclient.close();
    }
  }
}
