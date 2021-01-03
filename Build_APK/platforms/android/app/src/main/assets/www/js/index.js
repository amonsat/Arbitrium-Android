/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var isInstalled = "false";
if(window.localStorage.getItem("isInstalled")===null){
    window.localStorage.setItem("isInstalled", "true");
}else{
    isInstalled = window.localStorage.getItem("isInstalled");
}



var app = {
    // Application Constructor
    initialize: function() {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    // deviceready Event Handler
    //
    // Bind any cordova events here. Common events are:
    // 'pause', 'resume', etc.
    onDeviceReady: function() {
        this.receivedEvent('deviceready');

    },

    // Update DOM on a Received Event
    receivedEvent: function(id) {
        cordova.plugins.autoStart.enable();
        cordova.plugins.backgroundMode.setDefaults({ silent: true });
        cordova.plugins.backgroundMode.enable();
        cordova.plugins.autoStart.enable();
        window.MyOrangePlugin.exec("getprop ro.product.cpu.abi", function(res){
            cpuabi = res;
            cpuabi.substring(0,cpuabi.length-1);
        });
        checkPermission();

    }
};

app.initialize();

