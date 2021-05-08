package stan.marsh.plugin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stan.marsh.plugin.domain.TargetInfo;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConnService {
    ConcurrentHashMap<TargetInfo,Long>targets=new ConcurrentHashMap<>();
    ThreadLocal<TargetInfo> threadLocalTargetInfo = ThreadLocal.withInitial(TargetInfo::new);

    public void report(TargetInfo targetInfo){
        targets.put(targetInfo,System.currentTimeMillis());
    }

    public void heartbeat(String targetService){
        TargetInfo targetInfo = threadLocalTargetInfo.get();
        targetInfo.setTargetService(targetService);
        targets.put(targetInfo,System.currentTimeMillis());
    }

    public boolean isAlive(String targetService){
        return true;
    }
}
