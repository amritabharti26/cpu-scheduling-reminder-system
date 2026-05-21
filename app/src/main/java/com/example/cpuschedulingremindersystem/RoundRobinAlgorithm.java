package com.example.cpuschedulingremindersystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobinAlgorithm {
    public static List<Reminder> calculate(List<Reminder> inputList, int quantum) {
        List<Reminder> list = new ArrayList<>();
        for (Reminder r : inputList) {
            Reminder copy = new Reminder(r.title, r.arrival, r.burst, r.priority);
            list.add(copy);
        }
        
        List<Reminder> result = new ArrayList<>();
        Queue<Reminder> queue = new LinkedList<>();
        int currentTime = 0;
        int completed = 0;
        int n = list.size();
        boolean[] inQueue = new boolean[n];

        // Find the first arrival time
        int firstArrival = Integer.MAX_VALUE;
        for (Reminder r : list) {
            if (r.arrival < firstArrival) firstArrival = r.arrival;
        }
        currentTime = firstArrival;

        while (completed < n) {
            for (int i = 0; i < n; i++) {
                if (!inQueue[i] && list.get(i).arrival <= currentTime) {
                    queue.add(list.get(i));
                    inQueue[i] = true;
                }
            }

            if (queue.isEmpty()) {
                currentTime++;
                continue;
            }

            Reminder current = queue.poll();
            int executeTime = Math.min(current.remainingTime, quantum);
            
            current.remainingTime -= executeTime;
            currentTime += executeTime;

            // Check for new arrivals during execution
            for (int i = 0; i < n; i++) {
                if (!inQueue[i] && list.get(i).arrival <= currentTime) {
                    queue.add(list.get(i));
                    inQueue[i] = true;
                }
            }

            if (current.remainingTime > 0) {
                queue.add(current);
            } else {
                current.completionTime = currentTime;
                current.turnaround = current.completionTime - current.arrival;
                current.waiting = current.turnaround - current.burst;
                result.add(current);
                completed++;
            }
        }
        return result;
    }
}
