package com.mishchenkov.controller;

import com.mishchenkov.dto.UserDTO;
import com.mishchenkov.dto.UserLoginFormDTO;
import com.mishchenkov.entity.User;
import com.mishchenkov.service.repository.UserService;
import com.mishchenkov.service.repository.exception.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static com.mishchenkov.constant.AppConstant.FAIL_MSG_BED_USER_PARAM;
import static com.mishchenkov.constant.AppConstant.FAIL_MSG_INVALID_FIRST_PSW;
import static com.mishchenkov.constant.AppConstant.FAIL_MSG_INVALID_USER_MAIL;
import static com.mishchenkov.constant.AppConstant.REG_EMAIL;
import static com.mishchenkov.constant.AppConstant.REG_USER_PSW;
import static com.mishchenkov.constant.AppConstant.SESSION_ATTR_REFERER;
import static com.mishchenkov.constant.AppConstant.SESSION_ITEM_CURRENT_USER;

@WebServlet("/login")
public class LoginController extends CommonServicesHttpServlet {

    private final Logger logger = Logger.getLogger(LoginController.class);

    public static final String REQ_PARAM_USER_EMAIL = "email";
    public static final String REQ_PARAM_USER_PSW = "password";

    public static final String REQ_ATTR_ERROR_MSG = "errors";
    public static final String REQ_ATTR_FAULT_USER = "faultUser";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.trace(req.getSession().getId());

        HttpSession session = req.getSession();
        UserService userService = getUserService(req);
        Map<String, String> errorMap = new TreeMap<>();

        UserLoginFormDTO formDTO = obtainFormData(req);
        logger.trace(formDTO);
        validateForm(errorMap, formDTO);

        if (!errorMap.isEmpty()) {
            session.setAttribute(REQ_ATTR_ERROR_MSG, errorMap);
            logger.trace(errorMap);
        } else {
            Optional<UserDTO> userDTO = getUserFromService(userService, formDTO.getMail());

            if (userDTO.isPresent() && checkUserPassword(userDTO.get().getUser(), formDTO)) {
                session.setAttribute(SESSION_ITEM_CURRENT_USER, userDTO.get().getUser());
                logger.trace("user to session");
                logger.trace("CURRENT USER: " + userDTO.get().getUser());
            } else {
                errorMap.put(REQ_ATTR_FAULT_USER, FAIL_MSG_BED_USER_PARAM);
                session.setAttribute(REQ_ATTR_ERROR_MSG, errorMap);
                logger.trace("no user to session");
            }
        }
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        transportDataAndRemove(REQ_ATTR_ERROR_MSG, req);
        resp.sendRedirect((String) req.getSession().getAttribute(SESSION_ATTR_REFERER));
    }

    private boolean checkUserPassword(User user, UserLoginFormDTO formDTO) {
        String userPsw = user.getPassword();
        String formPsw = encryptMD5(formDTO.getPassword());

        return userPsw.equals(formPsw);
    }

    private String encryptMD5(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }
        md.update(password.getBytes());
        byte[] digest = md.digest();

        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }

    private Optional<UserDTO> getUserFromService(UserService userService, String mail) {
        Optional<UserDTO> user;

        try {
            user = userService.selectUserByEmail(mail);
        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }

        return user;
    }

    private void validateForm(Map<String, String> errorMap, UserLoginFormDTO formDTO) {
        validateItem(REQ_PARAM_USER_EMAIL, formDTO.getMail(), FAIL_MSG_INVALID_USER_MAIL, errorMap, REG_EMAIL);
        validateItem(REQ_PARAM_USER_PSW, formDTO.getPassword(), FAIL_MSG_INVALID_FIRST_PSW, errorMap, REG_USER_PSW);
    }

    private UserLoginFormDTO obtainFormData(HttpServletRequest req) {
        return new UserLoginFormDTO()
                .setMail(req.getParameter(REQ_PARAM_USER_EMAIL))
                .setPassword(req.getParameter(REQ_PARAM_USER_PSW));
    }
}
