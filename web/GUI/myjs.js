console.log("Ok");
// Let us open a web socket
var ws, display = 0;
var zones;

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


// function processRobots(array) {
//     var chunks = createGroupedArray(array, 5);
//
//     var container = $('#robotContainer');
//     container.empty();
//
//
//     for (var i = 0; i < chunks.length; i++) {
//         var row = document.createElement('div');
//         row.classList.add('row');
//
//         chunks[i].forEach(function (data) {
//             var col = document.createElement('div');
//             col.classList.add('col');
//
//             var robot = document.createElement('p');
//             robot.innerHTML = "<b>Robot:</b>" + data.robotId;
//             col.appendChild(robot);
//
//             var cluster = document.createElement('p');
//             cluster.innerHTML = "<b>Cluster:</b> " + data.clusterId;
//             col.appendChild(cluster);
//
//             var ir = document.createElement('p');
//             ir.innerHTML = "<b>IR:</b> " + data.inefficiencyRate;
//             col.appendChild(ir);
//
//             if (data.inefficiencyRate >= 40.0) {
//                 col.classList.add('bg-danger');
//             } else
//                 col.classList.add('bg-success');
//
//             row.appendChild(col);
//         });
//         container.append(row);
//     }
// }

function processClusters(array, zoneId) {
    var chunks = createGroupedArray(array, 5);

    // var container = document.createElement('div');
    // container.id = ""
    var container = $("#" + zoneId).parent();
    var div = document.createElement('div');
    div.classList.add('container');
    container.find('.container').empty();

    // var container = $(".col").find("[data-id='" + zoneId + "']").parent();

    // var container = $('#robotContainer');

    for (var i = 0; i < chunks.length; i++) {
        var row = document.createElement('div');
        row.classList.add('row');

        chunks[i].forEach(function (data) {
            var col = document.createElement('div');
            col.classList.add('col');

            var cluster = document.createElement('p');
            cluster.innerHTML = "<b>Cluster:</b>" + data.clusterId;
            col.appendChild(cluster);

            var ir = document.createElement('p');
            ir.innerHTML = "<b>IR:</b> " + data.inefficiencyRate;
            col.appendChild(ir);

            var robotsNumber = document.createElement('p');
            robotsNumber.innerHTML = "<b>Number of robots:</b> " + data.robotsList.length;
            col.appendChild(robotsNumber);

            if (data.inefficiencyRate >= 40.0) {
                col.classList.add('bg-danger');
            } else
                col.classList.add('bg-success');

            row.appendChild(col);
        });
        div.append(row);
        container.append(div);

        // var parent = div.parentNode;
        // div.append(container);
    }
    // parent.append(container);
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

    $('body').on('click', 'div.col', function () {
        var zoneId = $(this).attr('id');

        // console.log(zoneId);
        // var selectedZone = zones.find(function (zone) {
        //     if (zone.id === zoneId)
        //         return true;
        // });
        // var clusterList = selectedZone.clustersList;
        // // debugger;
        // console.log(clusterList);
        // processClusters(clusterList, zoneId);
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
