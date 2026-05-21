package com.example.cpuschedulingremindersystem;

import java.util.List;

public class FCFSAlgorithm {

    public static void calculate(List<Reminder> list) {
        int time = 0;
        for (Reminder r : list) {
            if (time < r.arrival)
                time = r.arrival;
            r.waiting = time - r.arrival;
            r.turnaround = r.waiting + r.burst;
            time += r.burst;
        }
    }
}
