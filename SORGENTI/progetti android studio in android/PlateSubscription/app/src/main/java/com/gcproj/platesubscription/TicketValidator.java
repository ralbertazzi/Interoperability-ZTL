package com.gcproj.platesubscription;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gabriele on 07/02/2017.
 */
public class TicketValidator {
    private long ticket;
    private String category; //type of the pass
    private String plate;

    public TicketValidator() {
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public long getTicket() {
        return ticket;
    }

    public void setTicket(long ticket) {
        this.ticket = ticket;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isValid() {
        if(ticket < 0 || category == null)
            return false;
        else
            return true;
    }

    public long getDeadline() {
        if(ticket < 0 || category == null)
            throw new IllegalStateException("null params");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if(category.equals("1-hour")){
            calendar.add(Calendar.HOUR,1);
        }else if(category.equals("1-month")){
            calendar.add(Calendar.MONTH,1);
        }else if(category.equals("1-year")){
            calendar.add(Calendar.YEAR,1);
        } else{ //Unlimited-access
            calendar.add(Calendar.YEAR,999);
        }
        return calendar.getTimeInMillis();

    }

    public static String[] getTypeValues(){
        return new String[]{
                "Unlimited-access",
                "1-hour",
                "1-month",
                "1-year"
        };
    }
}