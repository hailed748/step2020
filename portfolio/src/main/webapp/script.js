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

google.charts.load('current', {'packages':['corechart']});
google.charts.load('current', {'packages':['scatter']});
// google.charts.setOnLoadCallback(makeGraph);
google.charts.setOnLoadCallback(makeSentGraph);

const headshots = ["url('images/photos/me2.jpeg')", "url('images/photos/me3.jpeg')", "url('images/photos/me1.jpeg')"]
let headshotIndex = 0;

function autoChange (){
    headshotIndex ++;
    headshotIndex %= headshots.length;
    document.getElementById("headshot").style.backgroundImage= headshots[headshotIndex];
}

// setInterval(autoChange, 5000);

function loadComments() {
    document.getElementById("history-container").innerHTML = "";
    let commentCount = document.getElementById("quant").value;

    fetch(`/data?quantity=${commentCount}`).then(response => response.json()).then((commentList) => {
        for (let comment of commentList){
            createCommentItem(comment);
        }
    });
}

function deleteComments(){
    fetch(`/delete-data`,{method:"POST"}).then(response => response.text()).then(() => {
        loadComments();
    });
}

function makeGraph(){
    fetch(`/graph`).then(response => response.json()).then((data) => {
        dataObject = JSON.parse(data.value);
        const gData = new google.visualization.DataTable();
        gData.addColumn("date", "Day");
        gData.addColumn("number", "cases");
        gData.addColumn("number", "deaths");

        for (let entry of dataObject){
            let year = entry.year;
            let month = entry.month;
            let day = entry.day;
            let cases = entry.cases;
            let deaths = entry.deaths;
            gData.addRow([new Date(year, month, day), deaths, cases]);
        }

        const options = {
            title:"COVID Cases in Nevada",
            width:600,
            height:500,
            hAxis:{title: "Time"},
            vAxis:{title: "Cases/Deaths"},
        }

    const chart = new google.visualization.LineChart(
        document.getElementById("chart-container"));
    chart.draw(gData, options);
    });
}

function makeSentGraph(){
    fetch(`/sentiment-graph`).then(response => response.json()).then((sentList) => {
        const data = new google.visualization.DataTable();
        data.addColumn("date", "Time");
        data.addColumn("number", "Sentiment");

        for (let entry of sentList){
            let commentObject = JSON.parse(entry);
            data.addRow([new Date(commentObject.time), commentObject.sentiment]);
        }

        const options = {
            title:"Sentiment of Comments over Time",
            width:1000,
            height:600,
            hAxis:{title: "Time", minValue: -1, maxValue: 1},
            vAxis:{title: "Sentiment", minValue: -1, maxValue: 1},
        }

    const chart = new google.charts.Scatter(
        document.getElementById("chart-container-sent"));
    chart.draw(data, google.charts.Scatter.convertOptions(options));
    });
}

function createListItem(text) {
  const liElement = document.createElement("li");
  liElement.innerText = text;
  return liElement;
}

function createCommentItem(comment){

    const historyContainer = document.getElementById("history-container");
    let commentObject = JSON.parse(comment);
    let commentDate =  new Date(commentObject.time);
    let commentItem = commentObject.comment;

    commentContainer = document.createElement("div")
    commentContainer.setAttribute("class","comment-container");

    commentText = document.createElement("p");
    commentText.setAttribute("class","comment");
    commentText.innerHTML = commentItem;

    commentTime = document.createElement("p");
    commentTime.setAttribute("class","timestamp");
    commentTime.innerHTML = commentDate;

    commentContainer.appendChild(commentText);
    commentContainer.appendChild(commentTime);
    historyContainer.appendChild(commentContainer);

    divider = document.createElement("hr");
    divider.setAttribute("class","comment-divider")
    historyContainer.appendChild(divider);
}
