package stan.marsh.plugin.listener;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import stan.marsh.plugin.label.Plugged;

import java.util.Map;

@Component
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent e) {
        ConfigurableApplicationContext ac = (ConfigurableApplicationContext) e.getApplicationContext();
        Map<String, Object> beansWithAnnotation = ac.getBeansWithAnnotation(Plugged.class);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ac.getBeanFactory();
        for (String key : beansWithAnnotation.keySet()) {
            Object bean = beansWithAnnotation.get(key);
            ProxyFactory factory = new ProxyFactory(bean);
            Object proxy = factory.getProxy();

            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(key);
            beanFactory.removeBeanDefinition(key);
            beanDefinition.setBeanClassName(proxy.getClass().getCanonicalName());
            beanFactory.registerBeanDefinition(key, beanDefinition);
            beanFactory.registerSingleton(key, proxy);
        }
    }
}
