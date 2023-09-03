package com.mindhubap.homebanking.services;

import com.mindhubap.homebanking.dtos.ClientDTO;
import com.mindhubap.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    List<Client> findAllClients();

    List<ClientDTO> convertToClientsDTO(List<Client> clients);

    Client findByEmail(String email);

    Client findById(Long Id);

    void saveClient(Client client);

}
