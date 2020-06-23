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


const pictures = ["images/proj1.jpg", "images/proj2.jpg", "images/proj3.jpg"]
var currentIndex = 0;

function nextImage(){
    currentIndex = (currentIndex + 1) % pictures.length;
    document.getElementById("projectImg").src = pictures[currentIndex];
}

function prevImage(){
    currentIndex = ((currentIndex - 1  % pictures.length) + pictures.length) % pictures.length;
    document.getElementById("projectImg").src = pictures[currentIndex];
}
