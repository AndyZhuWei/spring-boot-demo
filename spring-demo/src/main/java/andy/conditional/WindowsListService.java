package andy.conditional;

/**
 * @Author: zhuwei
 * @Date:2018/10/23 17:29
 * @Description:
 */
public class WindowsListService implements ListService{

    @Override
    public String showListCmd() {
        return "dir";
    }
}
