console.log("Ok");
// Let us open a web socket
var ws;

function connectWS() {
    ws = new WebSocket("ws://127.0.0.1:9999");
    ws.onopen = function (data) {
        onOpen(data);
    };
    ws.onclose = function (data) {
        onClose(data);
    };
    ws.onmessage = function (data) {
        onMessage(data);
    };
    ws.onerror = function (data) {
        onError(data);
    };

    ws.onopen("Button click");
    setStatus();
}

function disconnectWS() {
    onClose();
    console.log(ws.readyState);
    setStatus();
}


function onOpen(data) {
    // Web Socket is connected, send data using send()
    // ws.send("Message to send");
    // console.log("Opened with data: " + data);
    setStatus();
}

function onMessage(evt) {
    // console.log("OnMessage");
    var data = JSON.parse(evt.data);
    var robots = JSON.parse(data.robots);
    // robots.forEach(function (value) {
    //     console.log(value);
    // });
    console.log("Length: " + robots.length);
    $('#robotsCount').text(robots.length);

    processAndDisplayRobots(robots);
    // console.log("Received data: " + received_msg);
    setStatus();
}

function onClose(data) {
    // websocket is closed.
    ws.close();
    console.log("Closed with data: " + data);
    setStatus();
}

function onError(error) {
    console.log("Error with data: " + error);
    setStatus();
}

function createGroupedArray(arr, chunkSize) {
    var groups = [], i;
    for (i = 0; i < arr.length; i += chunkSize) {
        groups.push(arr.slice(i, i + chunkSize));
    }
    return groups;
}

function processAndDisplayRobots(array) {
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

// CONNECTING	0	The connection is not yet open.
// OPEN	1	The connection is open and ready to communicate.
// CLOSING	2	The connection is in the process of closing.
// CLOSED	3	The connection is closed or couldn't be opened.
function setStatus() {
    var status = $('#status');
    if (ws == null) {
        status.text('CLOSED');
    } else {
        switch (ws.readyState) {
            case 0:
                // CONNECTING.
                status.text('CONNECTING');
                break;
            case 1:
                // OPEN.
                status.text('OPEN');
                break;
            case 2:
                // CLOSING.
                status.text('CLOSING');
                break;
            case 3:
                // CLOSED.
                status.text('CLOSED');
                break;
        }
    }
}


$(function () {
    setStatus();
    $('#wsStart').on('click', function () {
        if (ws != null && (ws.readyState == 0 || ws.readyState == 1))
            return;
        connectWS();
        console.log(ws.readyState);
    });

    $('#wsStop').on('click', function () {
        if (ws != null && (ws.readyState == 2 || ws.readyState == 3))
            return;
        disconnectWS();
    });
});
