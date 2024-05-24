package pl.edu.pja.tpo10.links.repositories;


import org.springframework.data.repository.CrudRepository;
import pl.edu.pja.tpo10.links.models.Link;

public interface LinkRepository extends CrudRepository<Link, String>
{
}
