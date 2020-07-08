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
google.charts.setOnLoadCallback(makeGraph);

const headshots = ["url('images/photos/me2.jpeg')", "url('images/photos/me3.jpeg')", "url('images/photos/me1.jpeg')"]
let headshotIndex = 0;

function autoChange (){
    headshotIndex ++;
    headshotIndex %= headshots.length;
    document.getElementById("headshot").style.backgroundImage= headshots[headshotIndex];
}

setInterval(autoChange, 5000);

function loadComments() {
    document.getElementById("history").innerHTML = "";
    let commentCount = document.getElementById("quant").value;

    fetch(`/data?quantity=${commentCount}`).then(response => response.json()).then((commentList) => {
        const historyElement = document.getElementById("history");
        console.log(commentList);
        for(let comment of commentList) { 
            let commentObject = JSON.parse(comment);
            let commentDate = new Date(commentObject.time);
            historyElement.appendChild(createListItem(`${commentObject.comment}, ${commentDate} (${commentObject.sentiment})`)); 
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
        gData.addColumn('date',"Day");
        gData.addColumn('number','cases');
        gData.addColumn('number','deaths');

        for (let entry of dataObject){
            let year = entry.year;
            let month = entry.month;
            let day = entry.day;
            let cases = entry.cases;
            let deaths = entry.deaths;
            gData.addRow([new Date(year,month,day),deaths,cases]);
        }

        const options = {
            'title': 'COVID Cases in Nevada',
            'width': 600,
            'height':500
        }
        
        console.log(gData);

    const chart = new google.visualization.LineChart(
        document.getElementById('chart-container'));
    chart.draw(gData, options);
    });
}

function createListItem(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
