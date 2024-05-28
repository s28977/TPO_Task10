package pl.edu.pja.tpo10.links.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.edu.pja.tpo10.links.dtos.LinkRequestDto;
import pl.edu.pja.tpo10.links.dtos.LinkResponseDto;
import pl.edu.pja.tpo10.links.exceptions.IllegalUpdateException;
import pl.edu.pja.tpo10.links.exceptions.WrongPasswordException;
import pl.edu.pja.tpo10.links.services.LinkService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/api/links/",
                produces = MediaType.APPLICATION_JSON_VALUE)
public class LinkApiController
{
    private final LinkService linkService;

    public LinkApiController(LinkService linkService)
    {
        this.linkService = linkService;
    }

    @PostMapping()
    public ResponseEntity<LinkResponseDto> saveLink(@RequestBody LinkRequestDto linkRequestDto)
    {
        LinkResponseDto linkResponseDto = linkService.saveLink(linkRequestDto);
        return ResponseEntity.ok(linkResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkResponseDto> getLink(@PathVariable String id)
    {
        try
        {
            return ResponseEntity.ok(linkService.getLinkById(id));
        } catch (NoSuchElementException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateLink(@PathVariable String id, @RequestBody JsonMergePatch patch)
    {
        try
        {
            linkService.updateLink(id, patch);
        } catch (NoSuchElementException e)
        {
            return ResponseEntity.notFound().build();
        } catch (JsonPatchException | JsonProcessingException | IllegalUpdateException e)
        {
            return ResponseEntity.internalServerError().build();
        } catch (WrongPasswordException e)
        {
            return ResponseEntity.status(403)
                    .header("reason", e.getMessage())
                    .build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteLink(HttpServletRequest request, @PathVariable String id)
    {
        String password = request.getHeader("pass");
        try
        {
            linkService.deleteLink(id, password);
        } catch (NoSuchElementException ignore)
        {
        } catch (WrongPasswordException e)
        {
            return ResponseEntity.status(403)
                    .header("reason", e.getMessage())
                    .build();
        }
        return ResponseEntity.noContent().build();
    }
}
