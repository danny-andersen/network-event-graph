'use strict';

angular.module('networkEventGraphApp').service('chartService', function () {
  //Points is an array of {label, size}
  this.drawCircles = function (id, h, w, points) {
    var i, svg;
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
    //Remove any old svg
    $(id + ' svg').remove();
    svg = d3.select(id).append('svg').attr({
      width: w,
      height: h
    });
    var circles = svg.selectAll('circle')
      .data(points)
      .enter()
      .append('circle').attr({
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
      });
  };
});