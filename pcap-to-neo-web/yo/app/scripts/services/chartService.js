'use strict';

angular.module('networkEventGraphApp').service('chartService', function () {
  var setSvg = function (id, w, h) {
    //Remove any old svg
    $(id + ' svg').remove();
    var svg = d3.select(id).append('svg').attr({
      width: w,
      height: h
    });
    return svg;
  };

  //Points is an array of {label, size}
  this.drawCircles = function (id, w, h, points) {
    var i, svg = setSvg(id, w, h);
    var maxSize = d3.max(points, function (p) {
      return p.size;
    });
    var minSize = d3.min(points, function (p) {
      return p.size;
    });
    var rScale = d3.scale.linear().domain([minSize, maxSize]);
    //Layout points in a square grid with an edge == sqrt num of points
    var dim = d3.min([h, w]);
    var noEdges = Math.ceil(Math.sqrt(points.length));
    var edgeStep = dim / noEdges;
    rScale.range([edgeStep / 20, edgeStep / 2]);
    var colour = d3.scale.category20c();
    var circles = svg.selectAll('circle')
      .data(points)
      .enter()
      .append('circle')
      .attr({
        'cx': function (p, i) {
          var pos = i;
          var mod = Math.floor(pos / noEdges);
          var rem = pos - (mod * noEdges);
          return (2 * rem + 1) * edgeStep / 2;
        },
        'cy': function (p, i) {
          var pos = i;
          var mod = Math.floor(pos / noEdges);
          return (2 * mod + 1) * edgeStep / 2;
        },
        'r': function (p, i) {
          return rScale(p.size);
        }
      })
      .style('fill', function (p) {
        return colour(p.label);
      });
  };

  var createHierarchy = function (points) {
    var i, root = {};
    root.name = 'root';
    root.children = [];
    for (i = 0; i < points.length; i++) {
      root.children.push({
        name: points[i].label,
        value: points[i].size
      });
    }
    return root;
  };

  this.drawBubble = function (id, w, h, points) {
    var svg = setSvg(id, w, h);
    var bubble = d3.layout.pack()
      .sort(null)
      .size([w, h])
      .padding(1.5);
    var format = d3.format(',d');
    var colour = d3.scale.category20c();
    svg.attr('class', 'bubble');
    var node = svg.selectAll('.node')
      .data(bubble.nodes(createHierarchy(points)))
      .enter()
      .append('g')
      .attr('class', 'node')
      .attr('transform', function (d) {
        return 'translate(' + d.x + ',' + d.y + ')';
      });
    node.append('title')
      .text(function (d) {
        return (d.name);
      });
    node.append('circle')
      .attr('r', function (d) {
        return d.r;
      })
      .style('fill', function (d) {
        var c;
        if (d.name === 'root') {
          c = 'none';
        } else {
          c = colour(d.name);
        }
        return c;
      });
    node.append('text')
      .attr('dy', '.3em')
      .style('text-anchor', 'middle')
      .text(function (d) {
        var str;
        if (d.r > 10) {
          //Just get the first network part
          var parts = d.name.split('.');
          str = parts[0].substring(0, d.r / 3);
        } else {
          str = '';
        }
        return str;
      });
  };
});