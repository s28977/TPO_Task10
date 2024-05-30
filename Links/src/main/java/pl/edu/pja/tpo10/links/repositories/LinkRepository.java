package pl.edu.pja.tpo10.links.repositories;


import org.springframework.data.repository.CrudRepository;
import pl.edu.pja.tpo10.links.models.Link;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface LinkRepository extends CrudRepository<Link, String>
{
    boolean existsByName(String name);
//    Optional<Link> findByName(String name);
}
