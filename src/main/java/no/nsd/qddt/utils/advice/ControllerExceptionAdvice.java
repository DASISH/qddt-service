package no.nsd.qddt.utils.advice;

import no.nsd.qddt.exception.ControllerAdviceExceptionMessage;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static net.logstash.logback.encoder.org.apache.commons.lang.exception.ExceptionUtils.getRootCauseMessage;

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
        ControllerAdviceExceptionMessage message = new ControllerAdviceExceptionMessage(
                req.getRequestURL().toString(),
                e.getLocalizedMessage()
        );

        logger.error("Resource not found: " + message.toString());

        return message;
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
        ControllerAdviceExceptionMessage message = new ControllerAdviceExceptionMessage(
                req.getRequestURL().toString(),
                e.getLocalizedMessage()
        );

        logger.error("User not found: " + message.toString());

        return message;
    }


    /**
     * Handle all exceptions of type {@link  org.springframework.dao.OptimisticLockingFailureException}
     * when they occur from methods executed from the controller.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.exception.ControllerAdviceExceptionMessage} object
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseBody public ControllerAdviceExceptionMessage handleConcurencyCheckedFailed(HttpServletRequest req, Exception e) {
        ControllerAdviceExceptionMessage message = new ControllerAdviceExceptionMessage(
            req.getRequestURL().toString(),
            ((ObjectOptimisticLockingFailureException) e).getMostSpecificCause().getMessage()
        );

        logger.error("ConcurencyCheckedFailed: " + e);

        return message;
    }


    /**
     * Default exception handler.
     * Will catch all bad requests, but will not provide further details of the error.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.exception.ControllerAdviceExceptionMessage} object
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody public ControllerAdviceExceptionMessage defaultErrorHandler(HttpServletRequest req, Exception e)  {
        ControllerAdviceExceptionMessage message = new ControllerAdviceExceptionMessage(
                req.getRequestURL().toString(),
                e.getLocalizedMessage()
        );

        message.setUserfriendlyMessage( getRootCauseMessage(e.getCause()));
        logger.error(e.getClass().getSimpleName(),e);

        return message;
    }



}