package pl.edu.pja.tpo10.links.exceptions;

public class WrongPasswordException extends Exception
{
    public WrongPasswordException(String message)
    {
        super(message);
    }
}
