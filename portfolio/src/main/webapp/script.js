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


const pictures = ["images/photos/proj1.jpg", "images/photos/proj2.jpg", "images/photos/proj3.jpg"]
const mePics = ["url('images/photos/me2.jpeg')", "url('images/photos/me3.jpeg')", "url('images/photos/me1.jpeg')"]

var currentIndex = 0;
var currentIndex2 = 0;

function nextImage(){
    currentIndex = (currentIndex + 1) % pictures.length;
    document.getElementById("projectImg").src = pictures[currentIndex];
}

function prevImage(){
    currentIndex = (currentIndex + pictures.length - 1) % pictures.length;
    document.getElementById("projectImg").src = pictures[currentIndex];
}

function autoChange (){
    currentIndex2 ++;
    currentIndex2 %= mePics.length;
    document.getElementById("headshot").style.backgroundImage= mePics[currentIndex2];
}

setInterval(autoChange,5000)