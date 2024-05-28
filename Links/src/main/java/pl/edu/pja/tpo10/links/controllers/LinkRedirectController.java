package pl.edu.pja.tpo10.links.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;
import pl.edu.pja.tpo10.links.services.LinkService;

import java.util.NoSuchElementException;

@Controller
public class LinkRedirectController
{
    private final LinkService linkService;

    public LinkRedirectController(LinkService linkService)
    {
        this.linkService = linkService;
    }
    @GetMapping("/red/{id}")
    public RedirectView redirect(@PathVariable String id)
    {
        try
        {
            return new RedirectView(linkService.getTargetUrlAndIncrementVisits(id), false, false);
        } catch (NoSuchElementException e)
        {
            return new RedirectView("/invalidId?id="+id, true, false);
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
