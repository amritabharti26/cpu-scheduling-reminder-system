package com.example.cpuschedulingremindersystem;

public class Reminder {
    String title;
    int arrival;
    int burst;
    int priority;
    int waiting;
    int turnaround;
    int remainingTime; // For Round Robin
    int completionTime;

    public Reminder(String title, int arrival, int burst, int priority) {
        this.title = title;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.remainingTime = burst;
    }
}
