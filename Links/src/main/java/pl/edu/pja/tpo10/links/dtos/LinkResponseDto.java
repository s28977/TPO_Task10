package pl.edu.pja.tpo10.links.dtos;

public class LinkResponseDto
{
    private String linkId;

    private String name;

    private String targetUrl;

    private String redirectUrl;

    private Integer visits;

    public LinkResponseDto()
    {
    }

    public String getLinkId()
    {
        return linkId;
    }

    public void setLinkId(String linkId)
    {
        this.linkId = linkId;
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

    public String getRedirectUrl()
    {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl)
    {
        this.redirectUrl = redirectUrl;
    }

    public Integer getVisits()
    {
        return visits;
    }

    public void setVisits(Integer visits)
    {
        this.visits = visits;
    }
}
