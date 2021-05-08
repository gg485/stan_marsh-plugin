package stan.marsh.plugin.domain;

import org.springframework.aop.framework.Advised;

public class TargetInfo {
    String targetService;
    Class<? extends Advised>[] proxiedClasses;

    public TargetInfo() {
    }

    public String getTargetService() {
        return targetService;
    }

    public TargetInfo setTargetService(String targetService) {
        this.targetService = targetService;
        return this;
    }

    public Class<? extends Advised>[] getProxiedAnnotatedClasses() {
        return proxiedClasses;
    }

    public TargetInfo setProxiedAnnotatedClasses(Class<? extends Advised>[] proxiedAnnotatedClasses) {
        this.proxiedClasses = proxiedAnnotatedClasses;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TargetInfo that = (TargetInfo) o;

        return targetService.equals(that.targetService);
    }

    @Override
    public int hashCode() {
        return targetService.hashCode();
    }
}
