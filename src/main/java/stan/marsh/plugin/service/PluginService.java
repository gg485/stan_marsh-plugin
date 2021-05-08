package stan.marsh.plugin.service;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PluginService {
    @Autowired
    ApplicationContext ac;
    private final Map<Integer, Advisor[]> advisorCache = new ConcurrentHashMap<>();
    private final Map<String, Advice[]> adviceCache = new ConcurrentHashMap<>();

    public void active(int pluginId,String jarURL,String pluginClassName, List<String> methodNames) {
        if (advisorCache.containsKey(pluginId)) {
            return;
        }
        boolean isValid = false;
        NameMatchMethodPointcutAdvisor advisorBefore = null;
        NameMatchMethodPointcutAdvisor advisorAfter = null;
        for (String name : ac.getBeanDefinitionNames()) {
            Object bean = ac.getBean(name);
            if (!(bean instanceof Advised)) {
                continue;
            }
            Advice[] advices;
            try {
                advices = buildAdvice(jarURL, pluginClassName);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (advices[0] != null) {
                if (advisorBefore == null) {
                    advisorBefore = new NameMatchMethodPointcutAdvisor();
                    advisorBefore.setMappedNames(methodNames.toArray(String[]::new));
                    advisorBefore.setAdvice(advices[0]);
                }
                ((Advised) bean).addAdvisor(advisorBefore);
                isValid = true;
            }
            if (advices[1] != null) {
                if (advisorAfter == null) {
                    advisorAfter = new NameMatchMethodPointcutAdvisor();
                    advisorAfter.setMappedNames(methodNames.toArray(String[]::new));
                    advisorAfter.setAdvice(advices[1]);
                }
                ((Advised) bean).addAdvisor(advisorAfter);
                isValid = true;
            }
        }
        if (isValid) {
            advisorCache.put(pluginId, new Advisor[]{advisorBefore, advisorAfter});
        }
    }

    public void inactive(int pluginId) {
        Advisor[] advisors = advisorCache.getOrDefault(pluginId, null);
        if (advisors != null) {
            for (String name : ac.getBeanDefinitionNames()) {
                Object bean = ac.getBean(name);
                if (!(bean instanceof Advised)) {
                    continue;
                }
                for (Advisor advisor : advisors) {
                    if (advisor != null) {
                        ((Advised) bean).removeAdvisor(advisor);
                    }
                }
            }
            advisorCache.remove(pluginId);
        }
    }

    @SuppressWarnings("deprecated")
    private Advice[] buildAdvice(String jarURL, String pluginClassName) throws Exception {
        if (adviceCache.containsKey(pluginClassName)) {
            return adviceCache.get(pluginClassName);
        }
        URL url = new URL(jarURL);
        URLClassLoader loader = (URLClassLoader) getClass().getClassLoader();
        boolean isLoaded = false;
        for (URL loadedURL : loader.getURLs()) {
            if (loadedURL.equals(url)) {
                isLoaded = true;
                break;
            }
        }
        if (!isLoaded) {
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURL.setAccessible(true);
            addURL.invoke(loader, url);
        }
        Class<?> pluginClass = loader.loadClass(pluginClassName);
        MethodBeforeAdvice beforeAdvice = getBeforeAdvice(pluginClass);
        AfterReturningAdvice afterAdvice = getAfterAdvice(pluginClass);
        Advice[] advices = {beforeAdvice, afterAdvice};
        adviceCache.put(pluginClassName, advices);
        return advices;
    }

    private MethodBeforeAdvice getBeforeAdvice(Class<?> pluginClass) {
        final Method before;
        try {
            before = pluginClass.getDeclaredMethod("before", Object[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        return (method, args, target) -> before.invoke(target, args);
    }

    private AfterReturningAdvice getAfterAdvice(Class<?> pluginClass) {
        final Method after;
        try {
            after = pluginClass.getDeclaredMethod("after", Object.class, Object[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        return (returnValue, method, args, target) -> after.invoke(target, returnValue, args);
    }

    public Collection<Integer> getRunningPlugins(){
        return advisorCache.keySet();
    }
}
