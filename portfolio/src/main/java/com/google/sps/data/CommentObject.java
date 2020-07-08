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

public class CommentObject {
    String comment;
    Date timestamp;
    float sentiment;

    public CommentObject(String newComment, Date newTime, float score) {
        comment = newComment;
        timestamp = newTime;
        sentiment = score;
    }
    public String getComment(){
        return comment;
    }

    public Date getTime(){
        return timestamp;
    }
    
    public float getSentiment(){
        return sentiment;
    }
}

