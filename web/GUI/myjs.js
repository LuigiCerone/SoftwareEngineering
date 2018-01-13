console.log("Ok");

// Let us open a web socket
var ws = new WebSocket("ws://192.168.0.4:9091");

ws.onopen = function (data) {
    // Web Socket is connected, send data using send()
    ws.send("Message to send");
    console.log("Opened with data: " + data);
};

ws.onmessage = function (evt) {
    var received_msg = evt.data;
    console.log("Opened with data: " + received_msg);
};

ws.onclose = function (data) {
    // websocket is closed.
    console.log("Closed with data: " + data);
};

ws.onerror = function (error) {
    console.log("Error with data: " + error);
}
