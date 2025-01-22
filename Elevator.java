package elevator;

import java.util.*;

class Request {
    int floor;
    String direction;
    int waitingTime; // How long the request has been waiting

    public Request(int floor, String direction) {
        this.floor = floor;
        this.direction = direction;
        this.waitingTime = 0;  // Initially, requests have no waiting time
    }
}

class ElevatorSystem {
    int currentFloor = 0;
    String currentDirection = "up";
    List<Request> requests = new ArrayList<>();

    // Add a new floor request
    public void addRequest(int floor, String direction) {
        requests.add(new Request(floor, direction));
    }

    // Update waiting times for all requests
    public void updateWaitingTimes() {
        for (Request request : requests) {
            request.waitingTime += 1;
        }
    }

    // Serve the next floor based on dynamic prioritization (distance + waiting time)
    public Request getNextRequest() {
        Request bestRequest = null;
        int bestScore = Integer.MAX_VALUE; // The lower the score, the higher the priority
        
        for (Request request : requests) {
            // Calculate the score based on distance and waiting time
            int distance = Math.abs(currentFloor - request.floor);
            int score = distance - request.waitingTime; // Adjusted with waiting time
            
            // Prioritize requests that are in the current direction
            if (request.direction.equals(currentDirection) && score < bestScore) {
                bestScore = score;
                bestRequest = request;
            }
        }

        // If no request in the current direction, check opposite direction
        if (bestRequest == null) {
            for (Request request : requests) {
                int distance = Math.abs(currentFloor - request.floor);
                int score = distance - request.waitingTime;
                
                if (score < bestScore) {
                    bestScore = score;
                    bestRequest = request;
                }
            }
        }

        return bestRequest;
    }

    // Move the elevator to the next requested floor
    public void moveToNextFloor() {
        if (requests.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        Request nextRequest = getNextRequest();
        if (nextRequest == null) {
            System.out.println("No suitable request found.");
            return;
        }

        // Move elevator to next request's floor
        System.out.println("Elevator moving to floor " + nextRequest.floor);
        currentFloor = nextRequest.floor;

        // Change direction if needed
        currentDirection = nextRequest.direction;

        // Remove the request once it's served
        requests.remove(nextRequest);

        // Update waiting times for remaining requests
        updateWaitingTimes();
    }
}

public class Elevator {
    public static void main(String[] args) {
        ElevatorSystem elevator = new ElevatorSystem();
        
        // Add some floor requests
        elevator.addRequest(10, "up");
        elevator.addRequest(3, "down");
        elevator.addRequest(7, "up");
        elevator.addRequest(1, "down");

        // Simulate elevator movement
        for (int i = 0; i < 10; i++) {
            elevator.moveToNextFloor();
        }
    }
}