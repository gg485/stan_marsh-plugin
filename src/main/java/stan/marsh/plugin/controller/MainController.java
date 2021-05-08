package stan.marsh.plugin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import stan.marsh.plugin.dao.PluginDao;
import stan.marsh.plugin.domain.PluginInfo;
import stan.marsh.plugin.label.Plugged;
import stan.marsh.plugin.service.PluginService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    ApplicationContext ac;
    @Autowired
    PluginDao pluginDao;
    @Autowired
    PluginService pluginService;

    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile file,
                       @RequestParam("name") String name,
                       @RequestParam("desc") String desc,
                       @RequestParam("url") String jarURL,
                       @RequestParam("class") String className) {
        String fileName = file.getOriginalFilename();
        String filePath = "file:D:\\Desktop\\plugin\\plugins";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
        } catch (IOException ignored) {
            return;
        }
        pluginDao.save(new PluginInfo().setName(name).setDesc(desc).setJarURL(jarURL).setClassName(className));
    }

    @GetMapping("/list_plugins")
    public List<PluginInfo> listPlugins() {
        List<PluginInfo> allPlugins = pluginDao.listPlugins();
        Collection<Integer> runningPlugins = pluginService.getRunningPlugins();
        for (PluginInfo plugin : allPlugins) {
            if(runningPlugins.contains(plugin.getId())){
                plugin.setActive(true);
            }
        }
        return allPlugins;
    }

    @GetMapping("/active")
    public void active(@RequestParam int pluginId,
                       @RequestParam List<String> targetMethods) {
        PluginInfo pluginInfo = pluginDao.getPluginById(pluginId);
        if (pluginInfo != null) {
            pluginService.active(
                    pluginId,
                    pluginInfo.getJarURL(),
                    pluginInfo.getClassName(),
                    targetMethods
            );
        }
    }

    @GetMapping("/inactive")
    public void inactive(@RequestParam int pluginId) {
        PluginInfo pluginInfo = pluginDao.getPluginById(pluginId);
        if (pluginInfo != null) {
            pluginService.inactive(pluginId);
        }
    }

    @GetMapping("/list_methods")
    public List<String> listMethods() {
        List<String> ret = new ArrayList<>();
        for (String name : ac.getBeanDefinitionNames()) {
            Object bean = ac.getBean(name);
            Class<?> beanClass = bean.getClass();
            if (!(beanClass.isAnnotationPresent(Service.class)
                    || beanClass.isAnnotationPresent(Controller.class))) {
                continue;
            }
            if (beanClass.isAnnotationPresent(Plugged.class)) {
                for (Method method : beanClass.getDeclaredMethods()) {
                    if (Modifier.isPublic(method.getModifiers())) {
                        ret.add(String.join("#", beanClass.getName(), method.getName()));
                    }
                }
            }
        }
        return ret;
    }
}
