package smtp.server.admin.services;

import smtp.server.admin.models.Domain;

import java.util.List;

public interface IDomainService {

    List<Domain> findAll();

    Domain findDomainByName(String name);

    Domain findDomainById(long id);

    Domain save(Domain domain);

    void delete(Domain domain);

    boolean existsById(Long id);

}