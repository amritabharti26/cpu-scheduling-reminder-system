package com.example.cpuschedulingremindersystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SJFAlgorithm {
    public static List<Reminder> calculate(List<Reminder> inputList) {
        List<Reminder> list = new ArrayList<>(inputList);
        List<Reminder> result = new ArrayList<>();
        int currentTime = 0;
        
        while (!list.isEmpty()) {
            List<Reminder> availableTasks = new ArrayList<>();
            for (Reminder r : list) {
                if (r.arrival <= currentTime) {
                    availableTasks.add(r);
                }
            }

            if (availableTasks.isEmpty()) {
                int nextArrival = Integer.MAX_VALUE;
                for(Reminder r : list) if(r.arrival < nextArrival) nextArrival = r.arrival;
                currentTime = nextArrival;
                continue;
            }

            // Shortest Burst Time first
            Reminder nextTask = Collections.min(availableTasks, Comparator.comparingInt(r -> r.burst));
            
            nextTask.waiting = currentTime - nextTask.arrival;
            nextTask.turnaround = nextTask.waiting + nextTask.burst;
            nextTask.completionTime = currentTime + nextTask.burst;
            
            currentTime += nextTask.burst;
            result.add(nextTask);
            list.remove(nextTask);
        }
        return result;
    }
}
