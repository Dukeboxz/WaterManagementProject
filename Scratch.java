import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.time.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jom.*;
import javafx.scene.control.ComboBox;

/**
 * Created by stephen on 14/06/17.
 * Class to test ideas out in will not form part of final product
 */
public class Scratch {

    public static void main(String[] args) {

        for(int i = 0; i < 35; i++) {

            LocalDate now = LocalDate.now();
           // System.out.println(now);
           // System.out.println(now.plusDays(i));
            LocalDate then = now.plusDays(i);
            System.out.println(now.until(then, ChronoUnit.DAYS));
        }

    }

}
