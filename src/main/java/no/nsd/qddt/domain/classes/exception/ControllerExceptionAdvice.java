package no.nsd.qddt.domain.classes.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static net.logstash.logback.encoder.org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;

//import static net.logstash.logback.encoder.org.apache.commons.lang.exception.ExceptionUtils.getRootCauseMessage;

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
     * Handle all exceptions of type {@link no.nsd.qddt.domain.classes.exception.ResourceNotFoundException}
     * when they occur from methods executed from the controller.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.domain.classes.exception.ControllerAdviceExceptionMessage} object
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody public ControllerAdviceExceptionMessage handleResourceNotFound(HttpServletRequest req, Exception e) {
        ControllerAdviceExceptionMessage message = new ControllerAdviceExceptionMessage(
                req.getRequestURL().toString(),
                e.getLocalizedMessage()
        );

        logger.error("EntityModeL not found: " + message.toString());

        return message;
    }


    /**
     * Handle all exceptions of type {@link no.nsd.qddt.domain.classes.exception.UserNotFoundException}
     * when they occur from methods executed from the controller.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.domain.classes.exception.ControllerAdviceExceptionMessage} object
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

//
//    /**
//     * Handle all exceptions of type {@link OAuth2AuthenticationException}
//     * when they occur from methods executed from the controller.
//     * @param req servlet request
//     * @param e general exception
//     * @return a {@link no.nsd.qddt.domain.classes.exception.ControllerAdviceExceptionMessage} object
//     */
//    @ResponseStatus(HttpStatus.NOT_MODIFIED)
//    @ExceptionHandler(OAuth2AuthenticationException.class)
//    @ResponseBody public ControllerAdviceExceptionMessage handleDeniedAuthorization(HttpServletRequest req, Exception e) {
//        ControllerAdviceExceptionMessage message = new ControllerAdviceExceptionMessage(
//                req.getRequestURL().toString(),
//                e.getLocalizedMessage()
//        );
//
//        logger.error("Password missmatch: " + message.toString());
//
//        return message;
//    }

    /**
     * Handle all exceptions of type {@link  org.springframework.dao.OptimisticLockingFailureException}
     * when they occur from methods executed from the controller.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.domain.classes.exception.ControllerAdviceExceptionMessage} object
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
     * Handle all exceptions of type {@link  org.springframework.dao.OptimisticLockingFailureException}
     * when they occur from methods executed from the controller.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.domain.classes.exception.ControllerAdviceExceptionMessage} object
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    @ResponseBody public ControllerAdviceExceptionMessage handleRetrievalFailure(HttpServletRequest req, Exception e) {
        ControllerAdviceExceptionMessage message = new ControllerAdviceExceptionMessage(
            req.getRequestURL().toString(),
            ((JpaObjectRetrievalFailureException) e).getMostSpecificCause().getMessage()
        );
        if (message.getExceptionMessage().contains( "Category" ) && message.getUrl().contains( "responsedomain" )) {
            message.setUserfriendlyMessage("Saving ResponseDomain failed.</BR>Can't add a MissingGroup to a deleted ResponseDomain.</br>Remove old ResponseDomain, add an active ResponseDomain, and then add MissingGroup...");
        } else
        message.setUserfriendlyMessage( "An Item required to complete the action, is no longer available.\r\n" +
        "(remove old reference, add a new one, and try again...)");
        logger.error("RetrievalFailure: " + e);

        return message;
    }

    /**
     * Handle all exceptions of type {@link  org.springframework.dao.OptimisticLockingFailureException}
     * when they occur from methods executed from the controller.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.domain.classes.exception.ControllerAdviceExceptionMessage} object
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ReferenceInUseException.class)
    @ResponseBody public ControllerAdviceExceptionMessage handleRefInUseFailure(HttpServletRequest req, Exception e) {
        ControllerAdviceExceptionMessage message = new ControllerAdviceExceptionMessage(
            req.getRequestURL().toString(),
             e.getMessage()
        );
        message.setUserfriendlyMessage( "User are author of active items and cannot be deleted, try to disable user instead." );

        return message;
    }


    /**
     * Handle all exceptions of type {@link  org.springframework.security.access.AccessDeniedException}
     * when they occur from methods executed from the controller.
     * @param req servlet request
     * @param e general exception
     * @return a {@link org.springframework.security.access.AccessDeniedException} object
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = org.springframework.security.access.AccessDeniedException.class)
    @ResponseBody public ControllerAdviceExceptionMessage handleAccessDeniedException(HttpServletRequest req, Exception e)  {
        ControllerAdviceExceptionMessage message = new ControllerAdviceExceptionMessage(
            req.getRequestURL().toString(),
            e.getLocalizedMessage()
        );

        message.setUserfriendlyMessage( getRootCauseMessage(e.getCause()));
        logger.error(e.getClass().getSimpleName(),e);

        return message;
    }


    /**
     * Handle all exceptions of type {@link  no.nsd.qddt.domain.classes.exception.InvalidPasswordException}
     * when they occur from methods executed from the controller.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.domain.classes.exception.InvalidPasswordException} object
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = InvalidPasswordException.class)
    @ResponseBody public ControllerAdviceExceptionMessage handleInvalidPasswordException(HttpServletRequest req, Exception e)  {
        ControllerAdviceExceptionMessage message = new ControllerAdviceExceptionMessage(
            req.getRequestURL().toString(),
            e.getLocalizedMessage()
        );

        message.setUserfriendlyMessage( getRootCauseMessage(e.getCause()));
        logger.error(e.getClass().getSimpleName(),e);

        return message;
    }


    /**
     * Handle all exceptions of type {@link  DescendantsArchivedException}
     * when they occur from methods executed from the controller.
     * @param req servlet request
     * @param e general exception
     * @return a {@link DescendantsArchivedException} object
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = DescendantsArchivedException.class)
    @ResponseBody public ControllerAdviceExceptionMessage handleDescendantsArchivedException (HttpServletRequest req, Exception e)  {
        ControllerAdviceExceptionMessage message = new ControllerAdviceExceptionMessage(
            req.getRequestURL().toString(),
            e.getLocalizedMessage()
        );

        message.setUserfriendlyMessage( getRootCauseMessage(e.getCause()));

        return message;
    }

    /**
     * Default exception handler.
     * Will catch all bad requests, but will not provide further details of the error.
     * @param req servlet request
     * @param e general exception
     * @return a {@link no.nsd.qddt.domain.classes.exception.ControllerAdviceExceptionMessage} object
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
        logger.error( req.getRequestURI()  );
        logger.error("stacktrace", e.fillInStackTrace() );

        return message;
    }

}
