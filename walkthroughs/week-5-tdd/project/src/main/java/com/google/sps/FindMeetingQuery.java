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

  private ArrayList<TimeRange> getMergedTimes(ArrayList<TimeRange> takenTimes) {
    Collections.sort(takenTimes, TimeRange.ORDER_BY_START);
    ArrayList<TimeRange> takenTimesMerged = new ArrayList();
    if (takenTimes.size() != 0) {
      TimeRange previous = takenTimes.get(0);

      for (TimeRange time : takenTimes) {
        if (time.overlaps(previous)) {
          int start = previous.start();
          int end = Math.max(time.end(), previous.end());
          previous = TimeRange.fromStartEnd(start, end, false);
        } else {
          takenTimesMerged.add(previous);
          previous = time;
        }
      }
      takenTimesMerged.add(previous);
    }
    return takenTimesMerged;
  }

  private ArrayList<TimeRange> getBetweenTimes(ArrayList<TimeRange> takenTimesMerged) {
    TimeRange emptyAtStart = TimeRange.fromStartDuration(TimeRange.START_OF_DAY,0);
    TimeRange emptyAtEnd = TimeRange.fromStartDuration(TimeRange.END_OF_DAY + 1, 0);
    takenTimesMerged.add(0, emptyAtStart);
    takenTimesMerged.add(emptyAtEnd);
    
    ArrayList<TimeRange> validTimes = new ArrayList();
    for (int i = 0; i < takenTimesMerged.size() - 1; i++) {
      TimeRange currentRange = takenTimesMerged.get(i);
      TimeRange adjRange = takenTimesMerged.get(i + 1);
      int newStart = currentRange.end();
      int newEnd = adjRange.start();
      validTimes.add(TimeRange.fromStartEnd(newStart, newEnd, false));
    }
    return validTimes;
  }

  private ArrayList<TimeRange> filterTimes(ArrayList<TimeRange> validTimes, MeetingRequest request) {
    ArrayList<TimeRange> finalTimes = new ArrayList();
    for (TimeRange time : validTimes) {
      if (time.duration() >= request.getDuration()) {
          finalTimes.add(time);
      }
    }
    return finalTimes;
  }


  /**
   * Parameters: a collection of events occuring that day, and a meeting request containings
   * a time for the meeting, duration, and attendence list of manadatory and optional attenedees
   * a time for the meeting,
   * 
   * Response: Based on the events happeing on the day, query will find and return time ranges
   * in which the meeting can be hosted because no mandatory members will have a conflict.
   * It will try to factor in optional attendees so long as they do not cause the mandatory
   * members to not attend. 
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
        return Arrays.asList();
    }
    if (events.isEmpty()) {
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    ArrayList<TimeRange> takenTimes = new ArrayList();
    ArrayList<TimeRange> takenTimesOptional = new ArrayList();
    for (Event event : events) {
      if (!Collections.disjoint(event.getAttendees(), request.getAttendees())) {
          takenTimes.add(event.getWhen());
        }
      if (!Collections.disjoint(event.getAttendees(), request.getOptionalAttendees())) {
          takenTimesOptional.add(event.getWhen());
        }
    }

    ArrayList<TimeRange> takenTimesMerged = new ArrayList();

    if (!takenTimes.isEmpty()) {
      takenTimesMerged.addAll(getMergedTimes(takenTimes));
    }
    else {
      takenTimesMerged.addAll(getMergedTimes(takenTimesOptional));
    }
    ArrayList<TimeRange> validTimes = getBetweenTimes(takenTimesMerged);
    ArrayList<TimeRange> finalTimes = filterTimes(validTimes, request);
    ArrayList<TimeRange> remove = new ArrayList();

    for (TimeRange optional : takenTimesOptional) {
      for (TimeRange mandatory : finalTimes) {
        if (mandatory.overlaps(optional)) {
            remove.add(mandatory);
        }
      }
    }

    if (remove.size() != finalTimes.size()) {
        finalTimes.removeAll(remove);
    } 
    return finalTimes;
  }
} 
