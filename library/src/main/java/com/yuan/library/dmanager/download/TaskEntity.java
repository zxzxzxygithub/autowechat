package com.yuan.library.dmanager.download;

import android.text.TextUtils;
import android.util.Log;

import com.yuan.library.BuildConfig;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.File;

/**
 * Created by Yuan on 8/17/16.
 * <p>
 * download status
 */

@Entity(nameInDb = "download_status")
public class TaskEntity {

    /**
     *  + "id INTEGER PRIMARY KEY autoincrement,"
     + "taskId TEXT,"
     + "totalSize LONG,"
     + "completedSize LONG,"
     + "url TEXT,"
     + "filePath TEXT,"
     + "fileName TEXT,"
     + "taskStatus INTEGER" + ");";
     */
    @Id
    private Long taskId;
    @Property
    private long totalSize;
    @Property
    private long completedSize;
    @Property
    private String url;
    @Property
    private String filePath;
    @Property
    private String fileName;
    @Property
    private int taskStatus;


    private TaskEntity(Builder builder) {
        this.taskId = builder.taskId;
        this.totalSize = builder.totalSize;
        this.completedSize = builder.completedSize;
        this.url = builder.url;
        this.filePath = builder.filePath;
        this.fileName = builder.fileName;
        this.taskStatus = builder.taskStatus;
    }


    @Generated(hash = 397975341)
    public TaskEntity() {
    }


    @Generated(hash = 996475170)
    public TaskEntity(Long taskId, long totalSize, long completedSize, String url,
            String filePath, String fileName, int taskStatus) {
        this.taskId = taskId;
        this.totalSize = totalSize;
        this.completedSize = completedSize;
        this.url = url;
        this.filePath = filePath;
        this.fileName = fileName;
        this.taskStatus = taskStatus;
    }

    public Long getTaskId() {
        taskId = taskId==0 ? url.hashCode() : taskId;
        return taskId;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public long getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(Long completedSize) {
        this.completedSize = completedSize;
    }

    public String getUrl() {
        return url;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public void setCompletedSize(long completedSize) {
        this.completedSize = completedSize;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        File file = new File(filePath);
        if (!file.exists()) {
            boolean createDir = file.mkdirs();
            if (createDir) {
                if (BuildConfig.DEBUG) Log.d("DownloadTask", "create file dir success");
            }
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public static class Builder {
        // file id
        private long taskId;
        // file length
        private long totalSize;
        // file complete length
        private long completedSize;
        // file url
        private String url;
        // file save path
        private String filePath;
        // file name
        private String fileName;
        // file download status
        private int taskStatus;

        public Builder downloadId(long taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder totalSize(long totalSize) {
            this.totalSize = totalSize;
            return this;
        }

        public Builder completedSize(long completedSize) {
            this.completedSize = completedSize;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder filePath(String saveDirPath) {
            this.filePath = saveDirPath;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder downloadStatus(int downloadStatus) {
            this.taskStatus = downloadStatus;
            return this;
        }

        public TaskEntity build() {
            return new TaskEntity(this);
        }

    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "taskId='" + taskId + '\'' +
                ", totalSize=" + totalSize +
                ", completedSize=" + completedSize +
                ", url='" + url + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }


    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

}
