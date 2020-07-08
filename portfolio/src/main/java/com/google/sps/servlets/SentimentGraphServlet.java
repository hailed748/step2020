package com.google.sps.servlets;

import java.util.LinkedHashMap;
import java.util.Scanner;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.sps.data.CommentObject;  
import com.google.sps.data.EntryObject;  
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
  

@WebServlet("/sentiment-graph")
public class SentimentGraphServlet extends HttpServlet {

  private List<Object> commentObjectList = new ArrayList<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("comment");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1000));

    for (Entity entity : results) {
       commentObjectList.add(entity.getProperty("commentObject"));
    }

    String commentObjectJSON = makeJSON(commentObjectList);
    response.setContentType("application/json;");
    response.getWriter().println(commentObjectJSON);
  }


  private String makeJSON(Object changeItem){
    try {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(changeItem);
        return jsonString;
    } catch (Exception e){
        return "Can't convert to JSON"; 
    }
  }

}
