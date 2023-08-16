package com.bestTravel.infrastructure.helpers;

import com.bestTravel.util.exception.ForbidenCustomerException;
import org.springframework.stereotype.Component;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Component
public class BlackListHelper {

    public void isInBlackListCustomer(String customerId){
        if (customerId.equals("VIKI771012HMCRG093")){
            throw new  ForbidenCustomerException();
        }
    }
}
