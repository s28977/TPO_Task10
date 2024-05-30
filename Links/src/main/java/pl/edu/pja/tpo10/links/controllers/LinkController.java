package pl.edu.pja.tpo10.links.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.edu.pja.tpo10.links.dtos.LinkRequestDto;
import pl.edu.pja.tpo10.links.dtos.LinkResponseDto;
import pl.edu.pja.tpo10.links.exceptions.LinkWithThisNameAlreadyExistsException;
import pl.edu.pja.tpo10.links.models.Link;
import pl.edu.pja.tpo10.links.services.LinkService;

import java.util.NoSuchElementException;

@Controller
public class LinkController
{
    private final LinkService linkService;

    public LinkController(LinkService linkService)
    {
        this.linkService = linkService;
    }

    @GetMapping("/addLink")
    public String newLink(Model model)
    {
        model.addAttribute("link", new LinkRequestDto());
        return "addLink";
    }

    @PostMapping("/saveLink")
    public RedirectView saveLink(LinkRequestDto linkRequestDto) throws LinkWithThisNameAlreadyExistsException
    {
        if(linkRequestDto.getPassword().isEmpty())
            linkRequestDto.setPassword(null);
        LinkResponseDto linkResponseDto = linkService.saveLink(linkRequestDto);
        return new RedirectView("/link/" + linkResponseDto.getLinkId(), true, false);
    }

    @GetMapping("/link/{id}")
    public String getLink(@PathVariable String id, Model model)
    {
        model.addAttribute("link", linkService.getLinkById(id));
        return "link";
    }

    @GetMapping("/red/{id}")
    public RedirectView redirect(@PathVariable String id)
    {
        try
        {
            return new RedirectView(linkService.getTargetUrlAndIncrementVisits(id), false, false);
        } catch (NoSuchElementException e)
        {
            return new RedirectView("/link?id=" + id, true, false);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @GetMapping("/invalidId")
    public String invalidId(@RequestParam String id, Model model)
    {
        model.addAttribute("id", id);
        return "invalidId";
    }
}
