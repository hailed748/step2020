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

package com.google.sps;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    if(request.getDuration() > TimeRange.WHOLE_DAY.duration()){
        return Arrays.asList();
    }
    if(events.size() == 0){
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }


    ArrayList<TimeRange> takenTimes = new ArrayList<>();
    for(Event event : events) {
        if(Collections.disjoint(event.getAttendees(),request.getAttendees()) == false){
            takenTimes.add(event.getWhen());
        }
    }


    System.out.println("this takentimes");
    System.out.println(takenTimes);

    int i = 0;
    TimeRange previous = takenTimes.get(0);
    System.out.println(previous);
    ArrayList<TimeRange> takenTimesMerged = new ArrayList<>();
    Collections.sort(takenTimes, TimeRange.ORDER_BY_START);
    System.out.println(takenTimes.size());
    while(i < takenTimes.size()){
        TimeRange currentRange = takenTimes.get(i);
            System.out.println(currentRange);
        if(currentRange.overlaps(previous) == true){
                System.out.println("here");
            int start = previous.start();
            int end = Math.max(currentRange.end(),previous.end());
            previous = TimeRange.fromStartEnd(start,end,false);
                System.out.println(previous);
            i++;
            if(i==takenTimes.size()){
                takenTimesMerged.add(previous);
            }
        }
        else {
            System.out.println("here2");
            takenTimesMerged.add(previous);
            previous = currentRange;
            i ++;
            if(i==takenTimes.size()){
                takenTimesMerged.add(previous);
            }
        }
    }

    System.out.println("this merged");
    System.out.println(takenTimesMerged);

    ArrayList<TimeRange> validTimes = new ArrayList<>();
    if(takenTimesMerged.size()>1){
        int j = 0;
        while(j < takenTimesMerged.size()-1){
            TimeRange currentRange = takenTimesMerged.get(j);
            TimeRange adjRange = takenTimesMerged.get(j+1);
            int newStart = currentRange.end();
            int newEnd = adjRange.start();
            validTimes.add(TimeRange.fromStartEnd(newStart,newEnd,false));
            j++;
        }

        TimeRange firstValid = validTimes.get(0);
        TimeRange lastValid = validTimes.get(validTimes.size()-1);
        int first = firstValid.start();
        int last = lastValid.end();
        validTimes.add(TimeRange.fromStartEnd(0,first,false));
        validTimes.add(TimeRange.fromStartEnd(last,1440,false));
        System.out.println("NO WAYULD BE");
    }else{
        System.out.println("SHOULD BE");
        TimeRange onlyValid = takenTimesMerged.get(0);
        int first = onlyValid.start();
        int last = onlyValid.end();
        validTimes.add(TimeRange.fromStartEnd(0,first,false));
        validTimes.add(TimeRange.fromStartEnd(last,1440,false));
    }

    ArrayList<TimeRange> finalTimes = new ArrayList<>();
    for(TimeRange time : validTimes){
        if(time.duration() >= request.getDuration()){
            finalTimes.add(time);
        }
    }
    
    System.out.println("FINAL");
    System.out.println(finalTimes);
    return finalTimes;
  } 
}
