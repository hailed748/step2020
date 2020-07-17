package com.google.sps.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.google.sps.data.EntryObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/graph")
public class GraphServlet extends HttpServlet {

  private List<Object> entryList = new ArrayList<>();
  private Text covidData;

  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/nevada.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");
      String[] dateNums = cells[0].split("-");

      Integer year = Integer.parseInt(dateNums[0]);
      Integer month = Integer.parseInt(dateNums[1]);
      Integer day = Integer.parseInt(dateNums[2]);
      Integer cases = Integer.parseInt(cells[1]);
      Integer deaths = Integer.parseInt(cells[2]);

      EntryObject newEntry = new EntryObject(year, month, day, cases, deaths);
      entryList.add(newEntry);
    }
    scanner.close();

    // 1500 bit limit on String objects in Datastore so had to use Text
    Text dataJSON = new Text(makeJSON(entryList));
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
    covidData = (Text) results.get(0).getProperty("dataList");

    String covidDataJSON = makeJSON(covidData);
    response.setContentType("application/json;");
    response.getWriter().println(covidDataJSON);
  }

  private String makeJSON(Object changeItem) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      String jsonString = mapper.writeValueAsString(changeItem);
      return jsonString;
    } catch (Exception e) {
      return "Could not convert to JSON";
    }
  }
}
