package io.github.zhaeong.worktracker.TaskConstructs;

/**
 * Created by Owen on 2017-01-24.
 * The object that represents the list
 */

public class TaskObject {
    //public int TaskId;
    public String Name;
    public String Description;
    public int isActive;

    public TaskObject(String name, String description)
    {
        //this.TaskId = taskid;
        this.Name = name;
        this.Description = description;
        this.isActive = 0;
    }
}
