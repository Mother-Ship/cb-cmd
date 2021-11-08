package top.mothership.cb.cmd.biz.constant;

import org.mockito.internal.matchers.Not;

import java.util.Objects;

public class CbCmdPrefix {
    public static final char COLON = ':';
    public static final char COMMENT = '#';

    /**
     * 空字符串，用于标识命令名+空格后，下一个其他前缀之前的内容
     * 例子：!command p1 #p2，则应当使用NOTHING前缀，来标志p1对应字段
     */
    public static final char NOTHING = ' ';

    public static boolean isNothing(Character separator){
        return Objects.equals(separator, NOTHING);
    }

}
