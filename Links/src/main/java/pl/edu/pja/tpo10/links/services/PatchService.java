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

@Service
public class PatchService
{
    private final ObjectMapper objectMapper;

    public PatchService(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public boolean hasIllegalFields(JsonMergePatch patch) throws JsonPatchException
    {
        JsonNode node = patch.apply(objectMapper.valueToTree(new Link()));
        return node.has("linkId") || node.has("redirectUrl") || node.has("visits");
    }

    public boolean hasPassword(JsonMergePatch patch) throws JsonPatchException
    {
        return patch.apply(objectMapper.valueToTree(new Link())).has("password");
    }


    public Link applyPatch(Link link, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException, WrongPasswordException, IllegalUpdateException
    {
        if (!hasPassword(patch))
        {
            throw new WrongPasswordException("wrong password");
        }
        if (hasIllegalFields(patch))
        {
            throw new IllegalUpdateException();
        }
        JsonNode linkNode = objectMapper.valueToTree(link);
        JsonNode patchNode = patch.apply(linkNode);
        return objectMapper.treeToValue(patchNode, Link.class);
    }
}
