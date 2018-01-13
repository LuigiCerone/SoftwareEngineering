console.log("Ok");

// Let us open a web socket
var ws = new WebSocket("ws://127.0.0.1:9091");

ws.onopen = function (data) {
    // Web Socket is connected, send data using send()
    // ws.send("Message to send");
    // console.log("Opened with data: " + data);
};

ws.onmessage = function (evt) {
    var received_msg = JSON.parse(evt.data);
    received_msg.forEach(function (value) {
        console.log(value);
    });
    processAndDisplay(received_msg);
    // console.log("Received data: " + received_msg);
};

ws.onclose = function (data) {
    // websocket is closed.
    console.log("Closed with data: " + data);
};

ws.onerror = function (error) {
    console.log("Error with data: " + error);
}

var createGroupedArray = function (arr, chunkSize) {
    var groups = [], i;
    for (i = 0; i < arr.length; i += chunkSize) {
        groups.push(arr.slice(i, i + chunkSize));
    }
    return groups;
}

function processAndDisplay(array) {
    var chunks = createGroupedArray(array, 5);

    var container = $('#container');
    for (var i = 0; i < chunks.length; i++) {
        var row = document.createElement('div');
        row.classList.add('row');

        chunks[i].forEach(function (data) {
            var col = document.createElement('div');
            col.classList.add('col');

            var robot = document.createElement('p');
            robot.innerHTML = "<b>Robot:</b>" + data.robotId;
            col.appendChild(robot);

            var cluster = document.createElement('p');
            cluster.innerHTML = "<b>Cluster:</b> " + data.clusterId;
            col.appendChild(cluster);

            var ir = document.createElement('p');
            ir.innerHTML = "<b>IR:</b> " + data.inefficiencyRate;
            col.appendChild(ir);

            if (data.inefficiencyRate >= 40.0) {
                col.classList.add('bg-danger');
            } else
                col.classList.add('bg-success');

            row.appendChild(col);
        });
        container.append(row);
    }
}