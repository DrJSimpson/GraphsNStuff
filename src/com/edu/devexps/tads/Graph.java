package com.edu.devexps.tads;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Graph<T> {
	private ArrayList<Node<T>> nodeList;

	public void addNodeToGraph(Node<T> n) {
		nodeList.add(n);
	}

	/***
	 * Initialize the graph class, instantiate the list of nodes
	 */

	public Graph() {
		super();
		nodeList = new ArrayList<Node<T>>();
	}

	/**
	 * Add the n Node to the list at index listIndex
	 * 
	 * @param listIndex
	 * @param n
	 */

	public void addEdge(Node<T> fromNode, Node<T> toNode) {
		nodeList.get(nodeList.indexOf(fromNode)).getAdjList().add(toNode);
	}

	/***
	 * Adds a node adjacent to another (from -> to) and also adds the edge to
	 * the graph
	 * 
	 * @param fromNode
	 * @param toNode
	 * @param weight
	 */
	public void addEdgeWithWeight(Node<T> fromNode, Node<T> toNode, Integer weight) {
		// keep using this list for adjacency only, it might be useful
		nodeList.get(nodeList.indexOf(fromNode)).getAdjList().add(toNode);
		nodeList.get(nodeList.indexOf(fromNode)).getEdgeList().add(new Edge<T>(fromNode, toNode, weight));
	}

	/**
	 * perform the Depth First Search, this method will initialize the visited
	 * set and then call the recursive method that will actually perform the
	 * DFS this may not be of a lot of use, but by doing this we do not need to
	 * have a visited array as a part of the class
	 * 
	 * @param start
	 */

	public void DFS(Node<T> start) {
		Set<Node<T>> visitedSet = new HashSet<Node<T>>();
		System.out.println("Starting DFS from -> " + start.toString());
		DFSUtil(start, visitedSet);
	}

	/**
	 * This method does the trick. It will receive a vertex, and show it, mark
	 * the node as visited, then it will take the adjacency list of the given
	 * node check the nodes on the list and if any of these nodes was not
	 * already visited it will call itself again with that node as parameter,
	 * the visited list is maintained and passed through successive calls
	 * 
	 * @param vertex
	 * @param visited
	 */
	public void DFSUtil(Node<T> vertex, Set<Node<T>> visitedSet) {
		System.out.println("Visited: " + vertex.toString());
		visitedSet.add(vertex);
		for (Node<T> n : vertex.getAdjList()) {
			if (!visitedSet.contains(n)) {
				DFSUtil(n, visitedSet);
			}
		}
	}

	/**
	 * Performs a Breadth first search, this method will check the neighbors
	 * first
	 * 
	 * @param start
	 */
	public void BFS(Node<T> start) {
		Set<Node<T>> visitedSet = new HashSet<Node<T>>();

		Queue<Node<T>> nodeQueue = new LinkedList<Node<T>>();

		nodeQueue.add(start); // start from here, ant take it's adj list
		while (!nodeQueue.isEmpty()) {
			visitedSet.add(nodeQueue.peek());
			// Save a reference to the list, because we will use it later
			LinkedList<Node<T>> adjListOfNode = nodeQueue.peek().getAdjList();

			// Show it and also poll it from the queue
			System.out.println("Visited: " + nodeQueue.poll());

			for (Node<T> n : adjListOfNode) {
				if (!visitedSet.contains(n) && !nodeQueue.contains(n)) {
					nodeQueue.add(n);
				}
			}
		}
	}

	/***
	 * Performs a DFS that indicates the weight of the edges used and its
	 * accumulated value
	 * 
	 * @param start
	 */
	public void DFSWithWeight(Node<T> start) {
		Set<Node<T>> visitedSet = new HashSet<Node<T>>();
		System.out.println("Starting DFS from -> " + start.toString());
		DFSUtilWithWeight(start, visitedSet, start.getEdgeList(), 0);
	}

	/***
	 * This util will make use of the edge list to check the path to follow
	 * 
	 * @param vertex
	 * @param visitedSet
	 * @param edgeList
	 * @param weightAcum
	 */
	public void DFSUtilWithWeight(Node<T> vertex, Set<Node<T>> visitedSet, ArrayList<Edge<T>> edgeList,
			Integer weightAcum) {
		System.out.println("Visited: " + vertex.toString() + "Current weight acumulated: " + weightAcum);
		visitedSet.add(vertex);
		for (Edge<T> e : vertex.getEdgeList()) {
			if (!visitedSet.contains(e.getToNode())) {
				Integer weightLocal = e.getWeight() + weightAcum;
				System.out.println("Using edge from " + e.getFromNode() + " to " + e.getToNode());
				DFSUtilWithWeight(e.getToNode(), visitedSet, vertex.getEdgeList(), weightLocal);
			}
		}
	}

	/**
	 * This current revision has the issue that I made the mistake of
	 * creating edges as directed, given that they have a from node and 
	 * a to node. Still, I'll fix this eventaully, just let me enjoy this pls
	 * @param start
	 */
	public void dijkstra(Node<T> start) {
		int amountOfNodes = this.nodeList.size();
		//Create priority queue
		PriorityQueue<Edge<T>> prioq = new PriorityQueue<Edge<T>>();
		//Create a set of nodes that are already in shortest path
		Set<Node<T>> visitedSet = new HashSet<Node<T>>();
		Set<Edge<T>> usedEdgeSet = new HashSet<Edge<T>>();
		//Create a dummy edge, just to point out the fact that we already have
		//a shortest path to our start edge
		Edge<T> edge0 = new Edge<T>(start, start, 0);
		
		//Add it to the priority queue to start the algorithm
		prioq.add(edge0);
		
		//this is as bas as not feeding your dog
		while (!prioq.isEmpty() && visitedSet.size() < amountOfNodes) {
			//get the edge with the shortest weight
			Edge<T> actualEdge = prioq.poll();
			usedEdgeSet.add(actualEdge);
			System.out.println("Going from: " + actualEdge.getFromNode() + " to " +
					actualEdge.getToNode() + " with a weight of " + actualEdge.getWeight());
			//iterate over the edges that connects the start node
			for (Edge<T> edge : actualEdge.getToNode().getEdgeList()) {
				//make sure that we are not going back to a node that is already in the
				//shortest path
				if (!visitedSet.contains(edge.getToNode())
						&& !usedEdgeSet.contains(edge)) {
					prioq.add(edge);
				}
			}
			visitedSet.add(actualEdge.getToNode());
		}
	}
}
