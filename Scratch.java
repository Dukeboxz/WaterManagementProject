import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.time.*;

import com.jom.*;
/**
 * Created by stephen on 14/06/17.
 * Class to test ideas out in will not form part of final product
 */
public class Scratch {

    public static void main(String[] args) {

        System.out.println( LocalDate.now());

        LocalDate then = LocalDate.of(2017, 06, 25);

        LocalDate now = LocalDate.now();

        System.out.println(then.until(now));

        String value = "2 Vegetable";

        int v = Integer.parseInt(value.substring(0, 1));
        System.out.println(v + 10);

    }

}
