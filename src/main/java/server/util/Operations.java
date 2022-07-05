package server.util;

import java.util.HashMap;
import java.util.Map;
import server.service.DataDeleter;
import server.service.DataGetter;
import server.service.DataSetter;
import server.service.Executable;

public final class Operations {
    public static final Map<String, Executable> processesMap = new HashMap<>();

    static {
        processesMap.put("get", new DataGetter());
        processesMap.put("set", new DataSetter());
        processesMap.put("delete", new DataDeleter());
    }
}
