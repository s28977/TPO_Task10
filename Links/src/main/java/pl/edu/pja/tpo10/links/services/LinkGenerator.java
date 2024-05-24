package pl.edu.pja.tpo10.links.services;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LinkGenerator
{
    public String generate()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++)
        {
            stringBuilder.append(getRandomLetter());
        }
        return stringBuilder.toString();
    }

    private char getRandomLetter()
    {
        int random = (int) (Math.random() * 52);
        if (random < 26)
        {
            return (char) ('A' + random);
        } else
        {
            return (char) ('a' + random - 26);
        }
    }
}
