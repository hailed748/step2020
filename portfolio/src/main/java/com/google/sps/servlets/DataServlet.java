// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.sps.data.commentObject;  
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;  
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter("comment");
    Timestamp ts = new Timestamp(System.currentTimeMillis()); 
    Date date = new Date(ts.getTime());

    commentObject myComment = new commentObject();
    myComment.setComment(comment);
    myComment.setDateTime(date);

    String myCommentJSON = makeJSON(myComment);

    Entity taskEntity = new Entity("comment");
    taskEntity.setProperty("commentObject", myCommentJSON);
    taskEntity.setProperty("timestamp",myComment.getTime());

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);
    response.sendRedirect("/index.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<String> commentList = new ArrayList<>();
    
    for (Entity entity : results.asIterable()) {
      String currentComment = (String) entity.getProperty("commentObject");
      commentList.add(currentComment);
    }

    String commentListJSON = makeJSON(commentList);
    response.setContentType("application/json;");
    response.getWriter().println(commentListJSON);
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
