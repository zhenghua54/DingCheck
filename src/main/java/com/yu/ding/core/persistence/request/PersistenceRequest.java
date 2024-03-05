package com.yu.ding.core.persistence.request;

import com.yu.ding.core.persistence.constants.DataOperation;
import com.yu.ding.core.persistence.constants.PersistenceType;

/**
 * @author MrYu(YWG)
 * @date 2022-07-07 12:11:50
 */
public interface PersistenceRequest {
    /**
     * get the data of the request
     */
    Object getData();

    /**
     * get the type of the data type
     */
    default String getType(){return PersistenceType.ENTITY;}

    /**
     * get operation type
     */
    DataOperation operationType();
    /**
     * need to rewrite the toString method
     */


}
