package com.mishchenkov.controller;

import com.mishchenkov.dto.UserDTO;
import com.mishchenkov.dto.UserRegistrationFormDTO;
import com.mishchenkov.entity.DataContainer;
import com.mishchenkov.entity.User;
import com.mishchenkov.service.CaptchaService;
import com.mishchenkov.service.repository.UserService;
import com.mishchenkov.service.repository.exception.ServiceException;
import com.mishchenkov.validator.EqualsValidator;
import com.mishchenkov.validator.NullValidator;
import com.mishchenkov.validator.RegexpValidator;
import com.mishchenkov.validator.Validator;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

import static com.mishchenkov.constant.AppConstant.FAIL_MSG_INCORRECT_CAPTCHA;
import static com.mishchenkov.constant.AppConstant.FAIL_MSG_INVALID_CAPTCHA;
import static com.mishchenkov.constant.AppConstant.FAIL_MSG_INVALID_FIRST_PSW;
import static com.mishchenkov.constant.AppConstant.FAIL_MSG_INVALID_SECOND_PSW;
import static com.mishchenkov.constant.AppConstant.FAIL_MSG_INVALID_USER_MAIL;
import static com.mishchenkov.constant.AppConstant.FAIL_MSG_INVALID_USER_NAME;
import static com.mishchenkov.constant.AppConstant.FAIL_MSG_INVALID_USER_SECOND_NAME;
import static com.mishchenkov.constant.AppConstant.FAIL_MSG_NOT_EQUAL_SECOND_PSW;
import static com.mishchenkov.constant.AppConstant.FAIL_MSG_TIMEOUT_CAPTCHA;
import static com.mishchenkov.constant.AppConstant.FAIL_MSG_USER_IS_EXIST;
import static com.mishchenkov.constant.AppConstant.PATH_IMAGES;
import static com.mishchenkov.constant.AppConstant.REG_EMAIL;
import static com.mishchenkov.constant.AppConstant.REG_USER_NAME;
import static com.mishchenkov.constant.AppConstant.REG_USER_PSW;
import static com.mishchenkov.constant.AppConstant.REQ_PARAM_USER_HIDDEN;
import static com.mishchenkov.constant.AppConstant.SERV_CXT_INIT_SERVER_PATH;
import static com.mishchenkov.constant.AppConstant.SERV_CXT_USER_AVATAR_DIRECTORY;
import static com.mishchenkov.constant.AppConstant.SESSION_ITEM_CURRENT_USER;

@WebServlet("/registration")
@MultipartConfig
public class RegistrationController extends CommonServicesHttpServlet {

    private final Logger logger = Logger.getLogger(RegistrationController.class);

    public static final String REQ_PARAM_USER_NAME = "name";
    public static final String REQ_PARAM_USER_SECOND_NAME = "second-name";
    public static final String REQ_PARAM_USER_EMAIL = "email";
    public static final String REQ_PARAM_USER_FIRST_PSW = "psw-1";
    public static final String REQ_PARAM_USER_SECOND_PSW = "psw-2";
    public static final String REQ_PARAM_USER_IS_GET_EMAIL = "is-email";
    public static final String REQ_PARAM_USER_CAPTCHA = "captcha";
    public static final String REQ_PARAM_FILE = "file";

    public static final String REQ_ATTR_ERROR_MSG = "errors";
    public static final String REQ_ATTR_FORM_BEAN = "formBean";

    public static final String PAGE_SUCCESS = "index.jsp";
    public static final String PAGE_MAIN_VIEW = "/WEB-INF/view/registration.jsp";

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        logger.info(req.getSession().getId());

        HttpSession session = req.getSession();
        UserService userService = getUserService(req);
        Map<String, String> errorMap = new TreeMap<>();

        UserRegistrationFormDTO formDto = obtainFormData(req);
        validateForm(errorMap, formDto);
        checkCaptcha(errorMap, formDto, req);

        User user = formDto.toUser();

        if (errorMap.isEmpty()) {
            saveUser(userService, errorMap, formDto, user);
        }

        logger.trace(errorMap);

        if (errorMap.isEmpty()) {
            saveUserAvatar(formDto, req.getServletContext());
            session.setAttribute(SESSION_ITEM_CURRENT_USER, user);

            resp.sendRedirect(PAGE_SUCCESS);

        } else {
            session.setAttribute(REQ_ATTR_ERROR_MSG, errorMap);
            session.setAttribute(REQ_ATTR_FORM_BEAN, formDto);

            doGet(req, resp);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info(req.getSession().getId());

        generateHiddenIdValueIfEmpty(req);
        transportSessionDataToRequestAttr(req);

        req.getRequestDispatcher(PAGE_MAIN_VIEW).forward(req, resp);
    }

    private void saveUser(UserService userService, Map<String, String> errorMap, UserRegistrationFormDTO formDto, User user) {
        try {
            if (!userService.selectUserByEmail(user.getEmail()).isPresent() ) {
                userService.insertUser(
                        new UserDTO().setUser(user).setSendEmail(formDto.getIsSendEmail())
                );
            } else {
                errorMap.put(REQ_PARAM_USER_EMAIL, FAIL_MSG_USER_IS_EXIST);
            }
        } catch (ServiceException e) {
            logger.warn(e);
            throw new IllegalStateException(e);
        }
    }

    private void saveUserAvatar(UserRegistrationFormDTO formDto, ServletContext servletContext) {
        if (formDto.getFile().length > 0) {

            String fileName = formDto.getMail().concat(".jpg");
            String contextDirName = (String) servletContext.getAttribute(SERV_CXT_USER_AVATAR_DIRECTORY);
            String serverDirPath = (String) servletContext.getAttribute(SERV_CXT_INIT_SERVER_PATH);

            File avatarDir = new File(serverDirPath.concat(PATH_IMAGES).concat(File.separator).concat(contextDirName));
            if (!avatarDir.exists()) {
                avatarDir.mkdir();
            }

            File avatar = new File(avatarDir.getPath().concat(File.separator).concat(fileName));
            logger.trace(avatarDir.getPath().concat(File.separator).concat(fileName));
            try (OutputStream outputStream = new FileOutputStream(avatar)) {
                outputStream.write(formDto.getFile());
                outputStream.flush();

            } catch (IOException e) {
                logger.warn(e);
                throw new IllegalStateException(e);
            }
        }
    }

    private void generateHiddenIdValueIfEmpty(HttpServletRequest req) {
        Object formDto = req.getAttribute(REQ_ATTR_FORM_BEAN);

        if (formDto == null) {
            req.setAttribute(REQ_PARAM_USER_HIDDEN, req.getSession().getId());
        } else {
            req.setAttribute(REQ_PARAM_USER_HIDDEN, ((UserRegistrationFormDTO)formDto).getHidden());
        }
    }

    private void transportSessionDataToRequestAttr(HttpServletRequest req) {
        transportDataAndRemove(REQ_ATTR_ERROR_MSG, req);
        transportDataAndRemove(REQ_ATTR_FORM_BEAN, req);
    }

    private void checkCaptcha(Map<String, String> errorMap, UserRegistrationFormDTO formDto, HttpServletRequest req) {
        String formCaptcha = formDto.getCaptcha();

        if (formCaptcha != null) {
            CaptchaService service = getCaptchaService(req);
            String serviceCaptcha = service.deriveCaptcha(req).getName();

            if (serviceCaptcha.equals("")) {
                errorMap.put(REQ_PARAM_USER_CAPTCHA, FAIL_MSG_TIMEOUT_CAPTCHA);
            } else {
                checkCaptchaValues(formCaptcha, serviceCaptcha, errorMap);
            }

        }
    }

    private void checkCaptchaValues(String formCaptcha, String serviceCaptcha, Map<String, String> errorMap) {
        if (!formCaptcha.equals(serviceCaptcha)) {
            errorMap.put(REQ_PARAM_USER_CAPTCHA, FAIL_MSG_INCORRECT_CAPTCHA);
        }
    }

    private void validateForm(Map<String, String> errorMap, UserRegistrationFormDTO formDto) {
        validateItem(REQ_PARAM_USER_NAME, formDto.getName(), FAIL_MSG_INVALID_USER_NAME, errorMap, REG_USER_NAME);
        validateItem(REQ_PARAM_USER_SECOND_NAME, formDto.getSecondName(), FAIL_MSG_INVALID_USER_SECOND_NAME, errorMap, REG_USER_NAME);
        validateItem(REQ_PARAM_USER_EMAIL, formDto.getMail(), FAIL_MSG_INVALID_USER_MAIL, errorMap, REG_EMAIL);
        validateItem(REQ_PARAM_USER_FIRST_PSW, formDto.getFirstPsw(), FAIL_MSG_INVALID_FIRST_PSW, errorMap, REG_USER_PSW);

        DataContainer<String, String> itemDataPair = new DataContainer<>(REQ_PARAM_USER_SECOND_PSW, formDto.getSecondPsw());
        DataContainer<String, String> pswDataPair = new DataContainer<>(formDto.getFirstPsw(), formDto.getSecondPsw());
        Validator[] validators = {
                new NullValidator(itemDataPair.changeData(), FAIL_MSG_INVALID_SECOND_PSW),
                new NullValidator(itemDataPair, FAIL_MSG_INVALID_SECOND_PSW),
                new RegexpValidator(itemDataPair, FAIL_MSG_INVALID_SECOND_PSW, REG_USER_PSW),
                new EqualsValidator(pswDataPair, REQ_PARAM_USER_SECOND_PSW, FAIL_MSG_NOT_EQUAL_SECOND_PSW)
        };
        validateItem(validators, errorMap);

        itemDataPair = new DataContainer<>(REQ_PARAM_USER_CAPTCHA, formDto.getCaptcha());
        validators = new Validator[]{ new NullValidator(itemDataPair, FAIL_MSG_INVALID_CAPTCHA) };
        validateItem(validators, errorMap);
    }

    private UserRegistrationFormDTO obtainFormData(HttpServletRequest req) throws IOException, ServletException {
        UserRegistrationFormDTO result = new UserRegistrationFormDTO();

        result.setHidden(req.getParameter(REQ_PARAM_USER_HIDDEN));
        result.setName(req.getParameter(REQ_PARAM_USER_NAME));
        result.setSecondName(req.getParameter(REQ_PARAM_USER_SECOND_NAME));
        result.setMail(req.getParameter(REQ_PARAM_USER_EMAIL));
        result.setFirstPsw(req.getParameter(REQ_PARAM_USER_FIRST_PSW));
        result.setSecondPsw(req.getParameter(REQ_PARAM_USER_SECOND_PSW));
        result.setIsSendEmail(req.getParameter(REQ_PARAM_USER_IS_GET_EMAIL));
        result.setCaptcha(req.getParameter(REQ_PARAM_USER_CAPTCHA));
        result.setFile(obtainFileData(req));

        return result;
    }

    private byte[] obtainFileData(HttpServletRequest req) throws IOException, ServletException {
        Part part = req.getPart(REQ_PARAM_FILE);

        if (part == null) return new byte[0];

        InputStream is = part.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[128];
        int count;
        while ((count = is.read(buf)) != -1) {
            os.write(buf, 0, count);
        }

        return os.toByteArray();
    }
}