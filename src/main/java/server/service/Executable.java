package server.service;

import server.communication.RequestFromClient;
import server.communication.ServerResponse;

public interface Executable {
    ServerResponse processData(RequestFromClient requestFromClient,
                               ResponseProducer responseProducer);
}
