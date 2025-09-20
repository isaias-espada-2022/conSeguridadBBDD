package domina.springboot.verduleria.back;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VerduleriaRepository extends CrudRepository<Verdura, Long>, PagingAndSortingRepository<Verdura, Long> {

}
