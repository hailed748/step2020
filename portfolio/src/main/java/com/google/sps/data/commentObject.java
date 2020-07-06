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

package com.google.sps.data;

import java.sql.Timestamp;    
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat; 


public class commentObject {
    String comment;
    Date timestamp;
    String strDate;

    public void setComment(String newComment){
        comment = newComment;
    }
    
    public String getComment(){
        return comment;
    }

    public void setDateTime(Date newTime){
        timestamp = newTime;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");  
        strDate = dateFormat.format(newTime);  
    }

    public Date getTime(){
        return timestamp;
    }

    public String getDate(){
        return strDate;
    }
}
