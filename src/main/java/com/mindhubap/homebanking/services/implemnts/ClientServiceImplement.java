package com.mindhubap.homebanking.services.implemnts;

import com.mindhubap.homebanking.dtos.ClientDTO;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.repositories.ClientRepository;
import com.mindhubap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<ClientDTO> convertToClientsDTO(List<Client> clients) {
        return clients.stream().map(ClientDTO :: new).collect(Collectors.toList());
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public Client findById(Long Id) {
        return clientRepository.findById(Id).orElse(null);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
