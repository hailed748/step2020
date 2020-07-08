package com.google.sps.servlets;

import java.util.LinkedHashMap;
import java.util.Scanner;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.sps.data.commentObject;  
import com.google.sps.data.entryObject;  
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;  
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.google.appengine.api.datastore.Text;
  

@WebServlet("/graph")
public class GraphServlet extends HttpServlet {

  private List<Object> entryList = new ArrayList<>();
  private Text covidData;

  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/nevada.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");
      String[] dateNums = cells[0].split("-");

      Integer year = Integer.parseInt(dateNums[0]);
      Integer month = Integer.parseInt(dateNums[1]);
      Integer day = Integer.parseInt(dateNums[2]);
      Integer cases = Integer.parseInt(cells[1]);
      Integer deaths = Integer.parseInt(cells[2]);

      entryObject newEntry = new entryObject(year, month, day, cases, deaths);
      entryList.add(newEntry);
    }
    scanner.close();

    //1500 bit limit on String objects in Datastore so had to use Text
    Text dataJSON = new Text(makeJSON(entryList));
    System.out.println(dataJSON); 

    Entity dataEntity = new Entity("GraphData");
    dataEntity.setProperty("dataList", dataJSON);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(dataEntity);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("GraphData");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
    
    for (Entity entity : results) {
        covidData = (Text) entity.getProperty("dataList");
    }

    String covidDataJSON = makeJSON(covidData);
    response.setContentType("application/json;");
    response.getWriter().println(covidDataJSON);
  }


  private String makeJSON(Object changeItem){
    try {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(changeItem);
        return jsonString;
    } catch (Exception e){
        return null; 
    }
  }

}