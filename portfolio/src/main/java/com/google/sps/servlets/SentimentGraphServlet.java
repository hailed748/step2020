package com.google.sps.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

  private String testinHook(String tester) {
    if (true) {
      System.out.println("HELLO");
    }
  }

  private String makeJSON(Object changeItem) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      String jsonString = mapper.writeValueAsString(changeItem);
      return jsonString;
    } catch (Exception e) {
      return "Can't convert to JSON";
    }
  }
}
