package server.service;

import server.RequestFromClient;

public interface Executable {
    String processData(RequestFromClient requestFromClient);
}
