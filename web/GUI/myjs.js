console.log("Ok");
// Let us open a web socket
var ws, display = 0;
var zones;
var clusterId, zoneId;

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
    zones = Object.values(JSON.parse(evt.data));
    processZones(zones);
    setStatus();

    if (display == 1) {
        processRobots(zoneId, clusterId);
    }

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
    // var chunks = createGroupedArray(zones, 5);
    var container = $('#zoneContainer');
    container.empty();
    zones.forEach(function (zone) {
        // Row is the container of a single zone.
        var row = document.createElement('div');
        row.classList.add('row');

        // console.log(data);
        // Col is a single zone.
        var col = document.createElement('div');
        col.classList.add('col');
        col.classList.add('shadow');
        col.id = zone.id;

        var zoneId = document.createElement('p');
        zoneId.innerHTML = "<b>Zone:</b>" + zone.id;
        col.appendChild(zoneId);

        var clusterLength = document.createElement('p');
        clusterLength.innerHTML = "<b>Number of clusters:</b> " + zone.clustersList.length;
        col.appendChild(clusterLength);
        row.appendChild(col);
        container.append(row);

        // Now the iterate over the clustersList.
        var chunks = createGroupedArray(zone.clustersList, 4);

        // We create a container in the single zone.
        var clusterContainer = document.createElement('div');
        clusterContainer.classList.add('container');
        col.appendChild(clusterContainer);
        $(col).find('.container').empty();

        for (var i = 0; i < chunks.length; i++) {
            var clusterRow = document.createElement('div');
            clusterRow.classList.add('row');

            chunks[i].forEach(function (cluster) {
                var clusterCol = document.createElement('div');
                clusterCol.classList.add('col-3');
                var innerCol = document.createElement('div');
                innerCol.classList.add('inner');
                innerCol.classList.add('cluster');

                innerCol.dataset.clusterId = cluster.clusterId;
                innerCol.dataset.zoneId = cluster.zoneId;

                clusterCol.appendChild(innerCol);

                var clusterId = document.createElement('p');
                clusterId.innerHTML = "<b>Cluster:</b>" + cluster.clusterId;
                innerCol.appendChild(clusterId);

                var ir = document.createElement('p');
                ir.innerHTML = "<b>IR:</b> " + cluster.inefficiencyRate;
                innerCol.appendChild(ir);

                var robotsNumber = document.createElement('p');
                robotsNumber.innerHTML = "<b>Number of robots:</b> " + cluster.robotsList.length;
                innerCol.appendChild(robotsNumber);

                if (cluster.inefficiencyRate >= 40.0) {
                    innerCol.classList.add('bg-danger');
                } else
                    innerCol.classList.add('bg-success');

                clusterRow.appendChild(clusterCol);
            });
            clusterContainer.append(clusterRow);
        }
    });
}


function processRobots() {
    var selectedZone = zones.find(function (zone) {
        if (zone.id === zoneId)
            return true;
    });
    var selectedCluster = null;
    if (selectedZone != null) {
        selectedCluster = selectedZone.clustersList.find(function (cluster) {
            if (cluster.clusterId === clusterId)
                return true;
        });
    }

    console.log(selectedCluster);
    // debugger;

    var container = $('#robotContainer');
    container.empty();

    var clusterInfo = document.createElement('div');
    clusterInfo.classList.add('row');
    var clusterInfoCol = document.createElement('div');
    clusterInfoCol.classList.add('col');
    clusterInfoCol.classList.add('no-border');
    clusterInfo.appendChild(clusterInfoCol);
    container.append(clusterInfo);

    var clusterIdInfo = document.createElement('p');
    clusterIdInfo.innerHTML = "<b>Cluster:</b>" + selectedCluster.clusterId;
    clusterInfoCol.appendChild(clusterIdInfo);

    var ir = document.createElement('p');
    ir.innerHTML = "<b>IR:</b> " + selectedCluster.inefficiencyRate;
    clusterInfoCol.appendChild(ir);

    var robotsNumber = document.createElement('p');
    robotsNumber.innerHTML = "<b>Number of robots:</b> " + selectedCluster.robotsList.length;
    clusterInfoCol.appendChild(robotsNumber);

    if (selectedCluster.robotsList.length == 0) {
        var error = document.createElement('h3');
        error.innerHTML = "<p>No robots in this clusters</p>";
        container.append(error);
    } else {
        var chunks = createGroupedArray(selectedCluster.robotsList, 5);
        for (var i = 0; i < chunks.length; i++) {
            var row = document.createElement('div');
            row.classList.add('row');

            chunks[i].forEach(function (data) {
                var col = document.createElement('div');
                col.classList.add('col');
                col.classList.add('inner');

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
        $('#robotContainer').hide();
        $('#zoneContainer').show();
        $('#toggle').prop("disabled", true);
    } else if (display == 1) {
        $('#robotContainer').show();
        $('#zoneContainer').hide();
        $('#toggle').prop("disabled", false);
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

    $('body').on('click', 'div.cluster', function () {
        zoneId = $(this).data('zoneId');
        clusterId = $(this).data('clusterId');
        display = 1;
        displaySelected();
        processRobots(zoneId, clusterId);
    });

    $('#toggle').on('click', function () {
        display = 1 - display;
        displaySelected();
    });

    $('#search').on('click', function (event) {
        event.preventDefault();
        var idToSearch = $('#input_id').val();
        // console.log(idToSearch);

        var searchedRobot = null;

        zones.forEach(function (zone) {
            zone.clustersList.forEach(function (cluster) {
                cluster.robotsList.forEach(function (robot) {
                    if (robot.robotId === idToSearch) {
                        searchedRobot = robot;
                    }
                });
            });
        });

        console.log(searchedRobot);

        $('#modalBody').empty();

        var body = document.createElement('div');
        if (searchedRobot == null) {
            var p = document.createElement('p');
            p.innerHTML = "<b>Not found </b>";
            body.appendChild(p);
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
