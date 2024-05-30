package pl.edu.pja.tpo10.links.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.stereotype.Service;
import pl.edu.pja.tpo10.links.exceptions.ImmutableFieldException;
import pl.edu.pja.tpo10.links.exceptions.WrongPasswordException;
import pl.edu.pja.tpo10.links.models.Link;
import pl.edu.pja.tpo10.links.dtos.LinkRequestDto;
import pl.edu.pja.tpo10.links.dtos.LinkResponseDto;
import pl.edu.pja.tpo10.links.repositories.LinkRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LinkService
{
    private final LinkRepository linkRepository;
    private final LinkDtoMapper linkDtoMapper;
    private final PatchService patchService;

    public LinkService(LinkRepository linkRepository, LinkDtoMapper linkDtoMapper, PatchService patchService)
    {
        this.linkRepository = linkRepository;
        this.linkDtoMapper = linkDtoMapper;
        this.patchService = patchService;
    }

    public LinkResponseDto saveLink(LinkRequestDto linkRequestDto)
    {
        Link link = linkRepository.save(linkDtoMapper.map(linkRequestDto));
        return linkDtoMapper.map(link);
    }

    public LinkResponseDto getLinkById(String id)
    {
        return linkRepository.findById(id).map(linkDtoMapper::map).orElseThrow();
    }

    public void updateLink(String id, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException, NoSuchElementException, WrongPasswordException, ImmutableFieldException
    {
        Link link = linkRepository.findById(id).orElseThrow();
        if(patchService.hasIncorrectFields(patch))
            throw new ImmutableFieldException();
        Optional<String> password = patchService.getPassword(patch);
        if(password.isEmpty() || !password.get().equals(link.getPassword()))
            throw new WrongPasswordException();
        Link patchedLink = patchService.applyPatch(link, patch);
        linkRepository.save(patchedLink);
    }

    public void deleteLink(String id, String password) throws WrongPasswordException
    {
        Link link = linkRepository.findById(id).orElseThrow();
        if (link.getPassword() == null)
        {
            throw new NoSuchElementException();
        }
        if (!link.getPassword().equals(password))
        {
            throw new WrongPasswordException();
        }
        linkRepository.deleteById(id);
    }

    public String  getTargetUrlAndIncrementVisits(String id)
    {
        Link link = linkRepository.findById(id).orElseThrow();
        incrementVisits(link);
        return link.getTargetUrl();
    }

    private void incrementVisits(Link link)
    {
        link.setVisits(link.getVisits() + 1);
        linkRepository.save(link);
    }
}
