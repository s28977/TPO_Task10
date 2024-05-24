package pl.edu.pja.tpo10.links.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Link
{
    @Id
    @Column(name = "LinkID")
    private String linkId;

    @Column(name = "Name")
    private String name;

    @Column(name = "TargetURL")
    private String targetUrl;

    @Column(name = "RedirectURL")
    private String redirectUrl;

    @Column(name = "Visits")
    private Integer visits;

    @Column(name = "Password")
    private String password;

    public Link()
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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
