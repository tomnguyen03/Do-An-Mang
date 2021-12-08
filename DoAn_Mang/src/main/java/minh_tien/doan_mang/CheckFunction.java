/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minh_tien.doan_mang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Tom
 */
public class CheckFunction {
    public CheckFunction() {
        
    }
    public static boolean check_IP(String n) {
        if (n == null || n.length()>15 || n.length()<11) return false;
        else {
            String strPattern = "[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}";
            
            //String strPattern = "[^0-9]{1,3}+\\.[^0-9]{1,3}+\\.[^0-9]{1,3}+\\.[^0-9]{1,3}";
            //String strPattern = "[^0-9]+\\.[^0-9]+\\.[^0-9]+\\.[^0-9]+\\.[^0-9]";  //co dang 1.1.1.1
            //String strPattern = "[^0-9][^.]";
            Pattern p;
            Matcher m;
            int flag = Pattern.CASE_INSENSITIVE;
            p = Pattern.compile(strPattern,flag);
            m = p.matcher(n);
            return m.find();
        }
    }
}
