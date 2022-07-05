package server.service;

import server.RequestFromClient;
import server.ServerResponse;

public interface Executable {
    ServerResponse processData(RequestFromClient requestFromClient);
}
