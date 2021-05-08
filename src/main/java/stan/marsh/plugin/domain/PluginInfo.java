package stan.marsh.plugin.domain;

public class PluginInfo {
    int id;
    String name;
    String desc;
    String jarURL;
    String className;
    boolean isActive;

    public String getName() {
        return name;
    }

    public PluginInfo setName(String name) {
        this.name = name;
        return this;
    }

    public int getId() {
        return id;
    }

    public PluginInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public PluginInfo setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getJarURL() {
        return jarURL;
    }

    public PluginInfo setJarURL(String jarURL) {
        this.jarURL = jarURL;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public PluginInfo setClassName(String className) {
        this.className = className;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
