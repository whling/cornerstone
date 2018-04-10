package com.whl.cornerstone.zookeeper.drm;

/**
 * Created by whling on 2018/4/12.
 */
public class AppDrmNode {
    private static final String SPILTER = "\\|\\|";
    private Object obj;
    private String parmname;
    private String configname;
    private String value;
    private String classname;
    private boolean persistent;
    private boolean overwrite;

    public AppDrmNode(Object obj, String parmname, String value) {
        this.obj = obj;
        this.parmname = parmname;
        this.value = value;
        this.classname = obj.getClass().getSimpleName();
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.classname == null ? 0 : this.classname.hashCode());
        result = 31 * result + (this.obj == null ? 0 : this.obj.hashCode());
        result = 31 * result + (this.parmname == null ? 0 : this.parmname.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AppDrmNode other = (AppDrmNode) obj;
        if (this.classname == null) {
            if (other.classname != null) {
                return false;
            }
        } else if (!this.classname.equals(other.classname)) {
            return false;
        }
        if (this.obj == null) {
            if (other.obj != null) {
                return false;
            }
        } else if (!this.obj.equals(other.obj)) {
            return false;
        }
        if (this.parmname == null) {
            if (other.parmname != null) {
                return false;
            }
        } else if (!this.parmname.equals(other.parmname)) {
            return false;
        }
        return true;
    }

    public Object getObj() {
        return this.obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getParmname() {
        return this.parmname;
    }

    public void setParmname(String parmname) {
        this.parmname = parmname;
    }

    public String getConfigname() {
        return this.configname;
    }

    public void setConfigname(String configname) {
        this.configname = configname;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getClassname() {
        return this.classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public boolean isOverwrite() {
        return this.overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public String toString() {
        return "AppDrmNode{obj=" + this.obj + ", parmname='" + this.parmname + '\'' + ", configname='" + this.configname + '\'' + ", value='" + this.value + '\'' + ", classname='" + this.classname + '\'' + ", persistent=" + this.persistent + ", overwrite=" + this.overwrite + '}';
    }
}