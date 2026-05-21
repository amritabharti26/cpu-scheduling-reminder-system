package com.example.cpuschedulingremindersystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText titleInput, arrivalInput, burstInput, priorityInput, quantumInput;
    TextView resultText, queueListText, currentTimeText, activeReminderText;
    Button addBtn, runBtn, resetBtn;
    LinearLayout statusCard, priorityLayout, quantumLayout;
    ProgressBar executionProgress;
    Spinner algoSpinner;

    List<Reminder> reminders = new ArrayList<>();
    Handler mainHandler = new Handler(Looper.getMainLooper());
    Handler timeHandler = new Handler(Looper.getMainLooper());
    boolean isExecuting = false;
    Ringtone currentRingtone;

    private static final String[] ALGOS = {"FCFS", "SJF", "Priority", "Round Robin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inputs
        titleInput = findViewById(R.id.titleInput);
        arrivalInput = findViewById(R.id.arrivalInput);
        burstInput = findViewById(R.id.burstInput);
        priorityInput = findViewById(R.id.priorityInput);
        quantumInput = findViewById(R.id.quantumInput);

        // Labels/Text
        resultText = findViewById(R.id.resultText);
        queueListText = findViewById(R.id.queueListText);
        currentTimeText = findViewById(R.id.currentTimeText);
        activeReminderText = findViewById(R.id.activeReminderText);
        
        // Layouts
        statusCard = findViewById(R.id.statusCard);
        priorityLayout = findViewById(R.id.priorityLayout);
        quantumLayout = findViewById(R.id.quantumLayout);
        executionProgress = findViewById(R.id.executionProgress);

        // Buttons
        addBtn = findViewById(R.id.addReminderBtn);
        runBtn = findViewById(R.id.runBtn);
        resetBtn = findViewById(R.id.resetBtn);

        // Spinner
        algoSpinner = findViewById(R.id.algoSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ALGOS);
        algoSpinner.setAdapter(adapter);

        setupListeners();
        startTimeUpdates();
    }

    private void setupListeners() {
        addBtn.setOnClickListener(v -> addReminder());
        runBtn.setOnClickListener(v -> startScheduling());
        resetBtn.setOnClickListener(v -> resetAll());

        algoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = ALGOS[position];
                priorityLayout.setVisibility(selected.equals("Priority") ? View.VISIBLE : View.GONE);
                quantumLayout.setVisibility(selected.equals("Round Robin") ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void startTimeUpdates() {
        timeHandler.post(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                currentTimeText.setText(sdf.format(new Date()));
                timeHandler.postDelayed(this, 1000);
            }
        });
    }

    private void addReminder() {
        String title = titleInput.getText().toString();
        String arrivalStr = arrivalInput.getText().toString();
        String burstStr = burstInput.getText().toString();
        String priorityStr = priorityInput.getText().toString();

        if (title.isEmpty() || arrivalStr.isEmpty() || burstStr.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int arrival = Integer.parseInt(arrivalStr);
        int burst = Integer.parseInt(burstStr);
        int priority = priorityStr.isEmpty() ? 1 : Integer.parseInt(priorityStr);

        reminders.add(new Reminder(title, arrival, burst, priority));
        updateQueueDisplay();

        Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
        titleInput.setText("");
        arrivalInput.setText("");
        burstInput.setText("");
        priorityInput.setText("");
    }

    private void updateQueueDisplay() {
        if (reminders.isEmpty()) {
            queueListText.setText("No reminders in queue");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reminders.size(); i++) {
            Reminder r = reminders.get(i);
            sb.append(String.format(Locale.US, "R%d | %s (AT=%d, D=%ds, P=%d)\n", 
                i + 1, r.title, r.arrival, r.burst, r.priority));
        }
        queueListText.setText(sb.toString());
    }

    private void startScheduling() {
        if (reminders.isEmpty()) {
            Toast.makeText(this, "Queue is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isExecuting) return;

        String selectedAlgo = algoSpinner.getSelectedItem().toString();
        List<Reminder> scheduledTasks;

        if (selectedAlgo.equals("FCFS")) {
            FCFSAlgorithm.calculate(reminders);
            scheduledTasks = new ArrayList<>(reminders);
        } else if (selectedAlgo.equals("SJF")) {
            scheduledTasks = SJFAlgorithm.calculate(reminders);
        } else if (selectedAlgo.equals("Priority")) {
            scheduledTasks = PriorityAlgorithm.calculate(reminders);
        } else { // Round Robin
            String qStr = quantumInput.getText().toString();
            int quantum = qStr.isEmpty() ? 2 : Integer.parseInt(qStr);
            scheduledTasks = RoundRobinAlgorithm.calculate(reminders, quantum);
        }

        displayResults(scheduledTasks);
        
        isExecuting = true;
        runBtn.setEnabled(false);
        runBtn.setText("Executing...");
        
        executeSequence(scheduledTasks, 0);
    }

    private void displayResults(List<Reminder> tasks) {
        StringBuilder result = new StringBuilder();
        result.append(String.format(Locale.US, "%-4s %-10s %-4s %-4s %-4s %-4s\n", "ID", "Msg", "Arr", "Dur", "Wait", "Turn"));
        result.append("------------------------------------------\n");

        int totalWait = 0;
        int totalTurnaround = 0;

        for (Reminder r : tasks) {
            result.append(String.format(Locale.US, "%-4s %-10.10s %-4d %-4d %-4d %-4d\n", 
                r.title.substring(0, Math.min(r.title.length(), 2)), r.title, r.arrival, r.burst, r.waiting, r.turnaround));
            
            totalWait += r.waiting;
            totalTurnaround += r.turnaround;
        }

        result.append("\n");
        result.append(String.format(Locale.US, "Avg Waiting: %.2f\n", (float) totalWait / tasks.size()));
        result.append(String.format(Locale.US, "Avg Turnaround: %.2f", (float) totalTurnaround / tasks.size()));

        resultText.setText(result.toString());
    }

    private void executeSequence(List<Reminder> tasks, int index) {
        if (index >= tasks.size()) {
            finishExecution();
            return;
        }

        Reminder r = tasks.get(index);
        statusCard.setVisibility(View.VISIBLE);
        activeReminderText.setText(String.format(Locale.US, "Current: %s", r.title));
        
        playAlarmSound();
        
        final int durationMs = r.burst * 1000;
        executionProgress.setMax(durationMs);
        executionProgress.setProgress(0);

        final long startTime = System.currentTimeMillis();
        
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTime;
                if (elapsed < durationMs) {
                    executionProgress.setProgress((int) elapsed);
                    mainHandler.postDelayed(this, 50);
                } else {
                    stopAlarmSound();
                    executeSequence(tasks, index + 1);
                }
            }
        });
    }

    private void playAlarmSound() {
        try {
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alert == null) alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            currentRingtone = RingtoneManager.getRingtone(getApplicationContext(), alert);
            if (currentRingtone != null) currentRingtone.play();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void stopAlarmSound() {
        if (currentRingtone != null && currentRingtone.isPlaying()) currentRingtone.stop();
    }

    private void finishExecution() {
        isExecuting = false;
        statusCard.setVisibility(View.GONE);
        runBtn.setEnabled(true);
        runBtn.setText("▶ Run");
        Toast.makeText(this, "Sequence completed", Toast.LENGTH_SHORT).show();
    }

    private void resetAll() {
        stopAlarmSound();
        mainHandler.removeCallbacksAndMessages(null);
        reminders.clear();
        updateQueueDisplay();
        resultText.setText("Results will appear here");
        statusCard.setVisibility(View.GONE);
        isExecuting = false;
        runBtn.setEnabled(true);
        runBtn.setText("▶ Run");
        Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeHandler.removeCallbacksAndMessages(null);
        mainHandler.removeCallbacksAndMessages(null);
        stopAlarmSound();
    }
}
