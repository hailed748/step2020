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

const headshots = ["url('images/photos/me2.jpeg')", "url('images/photos/me3.jpeg')", "url('images/photos/me1.jpeg')"]
let headshotIndex = 0;

function autoChange (){
    headshotIndex ++;
    headshotIndex %= headshots.length;
    document.getElementById("headshot").style.backgroundImage= headshots[headshotIndex];
}

setInterval(autoChange, 5000);

function loadComments() {

    $("#history").empty();
    // document.getElementById("history").empty();
    let commentCount = document.getElementById("quant").value;

    fetch(`/data?quantity=${commentCount}`).then(response => response.json()).then((commentList) => {
        const historyElement = document.getElementById("history");
        for(let comment of commentList) { 
            let commentObject = JSON.parse(comment);
            let commentDate = new Date(commentObject.time);
            historyElement.appendChild(createListItem(commentObject.comment + ", " + commentDate)); 
        }
    });
}

function deleteComments(){
    fetch(`/delete-data`,{method:"POST"}).then(response => response.text()).then((confirmation) => {
        loadComments();
    });
}

function createListItem(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
