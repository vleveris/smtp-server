package smtp.server.admin.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import smtp.server.admin.models.Domain;

@Repository
public interface DomainRepository extends CrudRepository<Domain, Long> {
    Domain findDomainByName(String name);

    Domain findDomainById(long id);

}