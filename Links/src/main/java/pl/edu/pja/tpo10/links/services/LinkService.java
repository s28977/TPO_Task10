package pl.edu.pja.tpo10.links.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.stereotype.Service;
import pl.edu.pja.tpo10.links.exceptions.IllegalUpdateException;
import pl.edu.pja.tpo10.links.exceptions.WrongPasswordException;
import pl.edu.pja.tpo10.links.models.Link;
import pl.edu.pja.tpo10.links.dtos.LinkRequestDto;
import pl.edu.pja.tpo10.links.dtos.LinkResponseDto;
import pl.edu.pja.tpo10.links.repositories.LinkRepository;

import java.util.NoSuchElementException;

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

    public void updateLink(String id, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException, NoSuchElementException, WrongPasswordException, IllegalUpdateException
    {
        Link link = linkRepository.findById(id).orElseThrow();
        if (link.getPassword() == null)
        {
            throw new NoSuchElementException();
        }
        if (!patchService.hasPassword(patch))
        {
            throw new WrongPasswordException("wrong password");
        }
        if (patchService.hasIllegalFields(patch))
        {
            throw new IllegalUpdateException();
        }
        Link patchedLink = patchService.applyPatch(link, patch);
        if (!link.getPassword().equals(patchedLink.getPassword()))
        {
            throw new WrongPasswordException("wrong password");
        }
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
            throw new WrongPasswordException("wrong password");
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
