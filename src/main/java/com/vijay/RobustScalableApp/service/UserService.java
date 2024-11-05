package com.vijay.RobustScalableApp.service;

import com.vijay.RobustScalableApp.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService extends AsyncCrudOperations<User, Long> {


}
