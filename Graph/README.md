**Graph** [Download](https://github.com/ablochha/Resume/blob/master/Graph/ImageSegment.zip?raw=true)

2nd year individual assignment, written in Java.

This project was designed to give experience building a graph data structure with an adjacency list representation. Furthermore, depth-first traversal is used to traverse the graph, and a minimum spanning tree is built. We were provided with the graph interface, an image reader, and an implementation for a priority queue. This program will read a simple image from a file, and build a graph for it. A minimum spanning tree is used to connect the graph, and the edges with the sharpest color contrast are removed to create unconnected graphs. These segments are then repainted as the average color of the segment.

After downloading `ImageSegment.jar`and `input.jpg`, run with

```
java -jar ImageSegment.jar input.jpg output.jpg 5
```
