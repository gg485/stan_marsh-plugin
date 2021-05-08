package stan.marsh.plugin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import stan.marsh.plugin.domain.PluginInfo;

import java.util.List;

@Mapper
@Repository
public interface PluginDao {
    void save(PluginInfo pluginInfo);
    List<PluginInfo> listPlugins();
    PluginInfo getPluginById(@Param("id") int id);
}
