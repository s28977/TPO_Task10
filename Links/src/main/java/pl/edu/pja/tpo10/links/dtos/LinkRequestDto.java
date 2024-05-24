package pl.edu.pja.tpo10.links.dtos;


public class LinkRequestDto
{
    private String name;
    private String targetUrl;
    private String password;

    public LinkRequestDto()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTargetUrl()
    {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl)
    {
        this.targetUrl = targetUrl;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
