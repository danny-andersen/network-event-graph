angular.module('networkEventGraphApp').service('graphService', function () {
  var sigInst;
  var colours = {
    from: '#0f0',
    to: '#f00',
    both: '#ff0',
    device: '#22f'
  };

  this.plotSessions = function ($scope, $window, nodes, edges, coreIpAddr, sessions) {
    //Show device node centrally
    if (nodes[coreIpAddr] === undefined) {
      sigInst.addNode(coreIpAddr, {
        label: coreIpAddr,
        x: 0.5,
        y: 0.5,
        color: colours.device,
        size: 5
      });
      nodes[coreIpAddr] = true;
    }
    //Find max and min session size
    var maxCnt = -1;
    var minCnt = 9999999;
    var nodeColour = {};
    var session;
    var i;
    for (i = 0; i < sessions.length; i++) {
      session = sessions[i];
      var cnt = session.numSessions;
      if (cnt < minCnt) {
        minCnt = cnt;
      }
      if (maxCnt < cnt) {
        maxCnt = cnt;
      }
      nodeColour[session.srcIpAddr] = nodeColour[session.srcIpAddr] === undefined ? colours.to : colours.both;
      nodeColour[session.destIpAddr] = nodeColour[session.destIpAddr] === undefined ? colours.from : colours.both;
    }
    var sizeScale = 9 / maxCnt;
    var minSize = 1;
    for (i = 0; i < sessions.length; i++) {
      session = sessions[i];
      //Check node exists
      var srcIp = session.srcIpAddr;
      var size = session.numSessions * sizeScale;
      if (nodes[srcIp] === undefined) {
        sigInst.addNode(srcIp, {
          label: session.srcHostname === null ? session.srcIpAddr : session.srcHostName,
          x: Math.random(),
          y: Math.random(),
          color: nodeColour[srcIp],
          size: size + minSize
        });
        nodes[srcIp] = true;
      }
      var destIp = session.destIpAddr;
      if (nodes[destIp] === undefined) {
        sigInst.addNode(destIp, {
          label: session.destHostname === null ? destIp : session.destHostName,
          x: Math.random(),
          y: Math.random(),
          color: nodeColour[destIp],
          size: size + minSize
        });
        nodes[destIp] = true;
      }
      var edge = srcIp + '-' + destIp;
      if (edges[edge] === undefined) {
        sigInst.addEdge(edge, srcIp, destIp);
        edges[edge] = true;
      }
    }
    sigInst.draw();
    sigInst.bind('downnodes', function (event) {
      var ipAddr = event.content;
      var path = '#/ipaddr/' + ipAddr;
      var inMenu = false;
      $scope.items.Devices.forEach(function (item) {
        if (item.name.indexOf(ipAddr) !== -1) {
          inMenu = true;
        }
      });
      if (!inMenu) {
        $scope.$parent.items.Devices.push({
          'href': path,
          'name': 'IpAddr(' + ipAddr + ')'
        });
      }
      var currentPath = $window.location.pathname;
      var url = currentPath + path;
      $window.location = url;
    });
  };

  this.plotDevices = function (devices, nodes) {
    var i, j, ipaddrs, ipAddr;
    //For each device, plot the ipaddr node for it (so that it has the right colour)
    for (i = 0; i < devices.length; i++) {
      ipaddrs = devices[i].ipaddr;
      for (j = 0; j < ipaddrs.length; j++) {
        ipAddr = ipaddrs[j].ipAddr;
        if (nodes[ipAddr] === undefined) {
          sigInst.addNode(ipAddr, {
            label: ipAddr,
            x: Math.random(),
            y: Math.random(),
            color: colours.device,
            size: 5
          });
          nodes[ipAddr] = true;
        }
      }
    }
  };

  this.initGraph = function () {
    //Reset graph by removing and then adding element
    $('#sessionGraph').remove();
    $('#sessionGraphParent').html('<div class="sigma-graph" id="sessionGraph" />');
    var element = document.getElementById('sessionGraph');
    //     var element = $('#sessionGraph');
    sigInst = sigma.init(element);
    sigInst.emptyGraph();
    sigInst.refresh();
    sigInst.drawingProperties({
      defaultLabelColor: '#fff',
      edgeColor: 'default',
      defaultEdgeColor: '#aaa',
      labelThreshold: 5
      // defaultEdgeType: 'curve'
    });
  };

  this.filterSessions = function (direction) {
    sigInst.iterNodes(function (node) {
      if (node.color === colours.device) {
        //leave this one
        node.hidden = false;
        return;
      }
      switch (direction) {
      case ('from'):
        if (node.color === colours.from) {
          node.hidden = false;
        } else {
          node.hidden = true;
        }
        break;
      case ('to'):
        if (node.color === colours.to) {
          node.hidden = false;
        } else {
          node.hidden = true;
        }
        break;
      case ('both'):
        if (node.color === colours.both) {
          node.hidden = false;
        } else {
          node.hidden = true;
        }
        break;
      case ('all'):
        node.hidden = false;
        break;
      }
    });
    sigInst.draw();
  };

  this.showGraph = function ($scope, $window, sessions, coreIpAddr) {
    var nodes = {};
    var edges = {};
    this.initGraph();
    this.plotSessions($scope, $window, nodes, edges, coreIpAddr, sessions);
  };

  this.showKey = function () {
    var canvas = document.getElementById('keyCanvas');
    var ctx = canvas.getContext('2d');
    ctx.font = '12px Georgia';
    var spacing = 65;
    var spotSize = 5;
    var offset = spotSize + 2;
    var x = 5;
    ctx.beginPath();
    ctx.fillStyle = '#222';
    ctx.fillText('Key:', x + offset, 10);
    ctx.fill();
    ctx.beginPath();
    x += spacing;
    ctx.fillStyle = colours.to;
    ctx.arc(x, 5, 5, 0, Math.PI * 2, false);
    ctx.fillText('Incoming', x + offset, 10);
    ctx.fill();
    ctx.beginPath();
    ctx.fillStyle = colours.from;
    x += spacing;
    ctx.arc(x, 5, 5, 0, Math.PI * 2, false);
    ctx.fillText('Outgoing', x + offset, 10);
    ctx.fill();
    ctx.beginPath();
    x += spacing;
    ctx.fillStyle = colours.both;
    ctx.arc(x, 5, 5, 0, Math.PI * 2, false);
    ctx.fillText('In+Out', x + offset, 10);
    ctx.fill();
    ctx.beginPath();
    x += spacing;
    ctx.fillStyle = colours.device;
    ctx.arc(x, 5, 5, 0, Math.PI * 2, false);
    ctx.fillText('Device', x + offset, 10);
    ctx.fill();
  };

  this.startForceAtlas2 = function () {
    sigInst.startForceAtlas2();
  };

  this.stopForceAtlas2 = function () {
    sigInst.stopForceAtlas2();
  };
});