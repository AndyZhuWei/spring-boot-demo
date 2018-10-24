package cn.andy.conditional;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 17:29
 * @Description:
 */
public class LinuxListService implements ListService{

    @Override
    public String showListCmd() {
        return "ls";
    }
}
