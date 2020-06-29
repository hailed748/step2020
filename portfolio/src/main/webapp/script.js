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


const projects = ["images/photos/proj1.jpg", "images/photos/proj2.jpg", "images/photos/proj3.jpg"]
const headshots = ["url('images/photos/me2.jpeg')", "url('images/photos/me3.jpeg')", "url('images/photos/me1.jpeg')"]

let projectIndex = 0;
let headshotIndex = 0;

function nextImage(){
    projectIndex ++;
    projectIndex %= projects.length;
    document.getElementById("projectImg").src = projects[projectIndex];
}

function prevImage(){
    projectIndex += projects.length -1;
    projectIndex %= projects.length;
    document.getElementById("projectImg").src = projects[projectIndex];
}

function autoChange (){
    headshotIndex ++;
    headshotIndex %= headshots.length;
    document.getElementById("headshot").style.backgroundImage= headshots[headshotIndex];
}

setInterval(autoChange, 5000);

function getName() {
  fetch("/data").then(response => response.text()).then((name) => {
    document.getElementById('name').innerText = name;
  });
}

function makeList(){
    fetch("/data").then(response => response.json()).then((wordList) => {
        const buildList = document.getElementById("make-me");
        buildList.innerHTML = "";
        for(let i = 0; i < wordList.length; i++){
            buildList.appendChild(createListItem(wordList[i]));
        }
    });
}

function createListItem(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
