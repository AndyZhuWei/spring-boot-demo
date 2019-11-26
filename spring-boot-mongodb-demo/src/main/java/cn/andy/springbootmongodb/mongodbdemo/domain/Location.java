package cn.andy.springbootmongodb.mongodbdemo.domain;

/**
 * @Author: zhuwei
 * @Date:2019/11/26 9:41
 * @Description:
 */
public class Location {

    private String place;

    private String year;

    public Location(String place, String year) {
        super();
        this.place = place;
        this.year = year;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
