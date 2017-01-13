package app.reminder.com.simplereminderapp;


public class Task {
    private int id;
    private String task_dttm;
    private int task_priority;
    private String task_description;
    private String task_reminder;
    private String task_reminder_dttm;

    public Task(int id, String task_dttm,int task_priority, String task_description, String task_reminder, String task_reminder_dttm)
    {
        super();
        this.id = id;
        this.task_dttm = task_dttm;
        this.task_priority = task_priority;
        this.task_description = task_description;
        this.task_reminder = task_reminder;
        this.task_reminder_dttm = task_reminder_dttm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask_dttm() {
        return task_dttm;
    }

    public void setTask_dttm(String task_dttm) {
        this.task_dttm = task_dttm;
    }

    public int getTask_priority() {
        return task_priority;
    }

    public void setTask_priority(int task_priority) {
        this.task_priority = task_priority;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public String getTask_reminder() {
        return task_reminder;
    }

    public void setTask_reminder(String task_reminder) {
        this.task_reminder = task_reminder;
    }

    public String getTask_reminder_dttm() {
        return task_reminder_dttm;
    }

    public void setTask_reminder_dttm(String task_reminder_dttm) {
        this.task_reminder_dttm = task_reminder_dttm;
    }
}
