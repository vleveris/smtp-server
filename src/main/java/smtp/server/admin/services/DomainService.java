package smtp.server.admin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smtp.server.admin.models.Domain;
import smtp.server.admin.repositories.DomainRepository;

import java.util.List;

@Service
public class DomainService implements IDomainService {

    @Autowired
    private DomainRepository repository;

    @Override
    public List<Domain> findAll() {

        var domains = (List<Domain>) repository.findAll();

        return domains;
    }

    @Override
    public Domain findDomainByName(String name) {
        return repository.findDomainByName(name);
    }

    public Domain findDomainById(long id) {
        return repository.findDomainById(id);
    }

    public Domain save(Domain domain) {
        return repository.save(domain);
    }

    public void delete(Domain domain) {
        repository.delete(domain);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }


}