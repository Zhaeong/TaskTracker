package io.github.zhaeong.worktracker;

/**
 * Created by Owen on 2017-01-24.
 * The object that represents the list
 */

public class TaskObject {
    //public int TaskId;
    public String Name;
    public String Description;

    public TaskObject(/*int taskid,*/ String name, String description)
    {
        //this.TaskId = taskid;
        this.Name = name;
        this.Description = description;
    }
}
