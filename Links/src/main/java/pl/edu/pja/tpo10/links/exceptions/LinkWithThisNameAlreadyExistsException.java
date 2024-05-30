package pl.edu.pja.tpo10.links.exceptions;

public class LinkWithThisNameAlreadyExistsException extends Exception
{
    public LinkWithThisNameAlreadyExistsException(String name){
        super("Link with name: " + name + " already exists.");
    }
}
