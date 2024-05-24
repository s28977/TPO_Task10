package pl.edu.pja.tpo10.links.services;

import org.springframework.stereotype.Service;
import pl.edu.pja.tpo10.links.models.Link;
import pl.edu.pja.tpo10.links.dtos.LinkRequestDto;
import pl.edu.pja.tpo10.links.dtos.LinkResponseDto;
import pl.edu.pja.tpo10.links.repositories.LinkRepository;

@Service
public class LinkDtoMapper
{
    private final LinkGenerator linkGenerator;
    private final LinkRepository linkRepository;

    public LinkDtoMapper(LinkGenerator linkGenerator, LinkRepository linkRepository)
    {
        this.linkGenerator = linkGenerator;
        this.linkRepository = linkRepository;
    }

    public Link map(LinkRequestDto linkRequestDto)
    {
        Link link = new Link();
        link.setName(linkRequestDto.getName());
        link.setTargetUrl(linkRequestDto.getTargetUrl());
        link.setPassword(linkRequestDto.getPassword());
        link.setVisits(0);
        String linkId = linkGenerator.generate();
        while (linkRepository.existsById(linkId))
        {
            linkId = linkGenerator.generate();
        }
        link.setLinkId(linkId);
        link.setRedirectUrl("http://localhost:8080/red/" + linkId);
        return link;
    }

    public LinkResponseDto map(Link link)
    {
        LinkResponseDto linkResponseDto = new LinkResponseDto();
        linkResponseDto.setLinkId(link.getLinkId());
        linkResponseDto.setName(link.getName());
        linkResponseDto.setVisits(link.getVisits());
        linkResponseDto.setRedirectUrl(link.getRedirectUrl());
        linkResponseDto.setTargetUrl(link.getTargetUrl());
        return linkResponseDto;
    }
}
