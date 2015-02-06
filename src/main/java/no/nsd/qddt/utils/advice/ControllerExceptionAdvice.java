package no.nsd.qddt.utils.advice;

import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.ControllerAdviceExceptionMessage;
import no.nsd.qddt.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller-advice to handle exception casted by any requests coming from
 * controllers. This will not interfere with the service layer, but it shares
 * the exceptions that can be cast from the service layer.
 *
 * @author Dag Østgulen Heradstveit
 */
@ControllerAdvice
@RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
public class ControllerExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionAdvice.class);

    /**
     * Handle all exceptions of type {@link no.nsd.qddt.exception.ResourceNotFoundException}
     * when they occur from methods executed from the controller.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.exception.ControllerAdviceExceptionMessage} object
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody public ControllerAdviceExceptionMessage handleResourceNotFound(HttpServletRequest req, Exception e) {
        logger.error("handleResourceNotFound");
        return new ControllerAdviceExceptionMessage(
                req.getRequestURL().toString(),
                e.getLocalizedMessage()
        );
    }

    /**
     * Handle all exceptions of type {@link no.nsd.qddt.exception.UserNotFoundException}
     * when they occur from methods executed from the controller.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.exception.ControllerAdviceExceptionMessage} object
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody public ControllerAdviceExceptionMessage handleUserByEmailNotFound(HttpServletRequest req, Exception e) {
        logger.error("handleUserByEmailNotFound");
        return new ControllerAdviceExceptionMessage(
                req.getRequestURL().toString(),
                e.getLocalizedMessage()
        );
    }

    /**
     * Default exception handler.
     * Will catch all bad requests, but will not provide further details of the error.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.exception.ControllerAdviceExceptionMessage} object
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody public ControllerAdviceExceptionMessage defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        logger.error("defaultErrorHandler");
        return new ControllerAdviceExceptionMessage(
                req.getRequestURL().toString(),
                "Bad request"
        );
    }
}