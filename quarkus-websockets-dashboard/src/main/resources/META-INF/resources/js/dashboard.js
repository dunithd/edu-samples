"use strict"
var connected = false;
var socket;

function connect() {
    if (! connected) {
        var clientId = generateClientId(6);
        socket = new WebSocket("ws://" + location.host + "/dashboard/" + clientId);

        socket.onopen = function() {
            connected = true;
            console.log("Connected to the web socket with clientId [" + clientId + "]");
            $("#connect").attr("disabled", true);
            $("#connect").text("Connected");
        };
        socket.onmessage =function(m) {
            console.log("Got message: " + m.data);
            $("#totalOrders").text(m.data);
        };
    }
}

function generateClientId(length) {
   var result           = '';
   var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
   var charactersLength = characters.length;
   for ( var i = 0; i < length; i++ ) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
   }
   return result;
}


