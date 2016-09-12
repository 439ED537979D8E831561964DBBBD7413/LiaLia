package cn.chono.yopper.Service.Http.ProfileUser;

import java.util.List;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class ProfileUserBean extends ParameterBean {

    private int userId;

    private Integer age ;

    private Integer relationship;

    private Integer height;

    private Integer weight;

    private String career;

    private Integer incomeLevel;

    private String tags;


    private String name;
    private boolean birthdayPrivacy;
    private String likes;
    private String dislikes;
    private String headImg;

    private List<String> album;
    private String hometown;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getRelationship() {
        return relationship;
    }

    public void setRelationship(Integer relationship) {
        this.relationship = relationship;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public Integer getIncomeLevel() {
        return incomeLevel;
    }

    public void setIncomeLevel(Integer incomeLevel) {
        this.incomeLevel = incomeLevel;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBirthdayPrivacy() {
        return birthdayPrivacy;
    }

    public void setBirthdayPrivacy(boolean birthdayPrivacy) {
        this.birthdayPrivacy = birthdayPrivacy;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public List<String> getAlbum() {
        return album;
    }

    public void setAlbum(List<String> album) {
        this.album = album;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }
}