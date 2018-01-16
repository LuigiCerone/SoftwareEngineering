console.log("Ok");
// Let us open a web socket
var ws, display = 0;
var robots;

// display == 0 --> display Robots (by default).
// display == 1 --> display Clusters.

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
    var zones = JSON.parse(evt.data);

    // console.log(evt.data);
    // debugger;


    // console.log(zones);
    // robots = JSON.parse(data.robots);
    // var clusters = JSON.parse(data.clusters);
    // robots.forEach(function (value) {
    //     console.log(value);
    // });
    // console.log("Length: " + robots.length);
    // $('#robotsCount').text(robots.length);
    // $('#clustersCount').text(clusters.length);
    debugger;

    processZones(zones);
    // processRobots(robots);
    // processClusters(clusters);
    // console.log("Received data: " + received_msg);
    setStatus();

    displaySelected();
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

function processZones(zones) {
    console.log(zones);
    var array = Object.values(zones);

    var chunks = createGroupedArray(array, 5);

    var container = $('#zoneContainer');
    container.empty();

    for (var i = 0; i < chunks.length; i++) {
        var row = document.createElement('div');
        row.classList.add('row');

        chunks[i].forEach(function (data) {
            // console.log(data);
            var col = document.createElement('div');
            col.classList.add('col');
            col.dataset.id = data.id;

            var zone = document.createElement('p');
            zone.innerHTML = "<b>Zone:</b>" + data.id;
            col.appendChild(zone);

            var clusterLength = document.createElement('p');
            clusterLength.innerHTML = "<b>Number of clusters:</b> " + data.clustersList.length;
            col.appendChild(clusterLength);

            row.appendChild(col);
        });
        container.append(row);
    }
}


function processRobots(array) {
    var chunks = createGroupedArray(array, 5);

    var container = $('#robotContainer');
    container.empty();


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

function processClusters(array) {
    var chunks = createGroupedArray(array, 5);

    var container = $('#clusterContainer');
    container.empty();

    for (var i = 0; i < chunks.length; i++) {
        var row = document.createElement('div');
        row.classList.add('row');

        chunks[i].forEach(function (data) {
            var col = document.createElement('div');
            col.classList.add('col');

            var cluster = document.createElement('p');
            cluster.innerHTML = "<b>Cluster:</b>" + data.clusterId;
            col.appendChild(cluster);

            var zone = document.createElement('p');
            zone.innerHTML = "<b>Zone:</b> " + data.zoneId;
            col.appendChild(zone);

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

// Elements are just hidden so there is no need to reprocess them when users change screen.
function displaySelected() {
    if (display == 0) {
        $('#clusterContainer').hide();
        $('#robotContainer').show();
    } else if (display == 1) {
        $('#clusterContainer').show();
        $('#robotContainer').hide();
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

    $('body').on('click', 'div.col', function() {
        var zoneId = $(this).attr('data-id');
        console.log(zoneId);
    });

    $('#toggle').on('click', function () {
        display = 1 - display;
        displaySelected();

        if (display === 1)
            $('#toggle').html("Go to Robots");
        else
            $('#toggle').html("Go to Clusters");
    });

    $('#search').on('click', function (event) {
        event.preventDefault();
        var idToSearch = $('#input_id').val();
        // console.log(idToSearch);

        var searchedRobot = null;
        if (robots != null) {
            searchedRobot = robots.find(function (robot) {
                if (robot.robotId === idToSearch)
                    return true;
            });
            console.log(searchedRobot);
        }

        $('#modalBody').empty();

        var body = document.createElement('div');
        if (searchedRobot == null) {
            var p = document.createElement('p');
            p.innerHTML = "<b>Not found </b>";
        } else {
            var robot = document.createElement('p');
            robot.innerHTML = "<b>Robot:</b>" + searchedRobot.robotId;
            body.appendChild(robot);

            var cluster = document.createElement('p');
            cluster.innerHTML = "<b>Cluster:</b> " + searchedRobot.clusterId;
            body.appendChild(cluster);

            var ir = document.createElement('p');
            ir.innerHTML = "<b>IR:</b> " + searchedRobot.inefficiencyRate;
            body.appendChild(ir);
        }

        $('#modalBody').append(body);

    });
});
