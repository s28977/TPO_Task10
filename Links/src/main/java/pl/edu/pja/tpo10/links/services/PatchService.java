package pl.edu.pja.tpo10.links.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.stereotype.Service;
import pl.edu.pja.tpo10.links.exceptions.WrongPasswordException;
import pl.edu.pja.tpo10.links.models.Link;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class PatchService
{
    private final ObjectMapper objectMapper;

    public PatchService(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public boolean hasIncorrectFields(JsonMergePatch patch) throws JsonPatchException
    {
        JsonNode patchNode = patch.apply(objectMapper.valueToTree(new Link()));
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(patchNode.fieldNames(), Spliterator.ORDERED), false)
                .allMatch(field -> field.equals("name") || field.equals("targetUrl") );
    }

    public Optional<String> getPassword(JsonMergePatch patch) throws JsonPatchException
    {
        JsonNode patchNode = patch.apply(objectMapper.valueToTree(new Link()));
        if(patchNode.hasNonNull("pass"))
            return Optional.of(patchNode.get("pass").asText());
        else
            return Optional.empty();
    }

    public Link applyPatch(Link link, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException, WrongPasswordException, IllegalArgumentException
    {
        return objectMapper.treeToValue(patch.apply(objectMapper.valueToTree(link)), Link.class);
    }
}
