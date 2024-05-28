package pl.edu.pja.tpo10.links.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public LinkService(LinkRepository linkRepository, LinkDtoMapper linkDtoMapper, ObjectMapper objectMapper)
    {
        this.linkRepository = linkRepository;
        this.linkDtoMapper = linkDtoMapper;
        this.objectMapper = objectMapper;
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
        String password = link.getPassword();
        link.setPassword(null);
        Link patchedLink = applyPatch(link, patch);
        if (!patchedLink.getLinkId().equals(link.getLinkId())
                || !patchedLink.getRedirectUrl().equals(link.getRedirectUrl())
                || !patchedLink.getVisits().equals(link.getVisits()))
        {
            throw new IllegalUpdateException();
        }
        if (!password.equals(patchedLink.getPassword()))
        {
            throw new WrongPasswordException("wrong password");
        }
        linkRepository.save(patchedLink);
    }

    private Link applyPatch(Link link, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException
    {
        JsonNode linkNode = objectMapper.valueToTree(link);
        JsonNode patchNode = patch.apply(linkNode);
        return objectMapper.treeToValue(patchNode, Link.class);
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
